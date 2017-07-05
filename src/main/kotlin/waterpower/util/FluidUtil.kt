/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandlerItem

private val registeredFluids = FluidRegistry.getRegisteredFluids().values

fun isFluidContainer(stack: ItemStack): Boolean {
    return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
}

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

fun isFillableFluidContainer(stack: ItemStack): Boolean {
    return isFillableFluidContainer(stack, null)
}

fun isFillableFluidContainer(stack: ItemStack, testFluids: Iterable<Fluid>?): Boolean {
    if (!isFluidContainer(stack)) {
        return false
    } else {
        if (testFluids == null) {
            val testFluids1 = registeredFluids
        }

        val singleStack = stack.copyWithNewCount(1)
        val handler = singleStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
        if (handler == null) {
            return false
        } else {
            var fs = handler.drain(2147483647, false)
            if (fs != null && testFillFluid(handler, fs.fluid, fs.tag)) {
                return true
            } else {
                val var5 = handler.tankProperties
                val var6 = var5.size

                for (var7 in 0..var6 - 1) {
                    val properties = var5[var7]
                    fs = properties.contents
                    if (fs != null && testFillFluid(handler, fs.fluid, fs.tag)) {
                        return true
                    }
                }

                val var10 = registeredFluids.iterator()

                var fluid: Fluid
                do {
                    if (!var10.hasNext()) {
                        return false
                    }

                    fluid = var10.next() as Fluid
                } while (!testFillFluid(handler, fluid, null))

                return true
            }
        }
    }
}

private fun testFillFluid(handler: IFluidHandlerItem, fluid: Fluid, nbt: NBTTagCompound?): Boolean {
    val fs = FluidStack(fluid, Int.MAX_VALUE)
    fs.tag = nbt
    return handler.fill(fs, false) > 0
}


fun drainContainer(stack: ItemStack, fluid: Fluid?, maxAmount: Int, outputMode: FluidContainerOutputMode): FluidOperationResult? {
    if (!isStackEmpty(stack) && maxAmount > 0) {
        var inPlace = stack.copy()
        var extra = emptyStack
        if (!inPlace.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            return null
        } else {
            val singleStack = inPlace.copyWithNewCount(1)
            val handler = singleStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
            if (handler == null) {
                return null
            } else {
                val fs: FluidStack?
                if (fluid == null) {
                    fs = handler.drain(maxAmount, true)
                } else {
                    fs = handler.drain(FluidStack(fluid, maxAmount), true)
                }

                if (fs != null && fs.amount > 0) {
                    if (isStackEmpty(singleStack)) {
                        inPlace = shrink(inPlace)
                    } else {
                        val leftOver = handler.drain(Int.MAX_VALUE, false)
                        val isEmpty = leftOver == null || leftOver.amount <= 0
                        if ((!isEmpty || !outputMode.isOutputEmptyFull) && outputMode != FluidContainerOutputMode.AnyToOutput && (outputMode != FluidContainerOutputMode.InPlacePreferred || getCount(inPlace) <= 1)) {
                            if (getCount(inPlace) > 1) {
                                return null
                            }

                            inPlace = handler.container
                        } else {
                            extra = handler.container
                            inPlace = shrink(inPlace)
                        }
                    }

                    return FluidOperationResult(fs, inPlace, extra)
                } else {
                    return null
                }
            }
        }
    } else {
        return null
    }
}

fun fillContainer(stack: ItemStack, fsIn: FluidStack?, outputMode: FluidContainerOutputMode): FluidOperationResult? {
    if (!isStackEmpty(stack) && fsIn != null && fsIn.amount > 0) {
        var inPlace = stack.copy()
        var extra = emptyStack
        if (inPlace.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            var singleStack = inPlace.copyWithNewCount(1)
            val handler = singleStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
            if (handler == null) {
                return null
            } else {
                val fsChange = fsIn.copy()
                val amount = handler.fill(fsChange, true)
                if (amount <= 0) {
                    return null
                } else {
                    fsChange.amount = amount
                    val fillTestFs = fsIn.copy()
                    fillTestFs.amount = 2147483647
                    val isFull = handler.fill(fillTestFs, false) <= 0
                    singleStack = handler.container

                    if (isFull && outputMode.isOutputEmptyFull || outputMode == FluidContainerOutputMode.AnyToOutput || outputMode == FluidContainerOutputMode.InPlacePreferred && getCount(inPlace) > 1) {
                        extra = singleStack
                        inPlace = shrink(inPlace)
                    } else {
                        if (getCount(inPlace) > 1) {
                            return null
                        }

                        inPlace = singleStack
                    }

                    return FluidOperationResult(fsChange, inPlace, extra)
                }
            }
        } else {
            return null
        }
    } else {
        return null
    }
}

data class FluidOperationResult(val fluidChange: FluidStack, val inPlaceOutput: ItemStack, val extraOutput: ItemStack)

enum class FluidContainerOutputMode(val isOutputEmptyFull: Boolean) {
    EmptyFullToOutput(true),
    AnyToOutput(true),
    InPlacePreferred(false),
    InPlace(false);
}