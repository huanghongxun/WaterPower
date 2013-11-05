package org.jackhuang.compactwatermills.block;

import ic2.api.tile.IWrenchable;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.IHasGUI;
import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.TileEntityBlock;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;

public abstract class BlockMultiID extends BlockBase {

	public BlockMultiID(Configuration config, InternalName name,
			Material material, Class<? extends ItemBlock> c) {
		super(config, name, material, c);
	}

	public int getFacing(IBlockAccess iBlockAccess, int x, int y, int z) {
		TileEntity te = iBlockAccess.getBlockTileEntity(x, y, z);

		if ((te instanceof TileEntityBlock)) {
			return ((TileEntityBlock) te).getFacing();
		}
		int meta = iBlockAccess.getBlockMetadata(x, y, z);

		return getFacing(meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z,
			int side) {
		int facing = getFacing(iBlockAccess, x, y, z);
		int meta = iBlockAccess.getBlockMetadata(x, y, z);

		int index = getTextureIndex(meta);
		if (index >= textures.length) {
			return null;
		}
		int subIndex = getTextureSubIndex(facing, side);
		// LogHelper.log("meta = " + meta + "; index = " + index + "; facing = "
		// + facing + "; side = " + side + "; subIndex = " + subIndex);
		try {
			return textures[index][subIndex];
		} catch (Exception e) {
			LogHelper.log(Level.SEVERE, "Failed to get texture at: x=" + x
					+ "; y=" + y + "; z=" + z + "; facing=" + facing
					+ "; side=" + side + "; meta=" + meta + ";");
		}

		return null;
	}

	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entityliving, ItemStack itemStack) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if ((tileEntity instanceof IWrenchable)) {
			IWrenchable te = (IWrenchable) tileEntity;

			if (entityliving == null) {
				te.setFacing((short) 2);
				LogHelper.log("placed 2");
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
				LogHelper.log("placed l: " + l);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs,
			List itemList) {
		Item item = Item.itemsList[this.blockID];
		if (!item.getHasSubtypes())
			itemList.add(new ItemStack(this));
		else {
			for (int i = 0; i < 16; i++) {
				ItemStack is = new ItemStack(this, 1, i);

				if (Item.itemsList[this.blockID].getUnlocalizedName(is) == null)
					break;
				itemList.add(is);
			}
		}
	}

	@Override
	public int idDropped(int meta, Random random, int id) {
		return blockID;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer entityPlayer, int s, float f1, float f2, float f3) {
		if (entityPlayer.isSneaking())
			return false;

		TileEntity te = world.getBlockTileEntity(x, y, z);

		if ((te instanceof IHasGUI)) {
			// return CompactWatermills.launchGui(entityPlayer, (IHasGUI)te);

			entityPlayer.openGui(CompactWatermills.instance,
					((IHasGUI) te).getGuiId(), world, x, y, z);
			return true;
		}

		return false;
	}

	public boolean rotateBlock(World worldObj, int x, int y, int z,
			ForgeDirection axis) {
		if (axis == ForgeDirection.UNKNOWN)
			return false;

		TileEntity tileEntity = worldObj.getBlockTileEntity(x, y, z);

		if ((tileEntity instanceof IWrenchable)) {
			IWrenchable te = (IWrenchable) tileEntity;

			int newFacing = ForgeDirection.getOrientation(te.getFacing())
					.getRotation(axis).ordinal();

			if (te.wrenchCanSetFacing(null, newFacing)) {
				te.setFacing((short) newFacing);
			}
		}

		return false;
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, World world, int x,
			int y, int z) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		int metaCount = getMetaCount();

		this.textures = new Icon[metaCount][12];

		for (int index = 0; index < metaCount; index++) {
			String textureFolder = getTextureFolder(index);
			textureFolder = textureFolder + "/";

			String name = Reference.ModID + ":" + textureFolder
					+ getTextureName(index);

			for (int active = 0; active < 1; active++)
				for (int side = 0; side < 6; side++) {
					int subIndex = active * 6 + side;
					String subName = name + ":" + subIndex;

					TextureAtlasSprite texture = new BlockTextureStitched(
							subName, subIndex);

					this.textures[index][subIndex] = texture;
					((TextureMap) iconRegister).setTextureEntry(subName,
							texture);
				}
		}
	}

	@Override
	public abstract TileEntity createNewTileEntity(World world);
}
