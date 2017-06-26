/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import waterpower.common.INameable
import java.awt.Color

enum class MaterialTypes(val R: Int, val G: Int, val B: Int, val A: Int, val blastFurnace: Boolean) : INameable {
    Zinc(182, 201, 206, 0, false),
    ZincAlloy(226, 226, 226, 0, false),
    Neodymium(210, 221, 221, 0, false),
    NeodymiumMagnet(162, 170, 171, 0, true),
    IndustrialSteel(221, 243, 249, 0, true),
    Magnetite(80, 83, 91, 0, false),
    Vanadium(189, 197, 202, 0, true),
    VanadiumSteel(166, 176, 183, 0, true),
    Manganese(137, 156, 167, 0, false),
    ManganeseSteel(174, 181, 194, 0, true),
    Steel(75, 83, 94, 0, false);

    override fun getName() = name.toLowerCase()

    override fun getUnlocalizedName() =
            "waterpower.material." + name

    val index: Int
        get() = ordinal * MaterialTypes.Companion.SPACE

    val color: Int by lazy { Color(R, G, B).rgb and 0xffffff }

    companion object {
        const val SPACE = 100
    }
}