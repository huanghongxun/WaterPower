/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.ore

import net.minecraft.block.material.Material
import net.minecraftforge.fml.common.LoaderState
import net.minecraftforge.oredict.OreDictionary
import waterpower.annotations.Init
import waterpower.annotations.NewInstance
import waterpower.common.block.BlockEnum
import waterpower.common.init.WPBlocks
import waterpower.common.init.WPItems
import waterpower.common.item.MaterialForms
import waterpower.common.recipe.Recipes

@Init
@NewInstance(LoaderState.ModState.PREINITIALIZED)
class BlockOre : BlockEnum<Ores>("ore", Material.ROCK, Ores::class.java) {
    init {
        WPBlocks.ore = this
        WPBlocks.blocks += this

        for (ore in Ores.values()) {
            OreDictionary.registerOre("ore" + ore.name, getItemStack(ore))
            setHarvestLevel("pickaxe", ore.harvestLevel, getStateFromType(ore))
        }
    }

    companion object {
        @JvmStatic
        fun postInit() {
            // register recipes of ores
            for (ore in Ores.values()) {
                val stack = WPBlocks.ore.getItemStack(ore)
                Recipes.crusher(stack, WPItems.crushed.getItemStack(ore, 2))
                Recipes.furnace(stack, WPItems.material.getItemStack(ore.material, MaterialForms.ingot))
                Recipes.furnace(WPItems.crushed.getItemStack(ore), WPItems.material.getItemStack(ore.material, MaterialForms.ingot))
            }
        }
    }
}