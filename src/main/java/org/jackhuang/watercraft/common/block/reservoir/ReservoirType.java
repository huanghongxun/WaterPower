package org.jackhuang.watercraft.common.block.reservoir;

import net.minecraft.util.StatCollector;

import org.apache.logging.log4j.Level;
import org.jackhuang.watercraft.common.block.reservoir.*;
import org.jackhuang.watercraft.util.WPLog;

import com.google.common.base.Throwables;

public enum ReservoirType {
	//1 - IndustrialCraft
	WOOD(10000, 100, 8),
	STONE(15000, 100, 16),
	LAPIS(20000, 200, 64),
	TIN(30000, 200, 64),
	COPPER(40000, 300, 64),
	LEAD(40000, 300, 64),
	QUARTZ(50000, 400, 128),
	BRONZE(70000, 400, 128),
	IRON(100000, 500, 256),
	NETHER(100000, 1000, 256),
	OBSIDIAN(120000, 600, 512),
	SILVER(140000, 700, 512),
	GOLD(180000, 1000, 512),
	CARBON(200000, 2000, 2048),
	ADVANCED(200000, 3000, 2048),
	EMERALD(220000, 4000, 2048),
	DIAMOND(300000, 5000, 2048),
	IRIDIUM(900000, 10000, 8192),
	IRIDIUM_PLATE(20000000, 10000, 8192),
	//2 - GregTech
	ZINC(50000, 500, 128),
	BRASS(70000, 500, 128),
	ALUMINUM(1000000, 500, 256),
	STEEL(13000, 600, 1024),
	INVAR(150000, 600, 21048),
	ELECTRUM(170000, 900, 512),
	NICKEL(200000, 700, 2048),
	OSMIUM(350000, 3000, 2048),
	TITANIUM(300000, 2000, 2048),
	PLATINUM(400000, 4000, 4096),
	TUNGSTEN(500000, 5000, 4096),
	CHROME(600000, 6000, 4096),
	TUNGSTEN_STEEL(700000, 10000, 8192),
	//3 - Thaumcraft
	THAUMIUM(220000, 4000, 2048)
	;
	
	public static TileEntityReservoir makeTileEntity(int metadata) {
		try {
			TileEntityReservoir tileEntity = new TileEntityReservoir();
			return tileEntity;
		}
		catch (Exception e) {
			WPLog.warn("Failed to Register Watermill: "
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
