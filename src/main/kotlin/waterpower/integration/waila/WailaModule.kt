/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.waila

import mcp.mobius.waila.api.impl.ModuleRegistrar
import waterpower.annotations.Integration
import waterpower.common.block.machine.*
import waterpower.common.block.reservoir.TileEntityReservoir
import waterpower.common.block.turbine.TileEntityTurbine
import waterpower.common.block.watermill.TileEntityWatermill
import waterpower.integration.IDs
import waterpower.integration.IModule

@Integration(IDs.Waila)
object WailaModule : IModule() {

    override fun onInit() {
        super.onInit()

        ModuleRegistrar.instance().registerHeadProvider(HUDHandlerRotorGenerator, TileEntityWatermill::class.java)
        ModuleRegistrar.instance().registerBodyProvider(HUDHandlerRotorGenerator, TileEntityWatermill::class.java)

        ModuleRegistrar.instance().registerHeadProvider(HUDHandlerRotorGenerator, TileEntityTurbine::class.java)
        ModuleRegistrar.instance().registerBodyProvider(HUDHandlerRotorGenerator, TileEntityTurbine::class.java)

        ModuleRegistrar.instance().registerHeadProvider(HUDHandlerReservoir, TileEntityReservoir::class.java)
        ModuleRegistrar.instance().registerBodyProvider(HUDHandlerReservoir, TileEntityReservoir::class.java)

        ModuleRegistrar.instance().registerHeadProvider(HUDHandlerMachine, TileEntityCrusher::class.java)
        ModuleRegistrar.instance().registerBodyProvider(HUDHandlerMachine, TileEntityCrusher::class.java)

        ModuleRegistrar.instance().registerHeadProvider(HUDHandlerMachine, TileEntityAdvCompressor::class.java)
        ModuleRegistrar.instance().registerBodyProvider(HUDHandlerMachine, TileEntityAdvCompressor::class.java)

        ModuleRegistrar.instance().registerHeadProvider(HUDHandlerMachine, TileEntityCompressor::class.java)
        ModuleRegistrar.instance().registerBodyProvider(HUDHandlerMachine, TileEntityCompressor::class.java)

        ModuleRegistrar.instance().registerHeadProvider(HUDHandlerMachine, TileEntityCutter::class.java)
        ModuleRegistrar.instance().registerBodyProvider(HUDHandlerMachine, TileEntityCutter::class.java)

        ModuleRegistrar.instance().registerHeadProvider(HUDHandlerMachine, TileEntityLathe::class.java)
        ModuleRegistrar.instance().registerBodyProvider(HUDHandlerMachine, TileEntityLathe::class.java)

        ModuleRegistrar.instance().registerHeadProvider(HUDHandlerMachine, TileEntitySawmill::class.java)
        ModuleRegistrar.instance().registerBodyProvider(HUDHandlerMachine, TileEntitySawmill::class.java)

        ModuleRegistrar.instance().registerHeadProvider(HUDHandlerMachine, TileEntityCentrifuge::class.java)
        ModuleRegistrar.instance().registerBodyProvider(HUDHandlerMachine, TileEntityCentrifuge::class.java)

    }
}