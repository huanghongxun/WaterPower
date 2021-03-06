/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common

import net.minecraft.util.IStringSerializable
import waterpower.client.i18n

interface INameable : IStringSerializable {
    fun getUnlocalizedName(): String
    fun getLocalizedName() = i18n(getUnlocalizedName())
    fun addInformation(tooltip: MutableList<String>) {}
}