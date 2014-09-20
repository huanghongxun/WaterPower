package org.jackhuang.watercraft.common.block.turbines;

import net.minecraft.util.StatCollector;

public enum TurbineType {
	MK1(512, "turbineMK1"),
	MK2(2048, "turbineMK2"),
	MK3(8192, "turbineMK3"),
	MK4(32768, "turbineMK4"),
	MK5(131072, "turbineMK5"),
	MK6(524288, "turbineMK6"),
	MK7(2097152, "turbineMK7"),
	MK8(8388608,  "turbineMK8"),
	MK9(33554432, "turbineMK9"),
	MK10(134217728, "turbineMK10"),
	MK11(536870912, "turbineMK11"),
	MK12(2147483647, "turbineMK12");
	
	public int percent;
	public String unlocalizedName;
	
	private TurbineType(int percent, String unlocalizedName) {
		this.percent = percent;
		this.unlocalizedName = unlocalizedName;
	}
	
	public String getShowedName() {
		return StatCollector.translateToLocal("cptwtrml.watermill.TURBINE") + ' ' + name();
	}
}
