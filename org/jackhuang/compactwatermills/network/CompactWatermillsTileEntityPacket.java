package org.jackhuang.compactwatermills.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;

import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.TileEntityBase;

public class CompactWatermillsTileEntityPacket {
	protected boolean isChunkDataPacket = false;
	protected String channel = Reference.ModChannel;
	
	public int x, y, z;
	public NBTTagCompound tag;
	
	public CompactWatermillsTileEntityPacket(TileEntityBase entity) {
		x = entity.xCoord;
		y = entity.yCoord;
		z = entity.zCoord;
		tag = new NBTTagCompound();
		entity.writePacketData(tag);
	}
	

	public Packet getPacket() {

		Packet132TileEntityData packet = new Packet132TileEntityData();
		packet.xPosition = x;
		packet.yPosition = y;
		packet.zPosition = z;
		packet.isChunkDataPacket = isChunkDataPacket;
		packet.actionType = 1;
		packet.data = tag;
		return packet;
	}
}
