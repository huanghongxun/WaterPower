/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.jei

import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawable
import net.minecraft.util.ResourceLocation
import waterpower.common.recipe.IRecipeManager
import java.awt.Point

class DefaultRecipeCategory(uid: String, title: String, recipeManager: IRecipeManager, guiHelper: IGuiHelper, image: ResourceLocation)
    : RecipeCategory(uid, title, recipeManager) {

    val _background: IDrawable = guiHelper.createDrawable(image, 3, 15, 140, 60)

    override fun getInputSlotPositions(): List<Point> = listOf(Point(22, 34 - 16))
    override fun getOutputSlotPositions(): List<Point> = listOf(Point(82, 35 - 16))
    override fun getBackground() = _background
}