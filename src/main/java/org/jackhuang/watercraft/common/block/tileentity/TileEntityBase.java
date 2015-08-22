/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.block.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.common.network.WaterPowerTileEntityPacket;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity {

    public TileEntityBase() {
        tick = WaterPower.updateTick;
    }

    public void sendUpdateToClient() {
        if (isServerSide()) {
        	WaterPowerTileEntityPacket packet = new WaterPowerTileEntityPacket(this);
    		PacketDispatcher.sendPacketToAllPlayers(packet.getPacket());
        }
    }
    
    @Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readPacketData(pkt.data);
	}

    public boolean isServerSide() {
        return WaterPower.isServerSide();
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
            this.worldObj.notifyBlockOfNeighborChange(this.xCoord, this.yCoord, this.zCoord,
                    getBlockType().blockID);
        }
    }

    public void onNeighborTileChange(int x, int y, int z) {
    }

    public void onNeighborBlockChange() {
    }

    public boolean isActive() {
        return true;
    }

    @Override
    public void validate() {
        onLoaded();

        super.validate();
    }

    @Override
    public void invalidate() {
        if (loaded)
            onUnloaded();
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        if (loaded)
            onUnloaded();
        super.onChunkUnload();
    }

    protected boolean loaded = false;

    public void onLoaded() {
        loaded = true;
    }

    public void onUnloaded() {
        loaded = false;
    }

    protected void onUpdate() {
    }

    private int tick;

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (isServerSide() && !isRedstonePowered()) {
            if (tick-- == 0) {
                onUpdate();
                tick = WaterPower.updateTick;

                sendUpdateToClient();
            }
        }
    }

}
