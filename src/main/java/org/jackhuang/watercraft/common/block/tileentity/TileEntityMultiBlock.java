/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.block.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.client.gui.IHasGui;

public abstract class TileEntityMultiBlock extends TileEntityLiquidTankInventory implements IHasGui {

    public TileEntityMultiBlock masterBlock;
    protected boolean tested;
    private int masterState, masterX, masterY, masterZ;
    private int tick = 0, tick2 = Reference.General.updateTick;
    protected List<TileEntityMultiBlock> blockList;

    public TileEntityMultiBlock(int tankSize) {
        super(tankSize);
        this.tested = isServerSide();
    }

    protected void setMaster(TileEntityMultiBlock block) {
        if (block == null) {
            masterState = 1;
        } else if (block == this) {
            masterState = 0;
        } else {
            masterState = 2;
            masterX = block.xCoord;
            masterY = block.yCoord;
            masterZ = block.zCoord;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        masterState = tag.getInteger("master");

        if (masterState == 0)
            masterBlock = this;
        else if (masterState == 1)
            masterBlock = null;
        else {
            masterX = tag.getInteger("masterX");
            masterY = tag.getInteger("masterY");
            masterZ = tag.getInteger("masterZ");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        if (masterBlock == this)
            masterState = 0;
        else if (masterBlock == null)
            masterState = 1;
        else
            masterState = 2;
        nbtTagCompound.setInteger("master", masterState);
        if (masterState == 2) {
            nbtTagCompound.setInteger("masterX", masterBlock.xCoord);
            nbtTagCompound.setInteger("masterY", masterBlock.yCoord);
            nbtTagCompound.setInteger("masterZ", masterBlock.zCoord);
        }
    }

    @Override
    public void readPacketData(NBTTagCompound tag) {
        super.readPacketData(tag);

        masterState = tag.getInteger("master");

        if (masterState == 0)
            masterBlock = this;
        else if (masterState == 1)
            masterBlock = null;
        else {
            masterX = tag.getInteger("masterX");
            masterY = tag.getInteger("masterY");
            masterZ = tag.getInteger("masterZ");
            TileEntity te = worldObj.getTileEntity(masterX, masterY, masterZ);
            if (te instanceof TileEntityMultiBlock)
                masterBlock = (TileEntityMultiBlock) te;
        }
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);

        tag.setInteger("master", masterState);
        if (masterState == 2) {
            tag.setInteger("masterX", masterX);
            tag.setInteger("masterY", masterY);
            tag.setInteger("masterZ", masterZ);
            TileEntity te = worldObj.getTileEntity(masterX, masterY, masterZ);
            if (te instanceof TileEntityMultiBlock)
                masterBlock = (TileEntityMultiBlock) te;
        }
    }

    protected abstract ArrayList<TileEntityMultiBlock> test();

    protected void onUpdate() {
    }

    protected boolean canBeMaster() {
        return true;
    }

    @Override
    public FluidTank getFluidTank() {
        if (isMaster())
            return fluidTank;
        else
            return masterBlock.getFluidTank();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (worldObj == null || worldObj.isRemote)
            return;

        if (tick-- == 0) {
            tick = Reference.General.updateTick;
            if (canBeMaster()) {

                tested = true;
                ArrayList<TileEntityMultiBlock> newBlockList = test();
                boolean changed = blockList == null || (blockList != null && (newBlockList == null || !newBlockList.equals(blockList)));
                if (changed && blockList != null) {
                    for (TileEntityMultiBlock block : blockList) {
                        block.tested = false;
                        block.setMaster(null);
                    }
                }
                blockList = newBlockList;
                if (blockList == null) {
                    tested = false;
                    onTestFailed();
                } else if (changed) {
                    for (TileEntityMultiBlock block : blockList) {
                        block.tested = true;
                        block.setMaster(this);
                    }
                }
            }
        }

        if (tick2-- == 0) {
            tick2 = Reference.General.updateTick;
            onUpdate();
        }
    }

    public boolean isMaster() {
        return masterBlock == null || masterBlock == this;
    }

    protected abstract void onTestFailed();
}
