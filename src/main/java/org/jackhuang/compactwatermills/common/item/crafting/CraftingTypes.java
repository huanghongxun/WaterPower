package org.jackhuang.compactwatermills.common.item.crafting;

import net.minecraft.util.StatCollector;

public enum CraftingTypes {
	
	paddleBase,
	drainagePlate,
	fixedFrame,
	fixedTool,
	rotationAxle,
	outputInterface,
	rotor,
	stator,
	casing,
	circuit;
	
	public static final int space = 100;
	
	public int ind() {
		return ordinal() * space;
	}
	
	public String getShowedName() {
		String format = "cptwtrml.crafting." + name();
		String s = StatCollector.translateToLocal(format);
		/*if(format.equals(s)) {
			return showedName;
		}*/
		return s;
	}

}
