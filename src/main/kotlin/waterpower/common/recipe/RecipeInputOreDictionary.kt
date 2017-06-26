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
import java.util.*

data class RecipeInputOreDictionary(val ore: String, val count: Int = 1, val meta: Int = OreDictionary.WILDCARD_VALUE) : IRecipeInput {

    override fun matches(input: ItemStack): Boolean {
        val inputs = OreDictionary.getOres(ore)

        for (input1 in inputs) {
            if (input1.item != null) {
                val metaRequired = if (this.meta == OreDictionary.WILDCARD_VALUE) input1.itemDamage else this.meta.toInt()

                if (input.item == input1.item && (input.itemDamage == metaRequired || metaRequired == 32767)) {
                    return true
                }
            }
        }
        return false
    }

    override fun getAmount() = count

    override fun getInputs(): List<ItemStack> {
        val ores = OreDictionary.getOres(ore)
        val ret = ArrayList<ItemStack>(ores.size)

        for (stack in ores)
            if (stack.item != null)
                ret.add(stack)

        return ret
    }
}