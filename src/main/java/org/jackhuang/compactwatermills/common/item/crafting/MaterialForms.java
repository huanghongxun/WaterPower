package org.jackhuang.compactwatermills.common.item.crafting;

import net.minecraft.util.StatCollector;

public enum MaterialForms {
	plate, dust, dustTiny, dustSmall, plateDense,
	ingot, block, stick, screw, gear, nugget, ring;
	
	public String getShowedName() {
		String format = "cptwtrml.forms." + name();
		String s = StatCollector.translateToLocal(format);
		return s;
	}
}
