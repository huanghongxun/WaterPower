/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.jei

import mezz.jei.api.BlankModPlugin
import mezz.jei.api.IGuiHelper
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import waterpower.WaterPower
import waterpower.client.i18n
import waterpower.common.block.machine.*
import waterpower.common.init.WPBlocks
import waterpower.common.recipe.IRecipeManager
import waterpower.common.recipe.RecipeManagers

@JEIPlugin
class JEIModule : BlankModPlugin() {

    override fun register(registry: IModRegistry) {
        val guiHelper = registry.jeiHelpers.guiHelper
        registry.addRecipeHandlers(RecipeHandler())
        addMachineRecipes(registry, "advcompressor", RecipeManagers.advCompressor, guiHelper, WPBlocks.advanced_compressor, GuiAdvCompressor::class.java)
        addMachineRecipes(registry, "compressor", RecipeManagers.compressor, guiHelper, WPBlocks.compressor, GuiCompressor::class.java)
        addMachineRecipes(registry, "cutter", RecipeManagers.cutter, guiHelper, WPBlocks.cutter, GuiCutter::class.java)
        addMachineRecipes(registry, "lathe", RecipeManagers.lathe, guiHelper, WPBlocks.lathe, GuiLathe::class.java)
        addMachineRecipes(registry, "crusher", RecipeManagers.crusher, guiHelper, WPBlocks.crusher, GuiCrusher::class.java)
        addMachineRecipes(registry, "sawmill", RecipeManagers.sawmill, guiHelper, WPBlocks.sawmill, GuiSawmill::class.java)
    }

    private fun addMachineRecipes(registry: IModRegistry, id: String, recipeManager: IRecipeManager, guiHelper: IGuiHelper, itemStack: ItemStack, guiClass: Class<out GuiContainer>) {
        val uid = "waterpower.machine.$id"
        val category = DefaultRecipeCategory(uid, i18n(uid), recipeManager, guiHelper, ResourceLocation("${WaterPower.MOD_ID}:textures/gui/$id.png"))
        registry.addRecipeCategories(category)
        registry.addRecipes(RecipeWrapper.createRecipes(category))
        registry.addRecipeClickArea(guiClass, 50, 34, 24, 14, uid)
        registry.addRecipeCategoryCraftingItem(itemStack, uid)
    }

}
