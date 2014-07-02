package org.jackhuang.watercraft.common.block.reservoir;

import net.minecraft.util.StatCollector;

import org.apache.logging.log4j.Level;
import org.jackhuang.watercraft.common.block.reservoir.*;
import org.jackhuang.watercraft.util.WCLog;

import com.google.common.base.Throwables;

public enum ReservoirType {
	//1 - IndustrialCraft
	WOOD(1000, 100, 8, "木水库方块"),
	STONE(1500, 100, 16, "石水库方块"),
	LAPIS(2000, 200, 64, "青金石水库方块"),
	TIN(3000, 200, 64, "锡水库方块"),
	COPPER(4000, 300, 64, "铜水库方块"),
	LEAD(4000, 300, 64, "铅水库方块"),
	QUARTZ(5000, 400, 128, "石英水库方块"),
	BRONZE(7000, 400, 128, "青铜水库方块"),
	IRON(10000, 500, 256, "铁水库方块"),
	NETHER(10000, 1000, 256, "地狱砖块水库方块"),
	OBSIDIAN(12000, 600, 512, "黑曜石水库方块"),
	SILVER(14000, 700, 512, "银水库方块"),
	REFINED_IRON(16000, 900, 512, "精炼铁水库方块"),
	GOLD(18000, 1000, 512, "金水库方块"),
	CARBON(20000, 2000, 2048, "碳板水库方块"),
	ADVANCED(20000, 3000, 2048, "高级合金水库方块"),
	EMERALD(22000, 4000, 2048, "绿宝石水库方块"),
	DIAMOND(30000, 5000, 2048, "钻石水库方块"),
	IRIDIUM(90000, 10000, 8192, "铱水库方块"),
	IRIDIUM_PLATE(2000000, 10000, 8192, "铱板水库方块"),
	//2 - GregTech
	ZINC(5000, 500, 128, "锌水库方块"),
	BRASS(7000, 500, 128, "黄铜水库方块"),
	ALUMINUM(10000, 500, 256, "铝水库方块"),
	STEEL(13000, 600, 1024, "钢水库方块"),
	INVAR(15000, 600, 21048, "殷钢水库方块"),
	ELECTRUM(17000, 900, 512, "金银水库方块"),
	NICKEL(20000, 700, 2048, "镍水库方块"),
	OSMIUM(35000, 3000, 2048, "锇水库方块"),
	TITANIUM(30000, 2000, 2048, "钛水库方块"),
	PLATINUM(40000, 4000, 4096, "铂水库方块"),
	TUNGSTEN(50000, 5000, 4096, "钨水库方块"),
	CHROME(60000, 6000, 4096, "铬水库方块"),
	TUNGSTEN_STEEL(70000, 10000, 8192, "钨钢水库方块"),
	//3 - Thaumcraft
	THAUMIUM(22000, 4000, 2048, "神秘水库方块")
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
	//public int maxOutput;
	
	private ReservoirType(int capacity, int maxUse, int maxOutput, String showedName) {
		this.capacity = capacity;
		this.maxUse = maxUse;
		//this.maxOutput = maxOutput;
	}
	
	public String getShowedName() {
		return StatCollector.translateToLocal("cptwtrml.reservoir." + name()) + ' ' +
				StatCollector.translateToLocal("cptwtrml.reservoir.RESERVOIR");
	}
	
	public String tileEntityName() {
		return "ReservoirType." + name();
	}
}
