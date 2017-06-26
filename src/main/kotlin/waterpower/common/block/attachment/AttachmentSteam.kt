/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.attachment

import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import waterpower.common.Energy
import waterpower.common.block.tile.TileEntityBase

class AttachmentSteam(te: TileEntityBase, val energyStorage: EnergyStorage, val fluidTank: FluidTank)
    : TileEntityAttachment(te) {
    var steam: Fluid? = null
    override fun getName() = "steam"
    override fun onLoaded() {
        super.onLoaded()

        if (FluidRegistry.isFluidRegistered("steam"))
            steam = FluidRegistry.getFluid("steam")
        else if (FluidRegistry.isFluidRegistered("ic2steam"))
            steam = FluidRegistry.getFluid("ic2steam")
        else
            steam = null
    }

    override fun onTick() {
        if (steam == null) return
        if (fluidTank.fluid?.fluid != steam)
            fluidTank.fluid = null
        val remain = fluidTank.fill(FluidStack(steam, Energy.EU2Steam(energyStorage.getMaxEnergyExtracted()).toInt()), false)
        val output = energyStorage.extractEnergy(Energy.Steam2EU(remain.toDouble()), true)
        fluidTank.fill(FluidStack(steam, Energy.EU2Steam(output).toInt()), true)
    }
}