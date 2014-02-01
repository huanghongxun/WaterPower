package org.jackhuang.compactwatermills.block.turbines;

public enum TurbineType {
	MK1(2048, "水轮机MK1", "turbineMK1"),
	MK2(8192, "水轮机MK2", "turbineMK2"),
	MK3(32768, "水轮机MK3", "turbineMK3"),
	MK4(131072, "水轮机MK4", "turbineMK4"),
	MK5(524288, "水轮机MK5", "turbineMK5"),
	MK6(1000000, "水轮机MK6", "turbineMK6");
	
	public int percent;
	public String unlocalizedName;
	public String showedName;
	
	private TurbineType(int percent, String showedName, String unlocalizedName) {
		this.percent = percent;
		this.showedName = showedName;
		this.unlocalizedName = unlocalizedName;
	}
}
