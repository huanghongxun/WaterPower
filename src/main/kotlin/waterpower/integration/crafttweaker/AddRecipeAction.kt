/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.crafttweaker

import minetweaker.OneWayAction
import net.minecraft.item.ItemStack

import waterpower.common.recipe.IRecipeManager

class AddRecipeAction(internal var name: String, internal var recipeManager: IRecipeManager, internal var `in`: ItemStack, internal var out: ItemStack) : OneWayAction() {
    override fun getOverrideKey() = null

    override fun apply() {
        recipeManager.addRecipe(`in`, out)
    }

    override fun describe(): String {
        return "Adding " + name + " recipe for " + out.displayName
    }

}
