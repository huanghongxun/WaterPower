/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import net.minecraft.util.IStringSerializable

enum class MaterialForms : IStringSerializable {
    dustTiny, dustSmall, dust, plate, plateDense, nugget, ingot, stick, screw, gear, ring;

    override fun getName() = name

    fun getLocalizedName() = waterpower.client.i18n("waterpower.forms.${getName()}")
}