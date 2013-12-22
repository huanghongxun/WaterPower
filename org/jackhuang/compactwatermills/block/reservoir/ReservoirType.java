package org.jackhuang.compactwatermills.block.reservoir;

import java.util.logging.Level;

import org.jackhuang.compactwatermills.block.reservoir.*;
import org.jackhuang.compactwatermills.block.watermills.TileEntityWatermill;
import org.jackhuang.compactwatermills.block.watermills.WaterType;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import com.google.common.base.Throwables;

public enum ReservoirType {
	
	WOOD(1000, 100, 8, "木水库方块", TileEntityReservoirWood.class),
	STONE(1500, 100, 8, "石水库方块", TileEntityReservoirStone.class),
	LAPIS(2000, 200, 16, "青金石水库方块", TileEntityReservoirLapis.class),
	TIN(3000, 200, 16, "锡水库方块", TileEntityReservoirTin.class),
	COPPER(4000, 300, 32, "铜水库方块", TileEntityReservoirCopper.class),
	LEAD(4000, 300, 32, "铅水库方块", TileEntityReservoirLead.class),
	QUARTZ(5000, 400, 64, "石英水库方块", TileEntityReservoirQuartz.class),
	BRONZE(7000, 400, 64, "青铜水库方块", TileEntityReservoirBronze.class),
	IRON(10000, 500, 128, "铁水库方块", TileEntityReservoirIron.class),
	NETHER(10000, 1000, 128, "地狱砖块水库方块", TileEntityReservoirNether.class),
	OBSIDIAN(12000, 600, 256, "黑曜石水库方块", TileEntityReservoirObsidian.class),
	SILVER(14000, 700, 256, "银水库方块", TileEntityReservoirSilver.class),
	REFINEDIRON(16000, 900, 512, "精炼铁水库方块", TileEntityReservoirRefinedIron.class),
	GOLD(18000, 1000, 512, "金水库方块", TileEntityReservoirGold.class),
	CARBON(20000, 2000, 1024, "碳板水库方块", TileEntityReservoirCarbon.class),
	ADVANCED(20000, 3000, 1024, "高级合金水库方块", TileEntityReservoirAdvanced.class),
	EMERALD(22000, 4000, 2048, "绿宝石水库方块", TileEntityReservoirEmerald.class),
	DIAMOND(24000, 5000, 2048, "钻石水库方块", TileEntityReservoirDiamond.class),
	IRIDIUM(500000, 10000, 8192, "铱水库方块", TileEntityReservoirIridium.class),
	IRIDIUMPLATE(20000000, 10000, 8192, "铱板水库方块", TileEntityReservoirIridiumPlate.class);
	
	public static TileEntityReservoir makeTileEntity(int metadata) {
		try {
			TileEntityReservoir tileEntity = values()[metadata].claSS.newInstance();
			return tileEntity;
		}
		catch (Exception e) {
			LogHelper.log(Level.WARNING, "Failed to Register Watermill: "
				+ values()[metadata].showedName);
			throw Throwables.propagate(e);
		}
	}
	
	public int capacity;
	public int maxUse;
	public int maxOutput;
	public String showedName;
	public Class<? extends TileEntityReservoir> claSS;
	
	private ReservoirType(int capacity, int maxUse, int maxOutput,
			String showedName, Class<? extends TileEntityReservoir> c) {
		this.capacity = capacity;
		this.maxUse = maxUse;
		this.maxOutput = maxOutput;
		this.showedName = showedName;
		this.claSS = c;
	}
	
	public String tileEntityName() {
		return "ReservoirType." + name();
	}
}
