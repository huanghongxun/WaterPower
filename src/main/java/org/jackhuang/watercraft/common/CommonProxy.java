/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common;

import org.jackhuang.watercraft.client.gui.ContainerRotor;
import org.jackhuang.watercraft.client.gui.ContainerStandardMachine;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.block.machines.ContainerCentrifuge;
import org.jackhuang.watercraft.common.block.machines.TileEntityAdvancedCompressor;
import org.jackhuang.watercraft.common.block.machines.TileEntityCentrifuge;
import org.jackhuang.watercraft.common.block.machines.TileEntityCompressor;
import org.jackhuang.watercraft.common.block.machines.TileEntityCutter;
import org.jackhuang.watercraft.common.block.machines.TileEntityLathe;
import org.jackhuang.watercraft.common.block.machines.TileEntityMacerator;
import org.jackhuang.watercraft.common.block.machines.TileEntitySawmill;
import org.jackhuang.watercraft.common.block.reservoir.ContainerReservoir;
import org.jackhuang.watercraft.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.watercraft.common.block.turbines.TileEntityTurbine;
import org.jackhuang.watercraft.common.block.watermills.ContainerWatermill;
import org.jackhuang.watercraft.common.block.watermills.TileEntityWatermill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer thePlayer,
			World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null)
			return null;
		if (ID == DefaultGuiIds.get("tileEntityTurbine").id) {
			if (tileEntity instanceof TileEntityTurbine) {
				TileEntityTurbine tileEntityT = (TileEntityTurbine) tileEntity;
				return new ContainerRotor(thePlayer, tileEntityT);
			}
		} else if (ID == DefaultGuiIds.get("tileEntityWatermill").id) {
			TileEntityWatermill tileEntityCW = (TileEntityWatermill) tileEntity;
			return new ContainerWatermill(thePlayer, tileEntityCW);
		} else if (ID == DefaultGuiIds.get("tileEntityReservoir").id) {
			TileEntityReservoir tileEntityR = (TileEntityReservoir) tileEntity;
			return new ContainerReservoir(thePlayer, tileEntityR);
		} else if(ID == DefaultGuiIds.get("tileEntityMacerator").id) {
			TileEntityMacerator tileEntityR = (TileEntityMacerator) tileEntity;
			return new ContainerStandardMachine(thePlayer, tileEntityR);
		} else if(ID == DefaultGuiIds.get("tileEntityCompressor").id) {
			TileEntityCompressor tileEntityR = (TileEntityCompressor) tileEntity;
			return new ContainerStandardMachine(thePlayer, tileEntityR);
		} else if(ID == DefaultGuiIds.get("tileEntityAdvancedCompressor").id) {
			TileEntityAdvancedCompressor tileEntityR = (TileEntityAdvancedCompressor) tileEntity;
			return new ContainerStandardMachine(thePlayer, tileEntityR);
		} else if(ID == DefaultGuiIds.get("tileEntitySawmill").id) {
			TileEntitySawmill tileEntityR = (TileEntitySawmill) tileEntity;
			return new ContainerStandardMachine(thePlayer, tileEntityR);
		} else if(ID == DefaultGuiIds.get("tileEntityLathe").id) {
			TileEntityLathe tileEntityR = (TileEntityLathe) tileEntity;
			return new ContainerStandardMachine(thePlayer, tileEntityR);
		} else if(ID == DefaultGuiIds.get("tileEntityCutter").id) {
			TileEntityCutter tileEntityR = (TileEntityCutter) tileEntity;
			return new ContainerStandardMachine(thePlayer, tileEntityR);
		} else if(ID == DefaultGuiIds.get("tileEntityCentrifuge").id) {
			TileEntityCentrifuge tileEntityR = (TileEntityCentrifuge) tileEntity;
			return new ContainerCentrifuge(thePlayer, tileEntityR);
		}
		return null;
	}
	
	public void registerRenderer() {
		
	}

}
