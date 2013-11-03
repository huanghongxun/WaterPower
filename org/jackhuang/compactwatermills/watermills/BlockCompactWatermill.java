/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package org.jackhuang.compactwatermills.watermills;


import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Aroma1997
 * 
 */
public class BlockCompactWatermill extends BlockContainer {
	
	public static final int[][] sideAndMetaToTextureNumber = { {0, 0, 0, 0, 0, 0}, {1, 2, 1, 1, 1, 1}, {3, 3, 2, 3, 3, 3}, {3, 3, 3, 2, 3, 3}, {3, 3, 3, 3, 2, 3}, {3, 3, 3, 3, 3, 2}};
	
	@SideOnly(Side.CLIENT)
	private Icon[][] textures;
	
	public BlockCompactWatermill(int id) {
		super(id, Material.iron);
		setUnlocalizedName("compactWatermill");
		setHardness(2.0F);
		setCreativeTab(CompactWatermills.creativeTabCompactWatermills);
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return WaterType.makeTileEntity(metadata);
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
		int facing = getFacing(iBlockAccess, x, y, z);
		int meta = iBlockAccess.getBlockMetadata(x, y, z);
		int textureIndex = sideAndMetaToTextureNumber[side][facing];
		
		if (meta >= textures.length) {
			return null;
		}
		try {
			return textures[meta][textureIndex];
		}
		catch (Exception e) {
			LogHelper.log(Level.WARNING, "Failed to get texture at: x=" + x + "; y=" + y + "; z="
				+ z + "; facing=" + facing + "; side=" + side + "; meta=" + meta + ";");
		}
		
		return null;
	}
	
	public int getFacing(IBlockAccess iBlockAccess, int x, int y, int z) {
		TileEntity tileEntity = iBlockAccess.getBlockTileEntity(x, y, z);
		
		if (tileEntity instanceof TileEntityWatermill) {
			return ((TileEntityWatermill) tileEntity).getFacing();
		}
		
		LogHelper.log(Level.WARNING, "Failed to get Facing at: x=" + x + "; y=" + y + "; z=" + z
			+ ";");
		
		return 4;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		if (meta > WaterType.values().length) {
			return null;
		}
		int facing = 4;
		int textureIndex = sideAndMetaToTextureNumber[side][facing];
		try {
			return textures[meta][textureIndex];
		}
		catch (Exception e) {
			LogHelper.log(Level.WARNING, "Failed to get texture at: side=" + side + "; meta="
				+ meta + ";");
		}
		
		return null;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List itemList) {
		for (WaterType type : WaterType.values()) {
			itemList.add(new ItemStack(this, 1, type.ordinal()));
		}
	}
	
	@Override
	public int idDropped(int meta, Random random, int id) {
		return blockID;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer thePlayer,
		int s, float f1, float f2, float f3) {
		if (thePlayer.isSneaking()) {
			return false;
		}
		
		if (world.isRemote) {
			return true;
		}
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityWatermill) {
			TileEntityWatermill tileEntityCW = (TileEntityWatermill) tileEntity;
			thePlayer.openGui(CompactWatermills.instance, tileEntityCW.getType()
				.ordinal(), world, x, y, z);
		}
		return true;
	}
	
	@Override
	public int quantityDropped(Random random) {
		return 1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		textures = new Icon[WaterType.values().length][4];
		for (WaterType type : WaterType.values()) {
			for (int side = 0; side < 4; side++) {
				String sideName = side == 0 ? "bottom" : side == 1 ? "top"
					: "side";
				textures[type.ordinal()][side] = par1IconRegister
					.registerIcon(Reference.ModID + ":" + type.name() + "_"
						+ sideName);
			}
		}
		
	}
}
