/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.util

import ic2.core.util.StackUtil
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fluids.*
import net.minecraftforge.fluids.capability.CapabilityFluidHandler

private val registeredFluids = FluidRegistry.getRegisteredFluids().values

fun pushFluidAround(world: IBlockAccess, pos: BlockPos, tank: FluidTank) {
    val potential = tank.drain(tank.fluidAmount, false)
    var drained = 0
    if (potential != null && potential.amount > 0) {
        val working = potential.copy()

        for (side in EnumFacing.VALUES) {
            if (potential.amount <= 0)
                break

            val target = world.getTileEntity(pos.offset(side))
            if (target != null) {
                val handler = target.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.opposite)
                if (handler != null) {
                    val used = handler.fill(potential, true)
                    if (used > 0) {
                        drained += used
                        potential.amount -= used
                    }
                }
            }
        }

        if (drained > 0) {
            val actuallyDrained = tank.drain(drained, true)
            if (actuallyDrained == null || actuallyDrained.amount != drained) {
                throw IllegalStateException("Bad tank! Could drain " + working + " but only drained " + actuallyDrained + "( tank " + tank.javaClass + ")")
            }
        }

    }
}

fun drainContainer(stack: ItemStack?, fluid: Fluid?, maxAmount: Int, outputMode: FluidContainerOutputMode): FluidOperationResult? {
    if (!isStackEmpty(stack) && maxAmount > 0) {
        var inPlace: ItemStack? = stack!!.copy()
        var extra: ItemStack? = null
        var fs: FluidStack?
        if (!FluidContainerRegistry.isFilledContainer(inPlace)) {
            if (inPlace!!.item is IFluidContainerItem) {
                val container = inPlace.item as IFluidContainerItem
                fs = container.getFluid(inPlace)
                if (fs == null) {
                    return null
                }

                if (fluid != null && fs.fluid !== fluid) {
                    return null
                }

                val singleStack = inPlace.copyWithNewCount(1)
                fs = container.drain(singleStack, maxAmount, true)
                if (fs == null || fs.amount <= 0) {
                    return null
                }

                if (isStackEmpty(singleStack)) {
                    inPlace = shrink(inPlace)
                } else {
                    val isEmpty = container.getFluid(singleStack) == null
                    if (isEmpty && outputMode.isOutputEmptyFull || outputMode == FluidContainerOutputMode.AnyToOutput || outputMode == FluidContainerOutputMode.InPlacePreferred && getCount(inPlace) > 1) {
                        extra = singleStack
                        inPlace = shrink(inPlace)
                    } else {
                        if (StackUtil.getSize(inPlace) > 1) {
                            return null
                        }

                        inPlace = singleStack
                    }
                }
            } else {
                if (!inPlace.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, (null as EnumFacing?)!!)) {
                    return null
                }

                val singleStack = inPlace.copyWithNewCount(1)
                val handler = singleStack.getCapability<net.minecraftforge.fluids.capability.IFluidHandler?>(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, (null as EnumFacing?)!!) ?: return null

                if (fluid == null) {
                    fs = handler.drain(maxAmount, true)
                } else {
                    fs = handler.drain(FluidStack(fluid, maxAmount), true)
                }

                if (fs == null || fs.amount <= 0) {
                    return null
                }

                if (StackUtil.isEmpty(singleStack)) {
                    inPlace = shrink(inPlace)
                } else {
                    val leftOver = handler.drain(2147483647, false)
                    val isEmpty = leftOver == null || leftOver.amount <= 0
                    if ((!isEmpty || !outputMode.isOutputEmptyFull) && outputMode != FluidContainerOutputMode.AnyToOutput && (outputMode != FluidContainerOutputMode.InPlacePreferred || getCount(inPlace) <= 1)) {
                        if (StackUtil.getSize(inPlace) > 1) {
                            return null
                        }

                        inPlace = singleStack
                    } else {
                        extra = singleStack
                        inPlace = shrink(inPlace)
                    }
                }
            }
        } else {
            fs = FluidContainerRegistry.getFluidForFilledItem(inPlace)
            if (fs != null && (fluid == null || fluid === fs.fluid)) {
                if (fs.amount in 1..maxAmount) {
                    if (!inPlace!!.item.hasContainerItem(inPlace)) {
                        inPlace = StackUtil.decSize(inPlace)
                    } else if (outputMode.isOutputEmptyFull || outputMode == FluidContainerOutputMode.InPlacePreferred && StackUtil.getSize(inPlace) > 1) {
                        val singleStack = inPlace.item.getContainerItem(inPlace)
                        if (singleStack != null) {
                            extra = singleStack.copy()
                        }

                        inPlace = StackUtil.decSize(inPlace)
                    } else {
                        if (StackUtil.getSize(inPlace) > 1) {
                            return null
                        }

                        val singleStack = inPlace.item.getContainerItem(inPlace)
                        inPlace = if (singleStack != null) StackUtil.copy(singleStack) else null
                    }
                    return FluidOperationResult(fs, inPlace, extra)
                }

                return null
            }

            return null
        }

        assert(fs!!.amount > 0)

        return FluidOperationResult(fs, inPlace, extra)
    } else {
        return null
    }
}

fun fillContainer(stack: ItemStack, fsIn: FluidStack?, outputMode: FluidContainerOutputMode): FluidOperationResult? {
    if (!StackUtil.isEmpty(stack) && fsIn != null && fsIn.amount > 0) {
        var inPlace = StackUtil.copy(stack)
        var extra: ItemStack? = null
        val fsChange: FluidStack
        val singleStack: ItemStack?
        val isFull: Boolean
        if (FluidContainerRegistry.isEmptyContainer(inPlace)) {
            singleStack = FluidContainerRegistry.fillFluidContainer(fsIn, inPlace)
            if (singleStack == null) {
                return null
            }

            fsChange = FluidContainerRegistry.getFluidForFilledItem(singleStack)
            isFull = true
        } else {
            val amount: Int
            if (inPlace.item is IFluidContainerItem) {
                val container = inPlace.item as IFluidContainerItem
                singleStack = StackUtil.copyWithSize(inPlace, 1)
                fsChange = fsIn.copy()
                amount = container.fill(singleStack, fsChange, true)
                if (amount <= 0) {
                    return null
                }

                fsChange.amount = amount
                isFull = container.getFluid(singleStack).amount == container.getCapacity(singleStack)
            } else {
                if (!inPlace.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, (null as EnumFacing?)!!)) {
                    return null
                }

                singleStack = StackUtil.copyWithSize(inPlace, 1)
                val handler = singleStack!!.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, (null as EnumFacing?)!!) as net.minecraftforge.fluids.capability.IFluidHandler ?: return null

                fsChange = fsIn.copy()
                amount = handler.fill(fsChange, true)
                if (amount <= 0) {
                    return null
                }

                fsChange.amount = amount
                val fillTestFs = fsIn.copy()
                fillTestFs.amount = 2147483647
                isFull = handler.fill(fillTestFs, false) <= 0
            }
        }

        assert(fsChange.fluid === fsIn.fluid)

        assert(fsChange.amount > 0)

        assert(StackUtil.getSize(singleStack) == 1)

        if (isFull && outputMode.isOutputEmptyFull || outputMode == FluidContainerOutputMode.AnyToOutput || outputMode == FluidContainerOutputMode.InPlacePreferred && StackUtil.getSize(inPlace) > 1) {
            extra = singleStack
            inPlace = StackUtil.decSize(inPlace)
        } else {
            if (StackUtil.getSize(inPlace) > 1) {
                return null
            }

            inPlace = singleStack
        }

        return FluidOperationResult(fsChange, inPlace, extra)
    } else {
        return null
    }
}

data class FluidOperationResult(val fluidChange: FluidStack, val inPlaceOutput: ItemStack?, val extraOutput: ItemStack?)

enum class FluidContainerOutputMode(val isOutputEmptyFull: Boolean) {
    EmptyFullToOutput(true),
    AnyToOutput(true),
    InPlacePreferred(false),
    InPlace(false);
}