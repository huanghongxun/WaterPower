package org.jackhuang.watercraft.integration;

import mcp.mobius.waila.api.impl.ModuleRegistrar;

import org.jackhuang.watercraft.common.block.machines.*;
import org.jackhuang.watercraft.common.block.reservoir.HUDHandlerReservoir;
import org.jackhuang.watercraft.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.watercraft.common.block.watermills.HUDHandlerWatermills;
import org.jackhuang.watercraft.common.block.watermills.TileEntityWatermill;
import org.jackhuang.watercraft.util.Mods;

public class WailaModule {
	
	public static void init() {
		if(!Mods.Waila.isAvailable) return;

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerWatermills(), TileEntityWatermill.class);
		ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerWatermills(), TileEntityWatermill.class);
		ModuleRegistrar.instance().registerSyncedNBTKey("*", TileEntityWatermill.class);

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerReservoir(), TileEntityReservoir.class);
		ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerReservoir(), TileEntityReservoir.class);
		ModuleRegistrar.instance().registerSyncedNBTKey("*", TileEntityReservoir.class);

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntityMacerator.class);
		ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntityMacerator.class);
		ModuleRegistrar.instance().registerSyncedNBTKey("*", TileEntityMacerator.class);

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntityAdvancedCompressor.class);
		ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntityAdvancedCompressor.class);
		ModuleRegistrar.instance().registerSyncedNBTKey("*", TileEntityAdvancedCompressor.class);

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntityCompressor.class);
		ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntityCompressor.class);
		ModuleRegistrar.instance().registerSyncedNBTKey("*", TileEntityCompressor.class);

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntityCutter.class);
		ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntityCutter.class);
		ModuleRegistrar.instance().registerSyncedNBTKey("*", TileEntityCutter.class);

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntityLathe.class);
		ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntityLathe.class);
		ModuleRegistrar.instance().registerSyncedNBTKey("*", TileEntityLathe.class);

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntitySawmill.class);
		ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntitySawmill.class);
		ModuleRegistrar.instance().registerSyncedNBTKey("*", TileEntitySawmill.class);

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerMachine(), TileEntityCentrifuge.class);
		ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerMachine(), TileEntityCentrifuge.class);
		ModuleRegistrar.instance().registerSyncedNBTKey("*", TileEntityCentrifuge.class);
		
	}

}
