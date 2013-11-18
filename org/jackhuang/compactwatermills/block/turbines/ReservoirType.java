package org.jackhuang.compactwatermills.block.turbines;

public enum ReservoirType {
	
	WOOD(1000, 100, 8, "木水库方块"),
	STONE(1500, 100, 32, "石水库方块"),
	LAPIS(2000, 200, 32, "青金石水库方块"),
	TIN(3000, 200, 32, "锡水库方块"),
	COPPER(4000, 300, 32, "铜水库方块"),
	LEAD(4000, 300, 32, "铅水库方块"),
	QUARTZ(5000, 400, 32, "石英水库方块"),
	BRONZE(7000, 400, 32, "青铜水库方块"),
	IRON(10000, 500, 32, "铁水库方块"),
	NETHER(10000, 1000, 32, "地狱砖块水库方块"),
	OBSIDIAN(12000, 600, 32, "黑曜石水库方块"),
	SLIVER(14000, 700, 32, "银水库方块"),
	REFINEDIRON(16000, 900, 32, "精炼铁水库方块"),
	GOLD(18000, 1000, 64, "金水库方块"),
	CARBON(20000, 2000, 64, "碳板水库方块"),
	ADVANCED(20000, 3000, 128, "高级合金水库方块"),
	EMERALD(22000, 4000, 128, "绿宝石水库方块"),
	DIAMOND(24000, 5000, 128, "钻石水库方块"),
	IRIDIUM(500000, 10000, 2048, "铱水库方块"),
	IRIDIUMPLATE(20000000, 10000, 2048, "铱板水库方块");
	
	public int capacity;
	public int maxUse;
	public int maxOutput;
	public String showedName;
	
	private ReservoirType(int capacity, int maxUse, int maxOutput,
			String showedName) {
		this.capacity = capacity;
		this.maxUse = maxUse;
		this.maxOutput = maxOutput;
		this.showedName = showedName;
	}
}
