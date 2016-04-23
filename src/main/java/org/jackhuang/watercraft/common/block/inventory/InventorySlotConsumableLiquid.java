package org.jackhuang.watercraft.common.block.inventory;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidTank;

import org.apache.commons.lang3.mutable.MutableObject;
import org.jackhuang.watercraft.common.block.tileentity.TileEntityInventory;
import org.jackhuang.watercraft.util.StackUtil;

public class InventorySlotConsumableLiquid extends InventorySlotConsumable {
    private OpType opType;

    public InventorySlotConsumableLiquid(TileEntityInventory base, String name, int count) {
        this(base, name, InventorySlot.Access.I, count, InventorySlot.InvSide.TOP, OpType.Both);
    }

    public InventorySlotConsumableLiquid(TileEntityInventory base, String name, InventorySlot.Access access, int count, InventorySlot.InvSide preferredSide,
            OpType opType) {
        super(base, name, access, count, preferredSide);

        this.opType = opType;
    }

    public boolean accepts(ItemStack stack) {
        Item item = stack.getItem();
        if (item == null)
            return false;

        if ((this.opType == OpType.Drain) || (this.opType == OpType.Both)) {
            FluidStack containerFluid = null;

            if (FluidContainerRegistry.isFilledContainer(stack))
                containerFluid = FluidContainerRegistry.getFluidForFilledItem(stack);
            else if ((item instanceof IFluidContainerItem)) {
                containerFluid = ((IFluidContainerItem) item).getFluid(stack);
            }

            if ((containerFluid != null) && (containerFluid.amount > 0) && (acceptsLiquid(containerFluid.getFluid()))) {
                return true;
            }
        }
        IFluidContainerItem containerItem;
        if ((this.opType == OpType.Fill) || (this.opType == OpType.Both)) {
            if (FluidContainerRegistry.isEmptyContainer(stack)) {
                if (getPossibleFluids() == null) {
                    return true;
                }
                for (Fluid fluid : getPossibleFluids()) {
                    if (FluidContainerRegistry.fillFluidContainer(new FluidStack(fluid, 1), stack) != null) {
                        return true;
                    }
                }
            } else if (item instanceof IFluidContainerItem) {
                containerItem = (IFluidContainerItem) item;
                FluidStack prevFluid = containerItem.getFluid(stack);

                if ((prevFluid == null) || (containerItem.getCapacity(stack) > prevFluid.amount)) {
                    if (getPossibleFluids() == null) {
                        return true;
                    }
                    for (Fluid fluid : getPossibleFluids()) {
                        if (containerItem.fill(stack, new FluidStack(fluid, 1), false) > 0) {
                            return true;
                        }
                    }
                }
            }

        }

        return false;
    }

    public FluidStack drain(Fluid fluid, int maxAmount, MutableObject<ItemStack> output, boolean simulate) {
        output.setValue(null);

        if ((this.opType != OpType.Drain) && (this.opType != OpType.Both))
            return null;

        ItemStack stack = get();
        if (stack == null)
            return null;

        if (FluidContainerRegistry.isFilledContainer(stack)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(stack);
            if ((fluidStack == null) || ((fluid != null) && (fluid != fluidStack.getFluid())))
                return null;
            if (!acceptsLiquid(fluidStack.getFluid()))
                return null;
            if ((fluidStack.amount <= 0) || (fluidStack.amount > maxAmount))
                return null;

            if (stack.getItem().hasContainerItem()) {
                output.setValue(stack.getItem().getContainerItem(stack));
            }

            if (!simulate) {
                stack.stackSize -= 1;
                if (stack.stackSize <= 0)
                    put(null);
            }

            return fluidStack;
        }
        if ((stack.getItem() instanceof IFluidContainerItem)) {
            IFluidContainerItem container = (IFluidContainerItem) stack.getItem();
            if (container.getFluid(stack) == null)
                return null;

            if ((fluid != null) && (container.getFluid(stack).getFluid() != fluid))
                return null;
            if (!acceptsLiquid(container.getFluid(stack).getFluid()))
                return null;

            ItemStack singleStack = StackUtil.copyWithSize(stack, 1);

            FluidStack fluidStack = container.drain(singleStack, maxAmount, true);
            if ((fluidStack == null) || (fluidStack.amount <= 0))
                return null;

            if (singleStack.stackSize <= 0) {
                if (!simulate)
                    stack.stackSize -= 1;
            } else if (container.getFluid(singleStack) == null) {
                output.setValue(singleStack);

                if (!simulate)
                    stack.stackSize -= 1;
            } else {
                if (stack.stackSize > 1)
                    return null;

                if (!simulate)
                    put(singleStack);

            }

            if (stack.stackSize <= 0)
                put(null);

            return fluidStack;
        }
        return null;
    }

    public int fill(Fluid fluid, int maxAmount, MutableObject<ItemStack> output, boolean simulate) {
        if (fluid == null)
            throw new NullPointerException("fluid is null");

        output.setValue(null);

        if ((this.opType != OpType.Fill) && (this.opType != OpType.Both))
            return 0;

        ItemStack stack = get();
        if (stack == null)
            return 0;

        if (FluidContainerRegistry.isEmptyContainer(stack)) {
            ItemStack filled = FluidContainerRegistry.fillFluidContainer(new FluidStack(fluid, maxAmount), stack);
            if (filled == null)
                return 0;

            output.setValue(filled);

            if (!simulate) {
                stack.stackSize -= 1;
                if (stack.stackSize <= 0)
                    put(null);
            }

            return FluidContainerRegistry.getFluidForFilledItem(filled).amount;
        }
        if (stack.getItem() instanceof IFluidContainerItem) {
            IFluidContainerItem container = (IFluidContainerItem) stack.getItem();
            ItemStack singleStack = StackUtil.copyWithSize(stack, 1);

            int amount = container.fill(singleStack, new FluidStack(fluid, maxAmount), true);
            if (amount <= 0)
                return 0;

            assert (singleStack.stackSize == 1);

            if (container.getFluid(singleStack).amount == container.getCapacity(singleStack)) {
                output.setValue(singleStack);

                if (!simulate)
                    stack.stackSize -= 1;
            } else {
                if (stack.stackSize > 1)
                    return 0;

                if (!simulate)
                    put(singleStack);
            }

            if (stack.stackSize <= 0)
                put(null);

            return amount;
        }
        return 0;
    }

    public boolean transferToTank(IFluidTank tank, MutableObject<ItemStack> output, boolean simulate) {
        int space = tank.getCapacity();
        Fluid fluidRequired = null;

        FluidStack tankFluid = tank.getFluid();

        if (tankFluid != null) {
            space -= tankFluid.amount;
            fluidRequired = tankFluid.getFluid();
        }

        FluidStack fluid = drain(fluidRequired, space, output, true);
        if (fluid == null)
            return false;

        int amount = tank.fill(fluid, !simulate);
        if (amount <= 0)
            return false;

        if (!simulate)
            drain(fluidRequired, amount, output, false);

        return true;
    }

    public boolean transferFromTank(IFluidTank tank, MutableObject<ItemStack> output, boolean simulate) {
        FluidStack tankFluid = tank.drain(tank.getFluidAmount(), false);
        if ((tankFluid == null) || (tankFluid.amount <= 0))
            return false;

        int amount = fill(tankFluid.getFluid(), tankFluid.amount, output, simulate);
        if (amount <= 0)
            return false;

        if (!simulate)
            tank.drain(amount, true);

        return true;
    }

    public void setOpType(OpType opType) {
        this.opType = opType;
    }

    protected boolean acceptsLiquid(Fluid fluid) {
        return true;
    }

    protected Iterable<Fluid> getPossibleFluids() {
        return null;
    }

    public static enum OpType {
        Drain, Fill, Both, None;
    }
}