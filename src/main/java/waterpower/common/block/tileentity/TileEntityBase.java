/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.common.block.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ITickable;
import waterpower.Reference;
import waterpower.WaterPower;
import waterpower.common.block.IDroppable;
import waterpower.common.network.MessagePacketHandler;
import waterpower.common.network.PacketTileEntity;

public class TileEntityBase extends TileEntity implements ITickable, IDroppable {

    public TileEntityBase() {
        tick = Reference.General.updateTick;
    }

    public void sendUpdateToClient() {
        if (isServerSide()) {
            PacketTileEntity packet = new PacketTileEntity(this);
            packet.tag = new NBTTagCompound();
            writePacketData(packet.tag);
            if (!packet.tag.hasNoTags())
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

    @Override
    public ItemStack getDroppedItemStack() {
        return new ItemStack(getBlockState().getBlock(), 1, getBlockMetadata());
    }

    public boolean isRedstonePowered() {
        return this.worldObj.isBlockIndirectlyGettingPowered(this.getPos()) > 0;
    }

    public void notifyNeighborTileChange() {
        if (getBlockType() != null) {
            this.worldObj.notifyNeighborsRespectDebug(this.getPos(), getBlockType());
        }
    }

    public void onNeighborTileChange(BlockPos pos) {
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
    public void update() {

        if (isServerSide() && !isRedstonePowered() && tick-- == 0) {
            onUpdate();
            tick = Reference.General.updateTick;

            sendUpdateToClient();
        }
    }
    
    protected IBlockState getBlockState() {
    	return getBlockType().getDefaultState();
    }
    
    protected final void rerender() {
    	IBlockState state = getBlockState();
    	worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), state, 3);
    	//worldObj.setBlockState(pos, state);
    }

}
