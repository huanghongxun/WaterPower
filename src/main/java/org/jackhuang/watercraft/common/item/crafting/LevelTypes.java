/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.item.crafting;

import net.minecraft.util.StatCollector;

public enum LevelTypes {
	/** Stone & Wood*/
	MK1,
	/** Brass & Zinc */
	MK3,
	/** Steel */
	MK4,
	/** Vanadium Steel & Maganese Steel */
	MK5,
	/** 中二病 */
	MK7;
	
	public String getShowedName() {
		String format = "cptwtrml.level." + name();
		String s = StatCollector.translateToLocal(format);
		return s;
	}
}
