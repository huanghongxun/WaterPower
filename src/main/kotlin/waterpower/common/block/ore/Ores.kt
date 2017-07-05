/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.ore

import waterpower.common.INameable
import waterpower.common.item.MaterialTypes

enum class Ores(val material: MaterialTypes, val harvestLevel: Int) : INameable {
    Monazite(MaterialTypes.Neodymium, 2),
    Vanadium(MaterialTypes.Vanadium, 2),
    Manganese(MaterialTypes.Manganese, 2),
    Magnetite(MaterialTypes.Magnetite, 1),
    Zinc(MaterialTypes.Zinc, 1);

    override fun getUnlocalizedName() = "waterpower.ore.${getName()}"

    override fun getName() = name.toLowerCase()
}