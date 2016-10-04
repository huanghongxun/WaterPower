/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.common.item.crafting;

import net.minecraft.client.resources.I18n;

public enum MaterialForms {
    plate, dust, dustTiny, dustSmall, plateDense, ingot, stick, screw, gear, nugget, ring;

    public String getShowedName() {
        String format = "cptwtrml.forms." + name();
        String s = I18n.format(format);
        return s;
    }
}
