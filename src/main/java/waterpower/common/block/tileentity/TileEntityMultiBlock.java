/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.common.block.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;
import waterpower.Reference;
import waterpower.client.gui.IHasGui;

public abstract class TileEntityMultiBlock extends TileEntityLiquidTankInventory implements IHasGui {

    public TileEntityMultiBlock masterBlock;
    protected boolean tested;
    private int masterState;
    private BlockPos master;
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
            master = block.pos.toImmutable();
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
            master = BlockPos.fromLong(tag.getLong("masterPos"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        if (masterBlock == this)
            masterState = 0;
        else if (masterBlock == null)
            masterState = 1;
        else
            masterState = 2;
        nbtTagCompound.setInteger("master", masterState);
        if (masterState == 2)
            nbtTagCompound.setLong("masterPos", masterBlock.pos.toLong());
        
        return nbtTagCompound;
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
            master = BlockPos.fromLong(tag.getLong("masterPos"));
            TileEntity te = worldObj.getTileEntity(master);
            if (te instanceof TileEntityMultiBlock)
                masterBlock = (TileEntityMultiBlock) te;
        }
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);

        tag.setInteger("master", masterState);
        if (masterState == 2) {
            TileEntity te = worldObj.getTileEntity(master);
            if (te instanceof TileEntityMultiBlock)
                masterBlock = (TileEntityMultiBlock) te;
            tag.setLong("masterPos", masterBlock.pos.toLong());
        }
    }

    protected abstract ArrayList<TileEntityMultiBlock> test();

    @Override
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
    public void update() {
    	super.update();
    	
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
    }

    public boolean isMaster() {
        return masterBlock == null || masterBlock == this;
    }

    protected abstract void onTestFailed();
}
