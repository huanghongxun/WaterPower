/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.integration.waila;

import java.util.List;

import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.block.watermills.TileEntityWatermill;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.Utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class HUDHandlerWatermills implements IWailaDataProvider {

	@Override
	public List<String> getWailaBody(ItemStack arg0, List<String> arg1,
			IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
		TileEntity te = arg2.getTileEntity();
		if(!(te instanceof TileEntityWatermill)) return arg1;
		TileEntityWatermill tile = (TileEntityWatermill) te;
		arg1.add(StatCollector.translateToLocal("cptwtrml.gui.latest_output") + ": " + Utils.DEFAULT_DECIMAL_FORMAT.format(tile.getFromEU(tile.latestOutput)) + tile.energyType.name());
		return arg1;
	}

	@Override
	public List<String> getWailaHead(ItemStack arg0, List<String> arg1,
			IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
		TileEntity te = arg2.getTileEntity();
		if(!(te instanceof TileEntityWatermill)) return arg1;
		TileEntityWatermill tile = (TileEntityWatermill) te;
		arg1.add(StatCollector.translateToLocal("cptwtrml.watermill.WATERMILL") + " " + tile.getType().name());
		return arg1;
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor arg0,
			IWailaConfigHandler arg1) {
        TileEntity te = arg0.getTileEntity();
        if(!(te instanceof TileEntityWatermill)) return null;
        TileEntityWatermill tile = (TileEntityWatermill) te;
        return new ItemStack(GlobalBlocks.waterMill, 1, tile.getType().ordinal());
	}

	@Override
	public List<String> getWailaTail(ItemStack arg0, List<String> arg1,
			IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
		// TODO Auto-generated method stub
		return arg1;
	}

}
