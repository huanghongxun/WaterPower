/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.item.crafting;

import net.minecraft.util.StatCollector;

public enum MaterialTypes {
    Zinc(182, 201, 206, 0, false, "Zinc"), ZincAlloy(226, 226, 226, 0, false, "ZincAlloy"), Neodymium(210, 221, 221, 0, false, "Neodymium"), NeodymiumMagnet(
            162, 170, 171, 0, true, "NeodymiumMagnet"), IndustrialSteel(221, 243, 249, 0, true, "IndustrialSteel"), Magnet(80, 83, 91, 0, false, "Magnetite"), Vanadium(
            189, 197, 202, 0, true, "Vanadium"), VanadiumSteel(166, 176, 183, 0, true, "VanadiumSteel"), Manganese(137, 156, 167, 0, false, "Manganese"), ManganeseSteel(
            174, 181, 194, 0, true, "ManganeseSteel"), Steel(75, 83, 94, 0, false, "Steel");

    public static int space = 100;

    public short R, G, B, A;
    public boolean blastFurnaceRequired;
    public String name;

    public static final MaterialTypes[] MATERIALS_SORTED_BY_VALUE = {

    };

    private MaterialTypes(int R, int G, int B, int A, boolean blastFurnaceRequired, String name) {
        this.R = (short) R;
        this.G = (short) G;
        this.B = (short) B;
        this.A = (short) A;
        this.blastFurnaceRequired = blastFurnaceRequired;
        this.name = name;
    }

    public int ind() {
        return ordinal() * space;
    }

    public String getShowedName() {
        String format = "cptwtrml.material." + name();
        String s = StatCollector.translateToLocal(format);
        return s;
    }

    public String getName() {
        return name;
    }
}