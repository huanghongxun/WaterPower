/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.jei

import com.google.common.collect.Lists
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.BlankRecipeWrapper
import net.minecraft.item.ItemStack
import waterpower.common.recipe.IRecipeInput
import waterpower.common.recipe.RecipeOutput
import java.util.*

class RecipeWrapper(private val container: Map.Entry<IRecipeInput, RecipeOutput>,
                    internal val category: RecipeCategory) : BlankRecipeWrapper() {

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists(ItemStack::class.java, getInputs())
        ingredients.setOutputs(ItemStack::class.java, ArrayList(getOutputs()))
    }

    override fun getInputs(): List<List<ItemStack>> =
            Collections.singletonList(container.key.getInputs())

    override fun getOutputs(): List<ItemStack> =
            Lists.newArrayList(container.value.items)

    companion object {
        fun createRecipes(category: RecipeCategory): List<RecipeWrapper> {
            val recipes = mutableListOf<RecipeWrapper>()
            for (container in category.recipeManager.getAllRecipes())
                recipes.add(RecipeWrapper(container, category))
            return recipes
        }
    }

}