package org.jackhuang.compactwatermills;

import org.jackhuang.compactwatermills.block.reservoir.TileEntityReservoir;
import org.jackhuang.compactwatermills.block.turbines.TileEntityTurbine;
import org.jackhuang.compactwatermills.block.watermills.TileEntityWatermill;
import org.jackhuang.compactwatermills.gui.ClientGUIReservoir;
import org.jackhuang.compactwatermills.gui.ClientGUITurbine;
import org.jackhuang.compactwatermills.gui.ContainerReservoir;
import org.jackhuang.compactwatermills.gui.ContainerRotor;
import org.jackhuang.compactwatermills.gui.DefaultGuiIds;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.network.CompactWatermillsPacket;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet;
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
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity == null)
			return null;
		if (ID == DefaultGuiIds.get("tileEntityTurbine")) {
			if (tileEntity instanceof TileEntityTurbine) {
				TileEntityTurbine tileEntityT = (TileEntityTurbine) tileEntity;
				return new ContainerRotor(thePlayer, tileEntityT);
			}
		} else if (ID == DefaultGuiIds.get("tileEntityWatermill")) {
			TileEntityWatermill tileEntityCW = (TileEntityWatermill) tileEntity;
			return new ContainerRotor(thePlayer, tileEntityCW);
		} else if (ID == DefaultGuiIds.get("tileEntityReservoir")) {
			TileEntityReservoir tileEntityR = (TileEntityReservoir) tileEntity;
			return new ContainerReservoir(thePlayer, tileEntityR);
		}
		return null;
	}

}
