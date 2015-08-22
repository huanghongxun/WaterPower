/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.integration.waila;

import mcp.mobius.waila.addons.ExternalModulesHandler;

import org.jackhuang.watercraft.common.block.machines.*;
import org.jackhuang.watercraft.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.watercraft.common.block.turbines.TileEntityTurbine;
import org.jackhuang.watercraft.common.block.watermills.TileEntityWatermill;
import org.jackhuang.watercraft.integration.BaseModule;
import org.jackhuang.watercraft.util.Mods;

public class WailaModule extends BaseModule {

    public void register() {
        ExternalModulesHandler.instance().registerHeadProvider(
                new HUDHandlerWatermills(), TileEntityWatermill.class);
        ExternalModulesHandler.instance().registerBodyProvider(
                new HUDHandlerWatermills(), TileEntityWatermill.class);

        ExternalModulesHandler.instance().registerHeadProvider(
                new HUDHandlerTurbine(), TileEntityTurbine.class);
        ExternalModulesHandler.instance().registerBodyProvider(
                new HUDHandlerTurbine(), TileEntityTurbine.class);

        ExternalModulesHandler.instance().registerHeadProvider(
                new HUDHandlerReservoir(), TileEntityReservoir.class);
        ExternalModulesHandler.instance().registerBodyProvider(
                new HUDHandlerReservoir(), TileEntityReservoir.class);

        ExternalModulesHandler.instance().registerHeadProvider(
                new HUDHandlerMachine(), TileEntityMacerator.class);
        ExternalModulesHandler.instance().registerBodyProvider(
                new HUDHandlerMachine(), TileEntityMacerator.class);

        ExternalModulesHandler.instance().registerHeadProvider(
                new HUDHandlerMachine(), TileEntityAdvancedCompressor.class);
        ExternalModulesHandler.instance().registerBodyProvider(
                new HUDHandlerMachine(), TileEntityAdvancedCompressor.class);

        ExternalModulesHandler.instance().registerHeadProvider(
                new HUDHandlerMachine(), TileEntityCompressor.class);
        ExternalModulesHandler.instance().registerBodyProvider(
                new HUDHandlerMachine(), TileEntityCompressor.class);

        ExternalModulesHandler.instance().registerHeadProvider(
                new HUDHandlerMachine(), TileEntityCutter.class);
        ExternalModulesHandler.instance().registerBodyProvider(
                new HUDHandlerMachine(), TileEntityCutter.class);

        ExternalModulesHandler.instance().registerHeadProvider(
                new HUDHandlerMachine(), TileEntityLathe.class);
        ExternalModulesHandler.instance().registerBodyProvider(
                new HUDHandlerMachine(), TileEntityLathe.class);

        ExternalModulesHandler.instance().registerHeadProvider(
                new HUDHandlerMachine(), TileEntitySawmill.class);
        ExternalModulesHandler.instance().registerBodyProvider(
                new HUDHandlerMachine(), TileEntitySawmill.class);

        ExternalModulesHandler.instance().registerHeadProvider(
                new HUDHandlerMachine(), TileEntityCentrifuge.class);
        ExternalModulesHandler.instance().registerBodyProvider(
                new HUDHandlerMachine(), TileEntityCentrifuge.class);
    }

    @Override
    public void init() {
        try {
            register();
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

}
