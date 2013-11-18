package org.jackhuang.compactwatermills;

import org.jackhuang.compactwatermills.block.turbines.ClientGUIReservoir;
import org.jackhuang.compactwatermills.block.turbines.ClientGUITurbine;
import org.jackhuang.compactwatermills.block.turbines.TileEntityReservoir;
import org.jackhuang.compactwatermills.block.turbines.TileEntityTurbine;
import org.jackhuang.compactwatermills.block.watermills.TileEntityWatermill;
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
				return ClientGUITurbine.makeContainer(thePlayer, tileEntityT);
			}
		} else if (ID == DefaultGuiIds.get("tileEntityWatermill")) {
			TileEntityWatermill tileEntityCW = (TileEntityWatermill) tileEntity;
			return ClientGUIRotor.makeContainer(thePlayer, tileEntityCW);
		} else if (ID == DefaultGuiIds.get("tileEntityReservoir")) {
			TileEntityReservoir tileEntityR = (TileEntityReservoir) tileEntity;
			LogHelper.log("server = " + tileEntityR.masterBlock);
			return ClientGUIReservoir.makeContainer(thePlayer, tileEntityR);
		}
		return null;
	}

	public static void sendToPlayers(Packet packet, World world, int x, int y, int z,
			int maxDistance) {
		if (packet != null) {
			for (int j = 0; j < world.playerEntities.size(); j++) {
				EntityPlayerMP player = (EntityPlayerMP) world.playerEntities
						.get(j);

				if (Math.abs(player.posX - x) <= maxDistance
						&& Math.abs(player.posY - y) <= maxDistance
						&& Math.abs(player.posZ - z) <= maxDistance) {
					player.playerNetServerHandler.sendPacketToPlayer(packet);
				}
			}
		}
	}

	public static void sendToPlayer(EntityPlayer entityplayer,
			CompactWatermillsPacket packet) {
		EntityPlayerMP player = (EntityPlayerMP) entityplayer;
		player.playerNetServerHandler.sendPacketToPlayer(packet.getPacket());
	}

}
