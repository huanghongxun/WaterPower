/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.inventory

import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.IFluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import org.apache.commons.lang3.mutable.MutableObject
import waterpower.common.block.tile.TileEntityInventory
import waterpower.util.*

class InventorySlotConsumableLiquid(base: TileEntityInventory, name: String, access: InventorySlot.Access, count: Int, preferredSide: InventorySlot.InvSide = InventorySlot.InvSide.TOP, private var opType: InventorySlotConsumableLiquid.OpType = OpType.Drain)
    : InventorySlotConsumable(base, name, access, count, preferredSide) {

    constructor(base: TileEntityInventory, name: String, count: Int) : this(base, name, InventorySlot.Access.I, count) {}

    override fun accepts(stack: ItemStack): Boolean {
        if (isStackEmpty(stack)) {
            return false
        }
        if (!isFluidContainer(stack)) {
            return false
        }
        if (this.opType == OpType.Drain || this.opType == OpType.Both) {
            var containerFluid: FluidStack? = null
            if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                val singleStack = stack.copyWithNewCount(1)
                val handler = singleStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
                if (handler != null) {
                    containerFluid = handler.drain(Integer.MAX_VALUE, false)
                }
            }
            if (containerFluid != null && containerFluid.amount > 0) {
                if (acceptsLiquid(containerFluid.fluid)) {
                    return true
                }
            }
        }
        if ((this.opType == OpType.Fill || this.opType == OpType.Both) && isFillableFluidContainer(stack, possibleFluids)) {
            return true
        }
        return false
    }

    fun drain(fluid: Fluid?, maxAmount: Int, output: MutableObject<ItemStack>, simulate: Boolean): FluidStack? {
        output.value = null
        if (fluid != null && !acceptsLiquid(fluid)) {
            return null
        }
        if (this.opType != OpType.Drain && this.opType != OpType.Both) {
            return null
        }
        val stack = get()
        if (isStackEmpty(stack)) {
            return null
        }
        val result = drainContainer(stack, fluid, maxAmount, FluidContainerOutputMode.EmptyFullToOutput) ?: return null
        if (fluid == null && !acceptsLiquid(result.fluidChange.fluid)) {
            return null
        }
        output.value = result.extraOutput
        if (!simulate) {
            put(result.inPlaceOutput)
        }
        return result.fluidChange
    }

    fun fill(fs: FluidStack?, output: MutableObject<ItemStack>, simulate: Boolean): Int {
        output.value = null
        if (fs == null || fs.amount <= 0) {
            return 0
        }
        if (this.opType != OpType.Fill && this.opType != OpType.Both) {
            return 0
        }
        val stack = get()
        if (isStackEmpty(stack)) {
            return 0
        }
        val result = fillContainer(stack, fs, FluidContainerOutputMode.EmptyFullToOutput) ?: return 0
        output.value = result.extraOutput
        if (!simulate) {
            put(result.inPlaceOutput)
        }
        return result.fluidChange.amount
    }

    fun transferToTank(tank: IFluidTank, output: MutableObject<ItemStack>, simulate: Boolean): Boolean {
        var space = tank.capacity
        var fluidRequired: Fluid? = null

        val tankFluid = tank.fluid
        if (tankFluid != null) {
            space -= tankFluid.amount
            fluidRequired = tankFluid.fluid
        }
        val fluid = drain(fluidRequired, space, output, true) ?: return false
        val amount = tank.fill(fluid, !simulate)
        if (amount <= 0) {
            return false
        }
        if (!simulate) {
            drain(fluidRequired, amount, output, false)
        }
        return true
    }

    fun transferFromTank(tank: IFluidTank, output: MutableObject<ItemStack>, simulate: Boolean): Boolean {
        val tankFluid = tank.drain(tank.fluidAmount, false)
        if (tankFluid == null || tankFluid.amount <= 0) {
            return false
        }
        val amount = fill(tankFluid, output, simulate)
        if (amount <= 0) {
            return false
        }
        if (!simulate) {
            tank.drain(amount, true)
        }
        return true
    }

    fun processIntoTank(tank: IFluidTank, outputSlot: InventorySlotOutput): Boolean {
        if (isEmpty()) {
            return false
        }
        val output = MutableObject<ItemStack>()
        var wasChange = false
        if (transferToTank(tank, output, true) && (isStackEmpty(output.value) || outputSlot.canAdd(output.value))) {
            wasChange = transferToTank(tank, output, false)
            if (!isStackEmpty(output.value)) {
                outputSlot.add(output.value)
            }
        }
        return wasChange
    }

    fun processFromTank(tank: IFluidTank, outputSlot: InventorySlotOutput): Boolean {
        if (isEmpty() || tank.fluidAmount <= 0) {
            return false
        }
        val output = MutableObject<ItemStack>()
        var wasChange = false
        if (transferFromTank(tank, output, true) && (isStackEmpty(output.value) || outputSlot.canAdd(output.value))) {
            wasChange = transferFromTank(tank, output, false)
            if (!isStackEmpty(output.value)) {
                outputSlot.add(output.value)
            }
        }
        return wasChange
    }

    fun setOpType(opType1: OpType) {
        this.opType = opType1
    }

    protected fun acceptsLiquid(fluid: Fluid): Boolean {
        return true
    }

    protected val possibleFluids: Iterable<Fluid>?
        get() = null

    enum class OpType private constructor() {
        Drain, Fill, Both, None
    }
}