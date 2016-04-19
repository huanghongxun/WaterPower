/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.common.network.MessagePacketHandler;
import org.jackhuang.watercraft.common.network.PacketTileEntity;

public class TileEntityBase extends TileEntity {

    public TileEntityBase() {
        tick = Reference.General.updateTick;
    }

    public void sendUpdateToClient() {
        if (isServerSide()) {
            PacketTileEntity packet = new PacketTileEntity(this);
            packet.tag = new NBTTagCompound();
            writePacketData(packet.tag);
            if (!packet.tag.func_150296_c().isEmpty())
                MessagePacketHandler.INSTANCE.sendToAll(packet);
        }
    }

    public boolean isServerSide() {
        return WaterPower.isServerSide();
    }

    public void writePacketData(NBTTagCompound tag) {

    }

    public void readPacketData(NBTTagCompound tag) {

    }

    public boolean isRedstonePowered() {
        return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
    }

    public void notifyNeighborTileChange() {
        if (getBlockType() != null) {
            this.worldObj.func_147453_f(this.xCoord, this.yCoord, this.zCoord, getBlockType());
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

        if (isServerSide() && !isRedstonePowered() && tick-- == 0) {
            onUpdate();
            tick = Reference.General.updateTick;

            sendUpdateToClient();
        }
    }

}
