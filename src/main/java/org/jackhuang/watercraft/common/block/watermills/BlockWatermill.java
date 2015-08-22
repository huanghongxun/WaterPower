/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.block.watermills;

import java.util.ArrayList;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.block.BlockMeta;
import org.jackhuang.watercraft.common.block.BlockRotor;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWatermill extends BlockRotor {
	
	public BlockWatermill() {
		super("cptBlockCompactWatermill", Material.iron, ItemWatermill.class);
		
		GameRegistry.registerTileEntity(TileEntityWatermill.class,
				"cptwtrml.watermill");		
	}
	
	@Override
	protected int getTextureIndex(IBlockAccess iBlockAccess, int x, int y, int z, int meta) {
		TileEntity tTileEntity = iBlockAccess.getBlockTileEntity(x, y, z);
		if(tTileEntity instanceof TileEntityWatermill) {
			if(((TileEntityWatermill)tTileEntity).getType() == null) return meta;
			return ((TileEntityWatermill)tTileEntity).getType().ordinal();
		}
		return meta;
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		textures = new Icon[maxMetaData()][6];
		
		Icon iconDown = iconRegister.registerIcon(Reference.ModID + ":watermill/DOWN");
		Icon iconUp = iconRegister.registerIcon(Reference.ModID + ":watermill/UP");
		
		for (int i = 0; i < maxMetaData(); i++) {
			textures[i][0] = iconDown;
			textures[i][1] = iconUp;
			textures[i][2] = textures[i][3] = textures[i][4] = textures[i][5] =
					iconRegister.registerIcon(Reference.ModID + ":watermill/" + getTextureName(i));
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return WaterType.makeTileEntity(0);
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return WaterType.makeTileEntity(metadata);
	}
	
	@Override
	protected String getTextureFolder(int index) {
		return "watermill";
	}

	@Override
	protected int maxMetaData() {
		return WaterType.values().length;
	}

	public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aX, int aY,
			int aZ, int aLogLevel) {
		ArrayList<String> al = new ArrayList<String>();
		TileEntity tileEntity = aPlayer.worldObj.getBlockTileEntity(aX, aY, aZ);
		if(tileEntity instanceof TileEntityWatermill) {
			TileEntityWatermill te = (TileEntityWatermill) tileEntity;
			if(te.getType() == null)
				al.add("Type: null");
			else
				al.add("Type: " + te.getType().name());
			al.add("Output: " + te.getOfferedEnergy());
			al.add("Range: " + te.getRange());
		} else {
			al.add("Not a watermill tile entity.");
		}
		return al;
	}
	
	
	
}
