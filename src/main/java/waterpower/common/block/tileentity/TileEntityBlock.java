package waterpower.common.block.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import waterpower.common.block.BlockWaterPower;

public abstract class TileEntityBlock extends TileEntityLiquidTankInventory {

    public TileEntityBlock(int tanksize) {
        super(tanksize);
    }

    private EnumFacing facing = EnumFacing.EAST;
    private EnumFacing prevFacing = EnumFacing.UP;

    public boolean prevActive = false;
    private boolean needsUpdate = false;

    @Override
	public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        this.facing = EnumFacing.getFront(tag.getByte("facing"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setByte("facing", (byte) facing.ordinal());
        return tag;
    }

    @Override
    public void readPacketData(NBTTagCompound tag) {
        super.readPacketData(tag);

        if (tag.hasKey("facing"))
            this.prevFacing = this.facing = EnumFacing.getFront(tag.getByte("facing"));
        if (tag.hasKey("needsUpdate"))
            needsUpdate = tag.getBoolean("needsUpdate");
    }

    @Override
    public void update() {
        super.update();

        if (needsUpdate && !isServerSide()) {
        	rerender();
            needsUpdate = false;
        }
    }
    
    @Override
	public IBlockState getBlockState() {
    	IBlockState state = super.getBlockState();
    	if (state.getPropertyNames().contains(BlockWaterPower.FACING))
    	    return state.withProperty(BlockWaterPower.FACING, facing);
    	return state;
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);

        if (prevFacing != facing) {
            tag.setByte("facing", (byte) facing.ordinal());
            needsUpdate = true;
        }
        if (needsUpdate) {
            tag.setBoolean("needsUpdate", needsUpdate);
            needsUpdate = false;
        }
    }

    /*
     * ---------------------------------------
     * 
     * WRENCH(INDUSTRIAL CRAFT 2) INTEGRATION ENDS
     * 
     * ---------------------------------------
     */

    public EnumFacing getPrevFacing() {
        return this.prevFacing;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public boolean setFacing(EnumFacing side) {
    	if (!EnumFacing.Plane.HORIZONTAL.apply(side)) return false;
        this.facing = side;

        if (this.prevFacing != facing) {
            if (isServerSide()) {
                needsUpdate = true;
                sendUpdateToClient();
            } else {
                rerender();
            }
        }

        boolean flag = prevFacing != facing;
        this.prevFacing = facing;
        return flag;
    }

    @Override
    public boolean canDrain(EnumFacing paramForgeDirection, Fluid paramFluid) {
        return false;
    }

    @Override
    public boolean canFill(EnumFacing paramForgeDirection, Fluid paramFluid) {
        return false;
    }
}