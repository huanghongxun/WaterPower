package org.jackhuang.compactwatermills.common.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.jackhuang.compactwatermills.common.network.CompactWatermillsTileEntityPacket;
import org.jackhuang.compactwatermills.common.network.PacketDispatcher;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity {

	public void sendUpdateToClient() {
		PacketDispatcher.sendToAll(new CompactWatermillsTileEntityPacket(this));
	}
	
	public boolean isServerSide() {
		return !worldObj.isRemote;
	}
	
	public void writePacketData(NBTTagCompound tag) {
		
	}
	
	public void readPacketData(NBTTagCompound tag) {
		
	}

	public boolean isRedstonePowered() {
		return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord,
				this.yCoord, this.zCoord);
	}

}
