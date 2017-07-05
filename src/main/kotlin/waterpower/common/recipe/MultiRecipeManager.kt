/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.recipe

import net.minecraft.item.ItemStack
import java.util.*


class MultiRecipeManager : IRecipeManager {
    internal var container: MutableList<IRecipeManager> = ArrayList()

    init {
        addRecipeManager(RecipeManager())
    }

    override fun addRecipe(input: ItemStack, vararg outputs: ItemStack): Boolean {
        for (r in container) {
            if (r.addRecipe(input, *outputs))
                return true
        }
        return false
    }

    override fun removeRecipe(input: ItemStack): Boolean {
        for (r in container) {
            if (r.removeRecipe(input))
                return true
        }
        return false
    }

    override fun getOutput(input: ItemStack, adjustInput: Boolean): RecipeOutput? {
        for (r in container) {
            val a = r.getOutput(input, adjustInput)
            if (a != null)
                return a
        }
        return null
    }

    override fun getAllRecipes(): Map<IRecipeInput, RecipeOutput> {
        val map = HashMap<IRecipeInput, RecipeOutput>()
        for (r in container) {
            map.putAll(r.getAllRecipes())
        }
        return map
    }

    fun addRecipeManager(rm: IRecipeManager): MultiRecipeManager {
        container.add(rm)
        return this
    }

}
