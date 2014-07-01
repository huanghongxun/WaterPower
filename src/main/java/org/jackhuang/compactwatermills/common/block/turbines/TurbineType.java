package org.jackhuang.compactwatermills.common.block.turbines;

import net.minecraft.util.StatCollector;

public enum TurbineType {
	MK1(512, "水轮机MK1", "turbineMK1"),
	MK2(2048, "水轮机MK2", "turbineMK2"),
	MK3(8192, "水轮机MK3", "turbineMK3"),
	MK4(32768, "水轮机MK4", "turbineMK4"),
	MK5(131072, "水轮机MK5", "turbineMK5"),
	MK6(524288, "水轮机MK6", "turbineMK6"),
	MK7(2097152, "水轮机MK7", "turbineMK7"),
	MK8(8388608, "水轮机MK8", "turbineMK8"),
	MK9(33554432, "水轮机MK9", "turbineMK9"),
	MK10(134217728, "水轮机MK10", "turbineMK10"),
	MK11(536870912, "水轮机MK11", "turbineMK11"),
	MK12(2147483647, "水轮机MK12", "turbineMK12");
	
	public int percent;
	public String unlocalizedName;
	private String showedName;
	
	private TurbineType(int percent, String showedName, String unlocalizedName) {
		this.percent = percent;
		this.showedName = showedName;
		this.unlocalizedName = unlocalizedName;
	}
	
	public String getShowedName() {
		return StatCollector.translateToLocal("cptwtrml.watermill.TURBINE") + ' ' + name();
	}
}
