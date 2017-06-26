/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import net.minecraftforge.fml.common.LoaderState
import waterpower.annotations.Init
import waterpower.annotations.NewInstance
import waterpower.client.i18n
import waterpower.common.INameable
import waterpower.common.init.WPItems
import waterpower.common.recipe.Recipes
import waterpower.integration.IDs
import waterpower.integration.Mod
import waterpower.integration.ic2.ICItemFinder
import waterpower.util.generalize
import waterpower.util.getItemStack


@NewInstance(LoaderState.ModState.PREINITIALIZED)
class ItemRange : ItemEnum<RangePlugins>("range", RangePlugins.values()) {

    init {
        WPItems.range = this
        WPItems.items += this
    }

    override fun getTextureFolder() = "range"

    companion object {
        @JvmStatic
        @Init(LoaderState.ModState.POSTINITIALIZED)
        fun addRecipes() {
            val machine: Any = ICItemFinder.getItem("resource", "machine") ?: getItemStack(IDs.Mekanism, "BasicBlock", 8) ?: "blockIron"

            if (Mod.IndustrialCraft2.isAvailable) {
                Recipes.craft(WPItems.range.getItemStack(RangePlugins.MK1), "WSW", "SAS", "WSW", 'W', "circuitBasic", 'S', ICItemFinder.getItem("te,batbox"),
                        'A', machine)
                Recipes.craft(WPItems.range.getItemStack(RangePlugins.MK2), "WSW", "SAS", "WSW", 'W', "circuitAdvanced", 'S',
                        ICItemFinder.getItem("te", "cesu"), 'A', WPItems.range.getItemStack(RangePlugins.MK1))
                Recipes.craft(WPItems.range.getItemStack(RangePlugins.MK3), "WSW", "SAS", "WSW", 'W',
                        ICItemFinder.getItem("energy_crystal")?.generalize(), 'S', ICItemFinder.getItem("te,mfe"), 'A', WPItems.range.getItemStack(RangePlugins.MK2))
                //addRecipeByOreDictionary(WPItems.range.getItemStack(RangePlugins.MK4), "WSW", "SAS", "WSW", 'W',
                //        getUsualItemStack(ICItemFinder.getItem("lapotron_crystal")), 'S', ICItemFinder.getItem("te,mfe"), 'A', ItemStack(
                //        GlobalItems.range, 1, 2))
            } else {
                Recipes.craft(WPItems.range.getItemStack(RangePlugins.MK1), "WSW", "SAS", "WSW", 'W', ItemComponent.get(EnumComponent.circuit, EnumLevel.MK1), 'S', ItemComponent.get(EnumComponent.rotationAxle, EnumLevel.MK3),
                        'A', machine)
                Recipes.craft(WPItems.range.getItemStack(RangePlugins.MK2), "WSW", "SAS", "WSW", 'W', ItemComponent.get(EnumComponent.circuit, EnumLevel.MK3), 'S',
                        ItemComponent.get(EnumComponent.rotationAxle, EnumLevel.MK4), 'A', WPItems.range.getItemStack(RangePlugins.MK1))
                Recipes.craft(WPItems.range.getItemStack(RangePlugins.MK3), "WSW", "SAS", "WSW", 'W',
                        ItemComponent.get(EnumComponent.circuit, EnumLevel.MK4), 'S', ItemComponent.get(EnumComponent.rotationAxle, EnumLevel.MK5), 'A', WPItems.range.getItemStack(RangePlugins.MK2))
                //addRecipeByOreDictionary(ItemStack(GlobalItems.range, 1, 3), "WSW", "SAS", "WSW", 'W',
                //        ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK5), 'S', ItemCrafting.get(CraftingTypes.rotationAxle, LevelTypes.MK7), 'A', ItemStack(
                //        GlobalItems.range, 1, 2))
            }
        }
    }
}

enum class RangePlugins(val range: Int) : INameable {
    MK1(2), MK2(16), MK3(128);

    override fun getName() = name.toLowerCase()

    override fun getUnlocalizedName() = "waterpower.range." + name

    override fun addInformation(tooltip: MutableList<String>) {
        tooltip += i18n("waterpower.range.$name.info")
    }
}