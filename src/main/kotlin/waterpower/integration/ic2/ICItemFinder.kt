/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.ic2

import ic2.api.item.IC2Items
import net.minecraft.item.ItemStack
import waterpower.WaterPower

object ICItemFinder {

    fun getItem(name: String): ItemStack? {
        try {
            val strings = name.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (strings.size == 2)
                return IC2Items.getItem(strings[0], strings[1])
            else
                return IC2Items.getItem(name)
        } catch (e: Throwable) {
            WaterPower.logger.warn("Failed to get Item: " + name)
            return null
        }

    }

    fun getItem(name: String, variant: String): ItemStack? {
        try {
            return IC2Items.getItem(name, variant)
        } catch (e: Throwable) {
            return null
        }

    }
}