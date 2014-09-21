package org.jackhuang.watercraft.integration.waila;

import mcp.mobius.waila.api.impl.ModuleRegistrar;

import org.jackhuang.watercraft.common.block.reservoir.HUDHandlerReservoir;
import org.jackhuang.watercraft.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.watercraft.common.block.watermills.HUDHandlerWatermills;
import org.jackhuang.watercraft.common.block.watermills.TileEntityWatermill;
import org.jackhuang.watercraft.util.mods.Mods;

public class WailaModule {
	
	public static void init() {
		if(!Mods.Waila.isAvailable) return;

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerWatermills(), TileEntityWatermill.class);
		ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerWatermills(), TileEntityWatermill.class);
		ModuleRegistrar.instance().registerSyncedNBTKey("*", TileEntityWatermill.class);

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerReservoir(), TileEntityReservoir.class);
		ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerReservoir(), TileEntityReservoir.class);
		ModuleRegistrar.instance().registerSyncedNBTKey("*", TileEntityReservoir.class);
		
	}

}
