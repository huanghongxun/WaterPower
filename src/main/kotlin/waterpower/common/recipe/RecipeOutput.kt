/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.recipe

import com.google.common.collect.Lists
import net.minecraft.item.ItemStack


data class RecipeOutput(val power: Int, val items: Collection<ItemStack>) {

    constructor(items: Collection<ItemStack>) : this(-1, items) {}

    constructor(power: Int, vararg items: ItemStack) : this(power, Lists.newArrayList(*items)) {}

    constructor(vararg items: ItemStack) : this(-1, *items) {}

}
