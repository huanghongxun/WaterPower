/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.api

import net.minecraft.item.ItemStack


interface IUpgrade {

    fun getUnderworldAdditionalValue(stack: ItemStack): Int

    fun getOverworldAdditionalValue(stack: ItemStack): Int

    fun getRainAdditionalValue(stack: ItemStack): Int

    fun getEnergyDemandMultiplier(stack: ItemStack): Double

    fun getSpeedAdditionalValue(stack: ItemStack): Double

    fun getStorageAdditionalValue(stack: ItemStack): Int

}
