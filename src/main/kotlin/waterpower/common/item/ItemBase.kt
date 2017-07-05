/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraftforge.fml.common.registry.ForgeRegistries
import waterpower.WaterPower
import waterpower.common.init.WPItems

abstract class ItemBase(id: String) : Item() {
    init {
        creativeTab = WaterPower.creativeTab
        unlocalizedName = "waterpower." + id
        setNoRepair()

        setRegistryName(id)
        WPItems.registryNames += registryName!!
        ForgeRegistries.ITEMS.register(this)
    }

    fun isRightCreativeTab(tab: CreativeTabs?) = tab == WaterPower.creativeTab
}