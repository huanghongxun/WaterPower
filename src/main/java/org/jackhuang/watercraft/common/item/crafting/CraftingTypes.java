/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.item.crafting;


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
