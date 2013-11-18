package org.jackhuang.compactwatermills;

import org.jackhuang.compactwatermills.network.CompactWatermillsTileEntityPacket;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity {

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readPacketData(pkt.data);
	}

	public void sendUpdateToClient() {
		CompactWatermillsTileEntityPacket packet = new CompactWatermillsTileEntityPacket(this);
		PacketDispatcher.sendPacketToAllPlayers(packet.getPacket());
	}
	
	public void writePacketData(NBTTagCompound tag) {
		
	}
	
	public void readPacketData(NBTTagCompound tag) {
		
	}

}
