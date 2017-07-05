/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.recipe

import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import waterpower.util.getCount

data class RecipeInputItemStack(var input: ItemStack, val count: Int = getCount(input)) : IRecipeInput {

    init {
        input = input.copy()
    }

    override fun getAmount() = count

    override fun getInputs(): List<ItemStack> = listOf(input)

    override fun matches(input: ItemStack)
            = this.input.item == input.item && (this.input.itemDamage == input.itemDamage || this.input.itemDamage == OreDictionary.WILDCARD_VALUE)

}