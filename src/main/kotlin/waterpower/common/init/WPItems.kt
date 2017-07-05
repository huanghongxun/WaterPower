/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.init

import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import waterpower.common.block.watermill.EnumWatermill
import waterpower.common.item.*
import java.util.*

object WPItems {
    val items = LinkedList<Item>()
    val registryNames = HashSet<ResourceLocation>()

    lateinit var crushed: ItemOreDust
    lateinit var material: ItemMaterial
    lateinit var crafting: ItemCrafting
    lateinit var range: ItemRange
    lateinit var hammer: ItemWoodenHammer
    lateinit var plugin: ItemPlugins
    lateinit var component: ItemComponent
    val trousers = EnumMap<EnumWatermill, ItemTrouser>(EnumWatermill::class.java)
    val rotors = EnumMap<EnumRotor, ItemRotor>(EnumRotor::class.java)
}