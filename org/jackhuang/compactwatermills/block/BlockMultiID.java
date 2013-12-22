package org.jackhuang.compactwatermills.block;

import ic2.api.tile.IWrenchable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.gui.IHasGUI;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.tileentity.TileEntityBlock;

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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;

public abstract class BlockMultiID extends BlockBase {

	private static TileEntity teBeforeBreak = null;

	public BlockMultiID(Configuration config, InternalName name,
			Material material, Class<? extends ItemBlock> c) {
		super(config, name, material, c);
	}

	@Override
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
		int subIndex = getTextureSubIndex(facing, side);
		// LogHelper.log("meta = " + meta + "; index = " + index + "; facing = "
		// + facing + "; side = " + side + "; subIndex = " + subIndex);
		try {
			return textures[index % textures.length][subIndex];
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
				LogHelper.debugLog("l=" + l);
			}
		}
	}

	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
		super.onBlockPreDestroy(world, x, y, z, meta);

		TileEntity te = world.getBlockTileEntity(x, y, z);

		if ((te instanceof TileEntityBlock)) {
			TileEntityBlock teb = (TileEntityBlock) te;

			if (teb.loaded)
				teb.onUnloaded();
		}
	}

	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y,
			int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = super.getBlockDropped(world, x, y, z,
				metadata, fortune);

		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te == null)
			te = teBeforeBreak;

		if ((te instanceof IInventory)) {
			IInventory inv = (IInventory) te;

			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack itemStack = inv.getStackInSlot(i);

				if (itemStack != null) {
					ret.add(itemStack);
					inv.setInventorySlotContents(i, null);
				}
			}
		}

		return ret;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if ((te instanceof TileEntityBlock))
			((TileEntityBlock) te).onBlockBreak(id, meta);

		if (te != null)
			teBeforeBreak = te;
		else {
			teBeforeBreak = null;
		}

		super.breakBlock(world, x, y, z, id, meta);
	}
	
	protected abstract int maxMetaData();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs,
			List itemList) {
		Item item = Item.itemsList[this.blockID];
		if (!item.getHasSubtypes())
			itemList.add(new ItemStack(this));
		else {
			for (int i = 0; i < maxMetaData(); i++) {
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

	public ItemStack getPickBlock(MovingObjectPosition target, World world,
			int x, int y, int z) {
		return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
	}
}
