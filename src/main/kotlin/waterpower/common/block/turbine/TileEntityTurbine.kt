/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.turbine

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidRegistry
import waterpower.annotations.HasGui
import waterpower.annotations.Register
import waterpower.common.block.container.ContainerRotor
import waterpower.common.block.reservoir.TileEntityReservoir
import waterpower.common.block.tile.TileEntityRotorGenerator
import waterpower.common.init.WPBlocks

@HasGui(guiClass = GuiTurbine::class, containerClass = ContainerRotor::class)
open class TileEntityTurbine(val type: Turbines) : TileEntityRotorGenerator(type.getOutput(), 10000000.0, type.getTier()) {

    override fun getName() = type.getLocalizedName()

    override fun getBlockState() = super.getBlockState().withProperty(WPBlocks.turbine.TYPES, type)!!

    private fun getWater(world: World, pos: BlockPos): TileEntityReservoir? {
        var reservoir: TileEntityReservoir? = null
        val newPos = pos.offset(getFacing().opposite)
        if (newPos.y != pos.y)
            return null
        val te = world.getTileEntity(newPos)
        if (te is TileEntityReservoir)
            reservoir = te
        return reservoir
    }

    override fun computeOutput(world: World, pos: BlockPos): Double {
        val pair = getWater(world, pos)
        if (pair?.getFluidTank()?.fluid?.fluid == null)
            return 0.0
        else {
            val fluidStack = pair.getFluidTank().fluid!!
            val use = minOf(pair.getFluidTank().fluidAmount, pair.type.maxExtract)
            var multiple = 0
            if (fluidStack.fluid == FluidRegistry.WATER)
                multiple = 1
            else if (FluidRegistry.isFluidRegistered("steam") && fluidStack.fluid == FluidRegistry.getFluid("steam") || FluidRegistry.isFluidRegistered("ic2steam") && fluidStack.fluid == FluidRegistry.getFluid("ic2steam"))
                multiple = 5
            if (multiple == 0)
                return 0.0
            val baseEnergy = 1.0 * use / 2048 * type.getOutput() * multiple
            val per = getEfficiency()
            if (per > 0) {
                val energy = baseEnergy * per
                if (energy > 0) {
                    pair.useLiquid(use)
                    damageRotor(1)
                }
                return energy
            }
        }
        return 0.0
    }
}

@Register("waterpower.turbine.mk1")
class TileEntityTurbineMK1 : TileEntityTurbine(Turbines.MK1)

@Register("waterpower.turbine.mk2")
class TileEntityTurbineMK2 : TileEntityTurbine(Turbines.MK2)

@Register("waterpower.turbine.mk3")
class TileEntityTurbineMK3 : TileEntityTurbine(Turbines.MK3)

@Register("waterpower.turbine.mk4")
class TileEntityTurbineMK4 : TileEntityTurbine(Turbines.MK4)

@Register("waterpower.turbine.mk5")
class TileEntityTurbineMK5 : TileEntityTurbine(Turbines.MK5)

@Register("waterpower.turbine.mk6")
class TileEntityTurbineMK6 : TileEntityTurbine(Turbines.MK6)

@Register("waterpower.turbine.mk7")
class TileEntityTurbineMK7 : TileEntityTurbine(Turbines.MK7)

@Register("waterpower.turbine.mk8")
class TileEntityTurbineMK8 : TileEntityTurbine(Turbines.MK8)

@Register("waterpower.turbine.mk9")
class TileEntityTurbineMK9 : TileEntityTurbine(Turbines.MK9)

@Register("waterpower.turbine.mk10")
class TileEntityTurbineMK10 : TileEntityTurbine(Turbines.MK10)

@Register("waterpower.turbine.mk11")
class TileEntityTurbineMK11 : TileEntityTurbine(Turbines.MK11)

@Register("waterpower.turbine.mk12")
class TileEntityTurbineMK12 : TileEntityTurbine(Turbines.MK12)