package org.jackhuang.watercraft.common.block.reservoir;

import net.minecraft.util.StatCollector;

import org.apache.logging.log4j.Level;
import org.jackhuang.watercraft.common.block.reservoir.*;
import org.jackhuang.watercraft.util.WCLog;

import com.google.common.base.Throwables;

public enum ReservoirType {
	//1 - IndustrialCraft
	WOOD(1000, 100, 8),
	STONE(1500, 100, 16),
	LAPIS(2000, 200, 64),
	TIN(3000, 200, 64),
	COPPER(4000, 300, 64),
	LEAD(4000, 300, 64),
	QUARTZ(5000, 400, 128),
	BRONZE(7000, 400, 128),
	IRON(10000, 500, 256),
	NETHER(10000, 1000, 256),
	OBSIDIAN(12000, 600, 512),
	SILVER(14000, 700, 512),
	GOLD(18000, 1000, 512),
	CARBON(20000, 2000, 2048),
	ADVANCED(20000, 3000, 2048),
	EMERALD(22000, 4000, 2048),
	DIAMOND(30000, 5000, 2048),
	IRIDIUM(90000, 10000, 8192),
	IRIDIUM_PLATE(2000000, 10000, 8192),
	//2 - GregTech
	ZINC(5000, 500, 128),
	BRASS(7000, 500, 128),
	ALUMINUM(10000, 500, 256),
	STEEL(13000, 600, 1024),
	INVAR(15000, 600, 21048),
	ELECTRUM(17000, 900, 512),
	NICKEL(20000, 700, 2048),
	OSMIUM(35000, 3000, 2048),
	TITANIUM(30000, 2000, 2048),
	PLATINUM(40000, 4000, 4096),
	TUNGSTEN(50000, 5000, 4096),
	CHROME(60000, 6000, 4096),
	TUNGSTEN_STEEL(70000, 10000, 8192),
	//3 - Thaumcraft
	THAUMIUM(22000, 4000, 2048)
	;
	
	public static TileEntityReservoir makeTileEntity(int metadata) {
		try {
			TileEntityReservoir tileEntity = new TileEntityReservoir();
			return tileEntity;
		}
		catch (Exception e) {
			WCLog.warn("Failed to Register Watermill: "
				+ values()[metadata].getShowedName());
			throw Throwables.propagate(e);
		}
	}
	
	public int capacity;
	public int maxUse;
	
	private ReservoirType(int capacity, int maxUse, int maxOutput) {
		this.capacity = capacity;
		this.maxUse = maxUse;
	}
	
	public String getShowedName() {
		return StatCollector.translateToLocal("cptwtrml.reservoir." + name()) + ' ' +
				StatCollector.translateToLocal("cptwtrml.reservoir.RESERVOIR");
	}
	
	public String tileEntityName() {
		return "ReservoirType." + name();
	}
}
