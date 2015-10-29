package org.jackhuang.watercraft.common.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
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

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        this.fluidTank.readFromNBT(nbttagcompound.getCompoundTag("fluidTank"));
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        NBTTagCompound fluidTankTag = new NBTTagCompound();
        this.fluidTank.writeToNBT(fluidTankTag);
        nbttagcompound.setTag("fluidTank", fluidTankTag);
    }

    public int getFluidID() {
        if (getFluidTank() == null || getFluidTank().getFluid() == null || getFluidTank().getFluid().getFluid() == null)
            return -1;
        return getFluidTank().getFluid().getFluid().getID();
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

    public int getTankFluidId() {
        if (getFluidStackfromTank() == null)
            return -1;
        return getFluidStackfromTank().getFluid().getID();
    }

    public void setTankAmount(int amount, int fluidid) {
        getFluidTank().setFluid(new FluidStack(FluidRegistry.getFluid(fluidid), amount));
    }

    public boolean needsFluid() {
        return getFluidAmount() <= getFluidTank().getCapacity();
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (canFill(from, resource.getFluid())) {
            return getFluidTank().fill(resource, doFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if ((resource == null) || (!resource.isFluidEqual(getFluidTank().getFluid()))) {
            return null;
        }

        if (!canDrain(from, resource.getFluid()))
            return null;

        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return getFluidTank().drain(maxDrain, doDrain);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { getFluidTank().getInfo() };
    }

    @Override
    public abstract boolean canFill(ForgeDirection paramForgeDirection, Fluid paramFluid);

    @Override
    public abstract boolean canDrain(ForgeDirection paramForgeDirection, Fluid paramFluid);

    public void pushFluidToConsumers(int flowCapacity) {
        int amount = flowCapacity;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            FluidStack fluidStack = getFluidTank().drain(amount, false);
            if (fluidStack != null && fluidStack.amount > 0) {
                TileEntity te = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
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