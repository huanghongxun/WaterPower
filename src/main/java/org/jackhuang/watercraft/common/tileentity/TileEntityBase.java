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

import org.jackhuang.watercraft.common.network.MessagePacketHandler;
import org.jackhuang.watercraft.common.network.PacketTileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity {

	public void sendUpdateToClient() {
		// FMLPacketDispatcher.sendToAll(new WCTileEntityPacket(this));
		MessagePacketHandler.INSTANCE.sendToAll(new PacketTileEntity(this));
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

	public void notifyNeighborTileChange() {
		if (getBlockType() != null) {
			this.worldObj.func_147453_f(this.xCoord, this.yCoord, this.zCoord,
					getBlockType());
		}
	}

	public void onNeighborTileChange(int paramInt1, int paramInt2, int paramInt3) {
	}

	public void onNeighborBlockChange() {
	}

	public boolean isActive() {
		return true;
	}

}
