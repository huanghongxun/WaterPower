package org.jackhuang.watercraft.common.block.reservoir;

import net.minecraft.util.StatCollector;

import org.jackhuang.watercraft.common.block.reservoir.*;
import org.jackhuang.watercraft.util.WPLog;

import com.google.common.base.Throwables;

public enum ReservoirType {
	//1 - IndustrialCraft
	WOOD(100000, 100, 8),
	STONE(150000, 100, 16),
	LAPIS(200000, 200, 64),
	TIN(300000, 200, 64),
	COPPER(400000, 300, 64),
	LEAD(400000, 300, 64),
	QUARTZ(500000, 400, 128),
	BRONZE(700000, 400, 128),
	IRON(1000000, 500, 256),
	NETHER(1000000, 1000, 256),
	OBSIDIAN(1200000, 600, 512),
	SILVER(1400000, 700, 512),
	GOLD(1800000, 1000, 512),
	CARBON(2000000, 2000, 2048),
	ADVANCED(2000000, 3000, 2048),
	EMERALD(2200000, 4000, 2048),
	DIAMOND(3000000, 5000, 2048),
	IRIDIUM(9000000, 10000, 8192),
	IRIDIUM_PLATE(200000000, 10000, 8192),
	//2 - GregTech
	ZINC(500000, 500, 128),
	BRASS(700000, 500, 128),
	ALUMINUM(10000000, 500, 256),
	STEEL(130000, 600, 1024),
	INVAR(1500000, 600, 21048),
	ELECTRUM(1700000, 900, 512),
	NICKEL(2000000, 700, 2048),
	OSMIUM(3500000, 3000, 2048),
	TITANIUM(3000000, 2000, 2048),
	PLATINUM(4000000, 4000, 4096),
	TUNGSTEN(5000000, 5000, 4096),
	CHROME(6000000, 6000, 4096),
	TUNGSTEN_STEEL(7000000, 10000, 8192),
	//3 - Thaumcraft
	THAUMIUM(2200000, 4000, 2048)
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
	
	public String getTitle() {
	    return StatCollector.translateToLocal("cptwtrml.reservoir." + name());
	}
	
	public String tileEntityName() {
		return "ReservoirType." + name();
	}
}
