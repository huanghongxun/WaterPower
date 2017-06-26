/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.util

import ic2.api.util.FluidContainerOutputMode
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandlerItem

private val registeredFluids = FluidRegistry.getRegisteredFluids().values

fun isFluidContainer(stack: ItemStack): Boolean {
    return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
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
    if (!stack.isEmpty && maxAmount > 0) {
        var inPlace = stack.copy()
        var extra = ItemStack.EMPTY
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
                    if (singleStack.isEmpty) {
                        inPlace.shrink(1)
                    } else {
                        val leftOver = handler.drain(Int.MAX_VALUE, false)
                        val isEmpty = leftOver == null || leftOver.amount <= 0
                        if ((!isEmpty || !outputMode.isOutputEmptyFull) && outputMode != FluidContainerOutputMode.AnyToOutput && (outputMode != FluidContainerOutputMode.InPlacePreferred || inPlace.count <= 1)) {
                            if (inPlace.count > 1) {
                                return null
                            }

                            inPlace = handler.container
                        } else {
                            extra = handler.container
                            inPlace.shrink(1)
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
    if (!stack.isEmpty && fsIn != null && fsIn.amount > 0) {
        var inPlace = stack.copy()
        var extra = ItemStack.EMPTY
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

                    if (isFull && outputMode.isOutputEmptyFull || outputMode == FluidContainerOutputMode.AnyToOutput || outputMode == FluidContainerOutputMode.InPlacePreferred && inPlace.count > 1) {
                        extra = singleStack
                        inPlace.shrink(1)
                    } else {
                        if (inPlace.count > 1) {
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

data class FluidOperationResult
internal constructor(val fluidChange: FluidStack, val inPlaceOutput: ItemStack, val extraOutput: ItemStack)
