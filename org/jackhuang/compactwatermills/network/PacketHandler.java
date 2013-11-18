package org.jackhuang.compactwatermills.network;

import org.jackhuang.compactwatermills.block.turbines.TileEntityReservoir;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {

		try {
			CompactWatermillsPacket pkt = CompactWatermillsPacket.parse(packet.data);
			if(player instanceof EntityPlayerMP) {
				handlePacketFromClient(pkt, (EntityPlayerMP)player);
			} else {
				handlePacketFromServer(pkt);
			}
		} catch (Exception e) {
			
		}
	}
	
	public void handlePacketFromClient(CompactWatermillsPacket packet,
			EntityPlayerMP playerMP) {
		
	}
	
	@SideOnly(Side.CLIENT)
	public void handlePacketFromServer(CompactWatermillsPacket packet) {
		switch (packet.id) {
		case 1: //reservoir
			TileEntity tileEntity = Minecraft.getMinecraft().theWorld.
				getBlockTileEntity(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]);
			if(tileEntity != null && tileEntity instanceof TileEntityReservoir) {
				TileEntityReservoir reservoir = (TileEntityReservoir) tileEntity;
			}
			break;

		default:
			break;
		}
	}

}
