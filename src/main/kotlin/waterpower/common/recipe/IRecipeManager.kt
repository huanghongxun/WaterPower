/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.recipe

import net.minecraft.item.ItemStack

interface IRecipeManager {
    fun addRecipe(input: ItemStack, vararg outputs: ItemStack): Boolean
    fun removeRecipe(input: ItemStack): Boolean
    fun getOutput(input: ItemStack, adjustInput: Boolean): RecipeOutput?
    fun getAllRecipes(): Map<IRecipeInput, RecipeOutput>
}