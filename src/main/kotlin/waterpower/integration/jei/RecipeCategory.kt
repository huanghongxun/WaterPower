/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.jei

import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import waterpower.WaterPower
import waterpower.common.recipe.IRecipeManager
import java.awt.Point

abstract class RecipeCategory(val machineName: String, val _title: String, val recipeManager: IRecipeManager)
    : IRecipeCategory<RecipeWrapper> {

    override fun getTitle() = _title
    override fun getUid() = machineName
    override fun drawExtras(p0: Minecraft) {}
    override fun getModName() = WaterPower.MOD_NAME
    override fun getIcon() = null
    override fun getTooltipStrings(mouseX: Int, mouseY: Int) = emptyList<String>()

    abstract fun getInputSlotPositions(): List<Point>
    abstract fun getOutputSlotPositions(): List<Point>

    override fun setRecipe(recipeLayout: IRecipeLayout, p1: RecipeWrapper, ingredients: IIngredients) {
        val itemStacks = recipeLayout.getItemStacks()
        val inputs = ingredients.getInputs(ItemStack::class.java).get(0)
        val outputs = ingredients.getOutputs(ItemStack::class.java)
        val inputSlots = getInputSlotPositions()
        val outputSlots = getOutputSlotPositions()
        itemStacks.init(0, true, 0, 1)
        var j = 0
        for (i in 0 until inputSlots.size) {
            val point = inputSlots[i]
            itemStacks.init(j, true, point.x, point.y)
            if (i < inputs.size)
                itemStacks.set(j, inputs[i])
            j++
        }
        for (i in 0 until outputSlots.size) {
            val point = outputSlots.get(i)
            itemStacks.init(j, false, point.x, point.y)
            if (i < outputs.size)
                itemStacks.set(j, outputs[i])
            j++
        }
    }
}