/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.attachment

import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import waterpower.common.Energy
import waterpower.common.block.tile.TileEntityBase

class AttachmentWater(te: TileEntityBase, val energyStorage: EnergyStorage, val fluidTank: FluidTank)
    : TileEntityAttachment(te) {
    override fun getName() = "water"
    override fun onTick() {
        if (fluidTank.fluid?.fluid != FluidRegistry.WATER)
            fluidTank.fluid = null
        val remain = fluidTank.fill(FluidStack(FluidRegistry.WATER, Energy.EU2Water(energyStorage.getMaxEnergyExtracted()).toInt()), false)
        val output = energyStorage.extractEnergy(Energy.Water2EU(remain.toDouble()), true)
        fluidTank.fill(FluidStack(FluidRegistry.WATER, Energy.EU2Water(output).toInt()), true)
    }
}