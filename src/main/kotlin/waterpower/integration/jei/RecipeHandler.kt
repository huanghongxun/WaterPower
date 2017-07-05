/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.jei

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper

class RecipeHandler : IRecipeHandler<RecipeWrapper> {

    override fun getRecipeClass(): Class<RecipeWrapper> {
        return RecipeWrapper::class.java
    }

    override fun getRecipeCategoryUid() = "waterpower"

    override fun getRecipeWrapper(recipe: RecipeWrapper): IRecipeWrapper {
        return recipe
    }

    override fun isRecipeValid(recipe: RecipeWrapper): Boolean {
        return !recipe.getInputs().isEmpty()
    }

    override fun getRecipeCategoryUid(wrapper: RecipeWrapper): String {
        return wrapper.category.uid
    }

}
