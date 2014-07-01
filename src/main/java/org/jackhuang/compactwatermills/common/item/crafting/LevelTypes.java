package org.jackhuang.compactwatermills.common.item.crafting;

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
