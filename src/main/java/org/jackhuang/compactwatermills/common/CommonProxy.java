package org.jackhuang.compactwatermills.common;

import org.jackhuang.compactwatermills.client.gui.ContainerRotor;
import org.jackhuang.compactwatermills.client.gui.ContainerStandardMachine;
import org.jackhuang.compactwatermills.client.gui.DefaultGuiIds;
import org.jackhuang.compactwatermills.common.block.machines.ContainerCentrifuge;
import org.jackhuang.compactwatermills.common.block.machines.TileEntityAdvancedCompressor;
import org.jackhuang.compactwatermills.common.block.machines.TileEntityCentrifuge;
import org.jackhuang.compactwatermills.common.block.machines.TileEntityCompressor;
import org.jackhuang.compactwatermills.common.block.machines.TileEntityCutter;
import org.jackhuang.compactwatermills.common.block.machines.TileEntityLathe;
import org.jackhuang.compactwatermills.common.block.machines.TileEntityMacerator;
import org.jackhuang.compactwatermills.common.block.machines.TileEntitySawmill;
import org.jackhuang.compactwatermills.common.block.reservoir.ContainerReservoir;
import org.jackhuang.compactwatermills.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.compactwatermills.common.block.turbines.TileEntityTurbine;
import org.jackhuang.compactwatermills.common.block.watermills.ContainerWatermill;
import org.jackhuang.compactwatermills.common.block.watermills.TileEntityWatermill;
import org.jackhuang.compactwatermills.common.network.CompactWatermillsPacket;
import org.jackhuang.compactwatermills.helpers.LogHelper;

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
