/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.jackhuang.watercraft.common.network.PacketDispatcher;
import org.jackhuang.watercraft.common.network.WCTileEntityPacket;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity {

	public void sendUpdateToClient() {
		PacketDispatcher.sendToAll(new WCTileEntityPacket(this));
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
