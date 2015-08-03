package org.jackhuang.watercraft.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class TileEntityLiquidTankInventory extends TileEntityInventory
		implements IFluidHandler {
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
		return getFluidTank().getCapacity();
	}

	public FluidStack getFluidStackfromTank() {
		return getFluidTank().getFluid();
	}

	public Fluid getFluidfromTank() {
		return getFluidStackfromTank().getFluid();
	}

	public int getTankAmount() {
		return getFluidTank().getFluidAmount();
	}

	public int getTankFluidId() {
		return getFluidStackfromTank().getFluidID();
	}

	public int gaugeLiquidScaled(int i) {
		if (getFluidTank().getFluidAmount() <= 0)
			return 0;

		return getFluidTank().getFluidAmount() * i
				/ getFluidTank().getCapacity();
	}

	public void setTankAmount(int amount, int fluidid) {
		getFluidTank().setFluid(
				new FluidStack(FluidRegistry.getFluid(fluidid), amount));
	}

	public boolean needsFluid() {
		return getFluidTank().getFluidAmount() <= getFluidTank().getCapacity();
	}

	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (canFill(from, resource.getFluid())) {
			return getFluidTank().fill(resource, doFill);
		}
		return 0;
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		if ((resource == null)
				|| (!resource.isFluidEqual(getFluidTank().getFluid()))) {
			return null;
		}

		if (!canDrain(from, resource.getFluid()))
			return null;

		return drain(from, resource.amount, doDrain);
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return getFluidTank().drain(maxDrain, doDrain);
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { getFluidTank().getInfo() };
	}

	public abstract boolean canFill(ForgeDirection paramForgeDirection,
			Fluid paramFluid);

	public abstract boolean canDrain(ForgeDirection paramForgeDirection,
			Fluid paramFluid);
	
	public void pushFluidToConsumers(int flowCapacity) {
		int amount = flowCapacity;
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			FluidStack fluidStack = getFluidTank().drain(amount, false);
			if (fluidStack != null && fluidStack.amount > 0) {
				TileEntity te = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
				if(te != null && te instanceof IFluidHandler) {
					int used = ((IFluidHandler) te).fill(side.getOpposite(), fluidStack, true);
					if(used > 0) {
						amount -= used;
						getFluidTank().drain(used, true);
						if (amount <= 0) return;
					}
				}
			}
		}
	}
    
    @Override
    public void readPacketData(NBTTagCompound tag) {
        super.readPacketData(tag);
        
        getFluidTank().readFromNBT(tag.getCompoundTag("tank"));
    }
    
    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);
        
        NBTTagCompound n = new NBTTagCompound();
        getFluidTank().writeToNBT(n);
        tag.setTag("tank", n);
    }
}