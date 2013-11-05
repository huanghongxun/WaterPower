package org.jackhuang.compactwatermills.block.turbines;

public enum ReservoirType {
	
	WOOD(0.5, "木水库方块"),
	STONE(0.75, "石水库方块"),
	LEAD(2, "铅水库方块"),
	TIN(1.5, "锡水库方块"),
	//GOLD(0.7, "金水库方块"),
	COPPER(2, "铜水库方块"),
	QUARTZ(2.5, "石英水库方块"),
	BRONZE(3.5, "青铜水库方块"),
	//SLIVER(0.3, "银水库方块"),
	IRON(5, "铁水库方块"),
	//OBSIDIAN(0.45, "黑曜石水库方块"),
	//LAPIS(0.55, "青金石水库方块"),
	NEITHER(5, "地狱砖块水库方块"),
	REFINEDIRON(8,  "精炼铁水库方块"),
	CARBON(10, "碳板水库方块"),
	ADVANCED(10, "高级合金水库方块"),
	//EMERALD(0.75, "绿宝石水库方块"),
	//DIAMOND(0.8, "钻石水库方块"),
	IRIDIUM(150, "铱水库方块"),
	IRIDIUMPLATE(10000, "铱板水库方块");
	
	public double capacity;
	
	public String showedName;
	
	private ReservoirType(double capacity, String showedName) {
		this.capacity = capacity;
		this.showedName = showedName;
	}
}
