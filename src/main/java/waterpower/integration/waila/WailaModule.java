/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.integration.waila;

import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraftforge.fml.common.Optional.Method;
import waterpower.common.block.machines.TileEntityAdvancedCompressor;
import waterpower.common.block.machines.TileEntityCentrifuge;
import waterpower.common.block.machines.TileEntityCompressor;
import waterpower.common.block.machines.TileEntityCutter;
import waterpower.common.block.machines.TileEntityLathe;
import waterpower.common.block.machines.TileEntityMacerator;
import waterpower.common.block.machines.TileEntitySawmill;
import waterpower.common.block.reservoir.TileEntityReservoir;
import waterpower.common.block.turbines.TileEntityTurbine;
import waterpower.common.block.watermills.TileEntityWatermill;
import waterpower.integration.BaseModule;
import waterpower.util.Mods;

public class WailaModule extends BaseModule {

    @Method(modid = Mods.IDs.Waila)
    public void register() {
        ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerWatermills(), TileEntityWatermill.class);
        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerWatermills(), TileEntityWatermill.class);

        ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerTurbine(), TileEntityTurbine.class);
        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerTurbine(), TileEntityTurbine.class);

        ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerReservoir(), TileEntityReservoir.class);
        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerReservoir(), TileEntityReservoir.class);

        ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntityMacerator.class);
        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntityMacerator.class);

        ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntityAdvancedCompressor.class);
        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntityAdvancedCompressor.class);

        ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntityCompressor.class);
        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntityCompressor.class);

        ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntityCutter.class);
        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntityCutter.class);

        ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntityLathe.class);
        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntityLathe.class);

        ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntitySawmill.class);
        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntitySawmill.class);

        ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntityCentrifuge.class);
        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntityCentrifuge.class);
    }

    @Override
    public void init() {
        try {
            register();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
