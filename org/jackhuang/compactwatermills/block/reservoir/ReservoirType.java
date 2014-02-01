package org.jackhuang.compactwatermills.block.reservoir;

import java.util.logging.Level;

import org.jackhuang.compactwatermills.block.reservoir.*;
import org.jackhuang.compactwatermills.block.watermills.TileEntityWatermill;
import org.jackhuang.compactwatermills.block.watermills.WaterType;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import com.google.common.base.Throwables;

public enum ReservoirType {
	//1 - IndustrialCraft
	WOOD(1000, 100, 8, "木水库方块", TileEntityReservoirWood.class),
	STONE(1500, 100, 16, "石水库方块", TileEntityReservoirStone.class),
	LAPIS(2000, 200, 64, "青金石水库方块", TileEntityReservoirLapis.class),
	TIN(3000, 200, 64, "锡水库方块", TileEntityReservoirTin.class),
	COPPER(4000, 300, 64, "铜水库方块", TileEntityReservoirCopper.class),
	LEAD(4000, 300, 64, "铅水库方块", TileEntityReservoirLead.class),
	QUARTZ(5000, 400, 128, "石英水库方块", TileEntityReservoirQuartz.class),
	BRONZE(7000, 400, 128, "青铜水库方块", TileEntityReservoirBronze.class),
	IRON(10000, 500, 256, "铁水库方块", TileEntityReservoirIron.class),
	NETHER(10000, 1000, 256, "地狱砖块水库方块", TileEntityReservoirNether.class),
	OBSIDIAN(12000, 600, 512, "黑曜石水库方块", TileEntityReservoirObsidian.class),
	SILVER(14000, 700, 512, "银水库方块", TileEntityReservoirSilver.class),
	REFINED_IRON(16000, 900, 512, "精炼铁水库方块", TileEntityReservoirRefinedIron.class),
	GOLD(18000, 1000, 512, "金水库方块", TileEntityReservoirGold.class),
	CARBON(20000, 2000, 2048, "碳板水库方块", TileEntityReservoirCarbon.class),
	ADVANCED(20000, 3000, 2048, "高级合金水库方块", TileEntityReservoirAdvanced.class),
	EMERALD(22000, 4000, 2048, "绿宝石水库方块", TileEntityReservoirEmerald.class),
	DIAMOND(30000, 5000, 2048, "钻石水库方块", TileEntityReservoirDiamond.class),
	IRIDIUM(90000, 10000, 8192, "铱水库方块", TileEntityReservoirIridium.class),
	IRIDIUM_PLATE(2000000, 10000, 8192, "铱板水库方块", TileEntityReservoirIridiumPlate.class),
	//2 - GregTech
	ZINC(5000, 500, 128, "锌水库方块", TileEntityReservoirZinc.class),
	BRASS(7000, 500, 128, "黄铜水库方块", TileEntityReservoirBrass.class),
	ALUMINUM(10000, 500, 256, "铝水库方块", TileEntityReservoirAluminum.class),
	STEEL(13000, 600, 1024, "钢水库方块", TileEntityReservoirSteel.class),
	INVAR(15000, 600, 21048, "殷钢水库方块", TileEntityReservoirInvar.class),
	ELECTRUM(17000, 900, 512, "金银水库方块", TileEntityReservoirElectrum.class),
	NICKEL(20000, 700, 2048, "镍水库方块", TileEntityReservoirNickel.class),
	OSMIUM(35000, 3000, 2048, "锇水库方块", TileEntityReservoirObsidian.class),
	TITANIUM(30000, 2000, 2048, "钛水库方块", TileEntityReservoirTitanium.class),
	PLATINUM(40000, 4000, 4096, "铂水库方块", TileEntityReservoirPlatinum.class),
	TUNGSTEN(50000, 5000, 4096, "钨水库方块", TileEntityReservoirTungsten.class),
	CHROME(60000, 6000, 4096, "铬水库方块", TileEntityReservoirChrome.class),
	TUNGSTEN_STEEL(70000, 10000, 8192, "钨钢水库方块", TileEntityReservoirTungstenSteel.class)
	;
	
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
