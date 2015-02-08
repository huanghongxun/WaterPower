package org.jackhuang.watercraft.common.block;

import ic2.api.tile.IWrenchable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.InternalName;
import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.client.gui.IHasGui;
import org.jackhuang.watercraft.common.tileentity.TileEntityBlock;
import org.jackhuang.watercraft.common.tileentity.TileEntityInventory;
import org.jackhuang.watercraft.util.WPLog;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockMultiID extends BlockBase {
	
	// Facing 4 - front 6 - BACK

	public BlockMultiID(InternalName name, Material material,
			Class<? extends ItemBlock> c) {
		super(name, material, c);
	}

	@Override
	public int getFacing(IBlockAccess iBlockAccess, int x, int y, int z) {
		TileEntity te = iBlockAccess.getTileEntity(x, y, z);

		if ((te instanceof TileEntityBlock)) {
			return ((TileEntityBlock) te).getFacing();
		}
		int meta = iBlockAccess.getBlockMetadata(x, y, z);

		return getFacing(meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess iBlockAccess, int x, int y, int z,
			int side) {
		int facing = getFacing(iBlockAccess, x, y, z);
		int meta = iBlockAccess.getBlockMetadata(x, y, z);

		int index = getTextureIndex(iBlockAccess, x, y, z, meta);
		int subIndex = getTextureSubIndex(facing, side);
		try {
			return textures[index][subIndex];
		} catch (Exception e) {
			WPLog.err(
					"BlockMultiID: Failed to get texture at: x=" + x + "; y="
							+ y + "; z=" + z + "; facing=" + facing + "; side="
							+ side + "; meta=" + meta + ";");
		}

		return null;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entityliving, ItemStack itemStack) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if ((tileEntity instanceof IWrenchable)) {
			IWrenchable te = (IWrenchable) tileEntity;
			if (entityliving == null) {
				te.setFacing((short) 2);
			} else {
				int l = MathHelper
						.floor_double(entityliving.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;

				switch (l) {
				case 0:
					te.setFacing((short) 2);
					break;
				case 1:
					te.setFacing((short) 5);
					break;
				case 2:
					te.setFacing((short) 3);
					break;
				case 3:
					te.setFacing((short) 4);
				}
			}
		}
	}

	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
		super.onBlockPreDestroy(world, x, y, z, meta);

		TileEntity te = world.getTileEntity(x, y, z);

		if ((te instanceof TileEntityBlock)) {
			TileEntityBlock teb = (TileEntityBlock) te;
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta) {
		TileEntity te = world.getTileEntity(x, y, z);
		if ((te instanceof TileEntityBlock))
			((TileEntityBlock) te).onBlockBreak(id, meta);

		super.breakBlock(world, x, y, z, block, meta);
	}

	protected abstract int maxMetaData();

	@Override
	public void getSubBlocks(Item item, CreativeTabs p_149666_2_, List itemList) {
		if (!item.getHasSubtypes())
			itemList.add(new ItemStack(this));
		else {
			for (int i = 0; i < maxMetaData(); i++) {
				ItemStack is = new ItemStack(this, 1, i);

				if (item.getUnlocalizedName(is) == null)
					break;
				itemList.add(is);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer entityPlayer, int s, float f1, float f2, float f3) {
		if (entityPlayer.isSneaking())
			return false;

		TileEntity te = world.getTileEntity(x, y, z);

		if ((te instanceof IHasGui)) {
			// return CompactWatermills.launchGui(entityPlayer, (IHasGUI)te);

			entityPlayer.openGui(WaterPower.instance,
					((IHasGui) te).getGuiId(), world, x, y, z);
			return true;
		}

		return false;
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world,
			int x, int y, int z) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		int metaCount = maxMetaData();

		this.textures = new IIcon[metaCount][6];

		for (int index = 0; index < metaCount; index++) {
			String textureFolder = getTextureFolder(index);
			textureFolder = textureFolder + "/";

			String name = Reference.ModID + ":" + textureFolder
					+ getTextureName(index);

				for (int side = 0; side < 6; side++) {
					String subName = name + ":" + side;

					this.textures[index][side] = iconRegister.registerIcon(name + "." + ForgeDirection.VALID_DIRECTIONS[side].name().toLowerCase());
				}
		}
	}

	public ItemStack getPickBlock(MovingObjectPosition target, World world,
			int x, int y, int z) {
		return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
	}
}
