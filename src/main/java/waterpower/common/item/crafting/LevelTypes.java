/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.common.item.crafting;

import net.minecraft.client.resources.I18n;

public enum LevelTypes {
    /** Stone & Wood */
    MK1(255, 255, 255, 0),
    /** Brass & Zinc */
    MK3(255, 255, 255, 0),
    /** Steel & Industrial Steel */
    MK4(255, 255, 255, 0),
    /** Maganese Steel */
    MK5(255, 255, 255, 0),
    /** Vanadium Steel */
    MK7(255, 255, 255, 0);

    public short R, G, B, A;

    private LevelTypes(int R, int G, int B, int A) {
        this.R = (short) R;
        this.G = (short) G;
        this.B = (short) B;
        this.A = (short) A;
    }

    public String getShowedName() {
        String format = "cptwtrml.level." + name();
        String s = I18n.format(format);
        return s;
    }
}