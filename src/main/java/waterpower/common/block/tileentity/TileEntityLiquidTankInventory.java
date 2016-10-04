package waterpower.common.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class TileEntityLiquidTankInventory extends TileEntityInventory implements IFluidHandler {
    protected FluidTank fluidTank;

    public TileEntityLiquidTankInventory(int tanksize) {
        this.fluidTank = new FluidTank(1000 * tanksize);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        this.fluidTank.readFromNBT(nbttagcompound.getCompoundTag("fluidTank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        NBTTagCompound fluidTankTag = new NBTTagCompound();
        this.fluidTank.writeToNBT(fluidTankTag);
        nbttagcompound.setTag("fluidTank", fluidTankTag);
        
        return nbttagcompound;
    }

    public String getFluidName() {
        if (getFluidTank() == null || getFluidTank().getFluid() == null || getFluidTank().getFluid().getFluid() == null)
            return null;
        return getFluidTank().getFluid().getFluid().getName();
    }

    public FluidTank getFluidTank() {
        return this.fluidTank;
    }

    public void setFluidTank(FluidTank fluidTank) {
        this.fluidTank = fluidTank;
    }

    public void setFluidTankCapacity(int capacity) {
        getFluidTank().setCapacity(capacity);
    }

    public int getFluidTankCapacity() {
        if (getFluidTank() == null)
            return -1;
        return getFluidTank().getCapacity();
    }

    public FluidStack getFluidStackfromTank() {
        if (getFluidTank() == null)
            return null;
        return getFluidTank().getFluid();
    }

    public Fluid getFluidfromTank() {
        if (getFluidStackfromTank() == null)
            return null;
        return getFluidStackfromTank().getFluid();
    }

    public int getFluidAmount() {
        if (getFluidTank() == null)
            return -1;
        return getFluidTank().getFluidAmount();
    }

    public void setTankAmount(int amount, int fluidid) {
        getFluidTank().setFluid(new FluidStack(FluidRegistry.getFluid(fluidid), amount));
    }

    public boolean needsFluid() {
        return getFluidAmount() <= getFluidTank().getCapacity();
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (canFill(from, resource.getFluid())) {
            return getFluidTank().fill(resource, doFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if ((resource == null) || (!resource.isFluidEqual(getFluidTank().getFluid()))) {
            return null;
        }

        if (!canDrain(from, resource.getFluid()))
            return null;

        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return getFluidTank().drain(maxDrain, doDrain);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[] { getFluidTank().getInfo() };
    }

    @Override
    public abstract boolean canFill(EnumFacing paramForgeDirection, Fluid paramFluid);

    @Override
    public abstract boolean canDrain(EnumFacing paramForgeDirection, Fluid paramFluid);

    public void pushFluidToConsumers(int flowCapacity) {
        int amount = flowCapacity;
        for (EnumFacing side : EnumFacing.values()) {
            FluidStack fluidStack = getFluidTank().drain(amount, false);
            if (fluidStack != null && fluidStack.amount > 0) {
                TileEntity te = worldObj.getTileEntity(getPos().add(side.getDirectionVec()));
                if (te != null && te instanceof IFluidHandler) {
                    int used = ((IFluidHandler) te).fill(side.getOpposite(), fluidStack, true);
                    if (used > 0) {
                        amount -= used;
                        getFluidTank().drain(used, true);
                        if (amount <= 0)
                            return;
                    }
                }
            }
        }
    }

    protected boolean allowedSendPacketTank() {
        return true;
    }

    int preTankCapacity = -999;

    @Override
    public void readPacketData(NBTTagCompound tag) {
        super.readPacketData(tag);

        if (allowedSendPacketTank()) {
            setFluidTankCapacity(tag.getInteger("tankCapacity"));
            getFluidTank().readFromNBT(tag.getCompoundTag("tank"));
        }
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);

        if (allowedSendPacketTank()) {
            NBTTagCompound n = new NBTTagCompound();
            getFluidTank().writeToNBT(n);
            tag.setInteger("tankCapacity", getFluidTankCapacity());
            tag.setTag("tank", n);
        }
    }
}