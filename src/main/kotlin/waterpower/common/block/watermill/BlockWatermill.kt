/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.watermill

import net.minecraft.block.material.Material
import net.minecraftforge.fml.common.LoaderState
import waterpower.annotations.Init
import waterpower.annotations.NewInstance
import waterpower.common.block.BlockRotor
import waterpower.common.init.WPBlocks
import waterpower.common.item.*
import waterpower.common.recipe.Recipes
import waterpower.integration.Mod
import waterpower.integration.ic2.ICItemFinder
import waterpower.util.generalize


@NewInstance(LoaderState.ModState.PREINITIALIZED)
class BlockWatermill : BlockRotor<EnumWatermill>("watermill", Material.IRON, EnumWatermill::class.java, EnumWatermill.values()) {

    init {
        WPBlocks.watermill = this
        WPBlocks.blocks += this
    }

    companion object {
        @JvmStatic
        @Init(LoaderState.ModState.POSTINITIALIZED)
        fun addRecipes() {
            val transformerUpgrade: Any = ICItemFinder.getItem("upgrade", "transformer") ?: "circuitBasic"

            if (Mod.IndustrialCraft2.isAvailable)
                Recipes.craft(WPBlocks.watermill.getItemStack(2), "W", 'W', ICItemFinder.getItem("te", "water_kinetic_generator"))

            for (i in 1 until EnumWatermill.values().size) {
                Recipes.craft(WPBlocks.watermill.getItemStack(i), " W ", "WTW", " W ", 'W', WPBlocks.watermill.getItemStack(i - 1),
                        'T', transformerUpgrade)
            }


            addWatermillRecipe(EnumWatermill.MK1, EnumLevel.MK1);
            addWatermillRecipe(EnumWatermill.MK3, EnumLevel.MK3);
            addWatermillRecipe(EnumWatermill.MK5, EnumLevel.MK4);
            addWatermillRecipe(EnumWatermill.MK7, EnumLevel.MK5);
            addWatermillRecipe(EnumWatermill.MK9, EnumLevel.MK7);
        }

        fun redundantRecipes() {
            val advancedAlloy: Any = ICItemFinder.getItem("crafting", "alloy") ?: "plateSteel"
            val carbonPlate: Any = ICItemFinder.getItem("crafting", "carbon_plate") ?: "gemDiamond"

            Recipes.craft(WPBlocks.watermill.getItemStack(EnumWatermill.MK1), "CUC", "SAS", "PMP", 'C', ICItemFinder.getItem("fluid_cell"), 'A', "blockIron",
                    'S', "plateIron", 'P', "circuitBasic", 'M', ItemCrafting.get(EnumCrafting.updater_mk0), 'U', ItemCrafting.get(EnumCrafting.water_uranium_ingot))

            Recipes.craft(WPBlocks.watermill.getItemStack(EnumWatermill.MK2), "CUC", "SAS", "PMP", 'C', ICItemFinder.getItem("fluid_cell"), 'A',
                    WPBlocks.watermill.getItemStack(EnumWatermill.MK1), 'S', "plateIron", 'P', "circuitAdvanced", 'M', ItemCrafting.get(EnumCrafting.updater_mk0), 'U',
                    ItemCrafting.get(EnumCrafting.water_uranium_ingot))

            Recipes.craft(WPBlocks.watermill.getItemStack(EnumWatermill.MK4), "CUC", "SAS", "PMP", 'C', ICItemFinder.getItem("fluid_cell"), 'A',
                    WPBlocks.watermill.getItemStack(EnumWatermill.MK2), 'S', carbonPlate, 'P', "circuitAdvanced", 'M', ItemCrafting.get(EnumCrafting.updater_mk1), 'U',
                    ItemCrafting.get(EnumCrafting.water_uranium_plate_mk1))

            Recipes.craft(WPBlocks.watermill.getItemStack(EnumWatermill.MK5), "CUC", "SAS", "PMP", 'C', ICItemFinder.getItem("fluid_cell"), 'A',
                    WPBlocks.watermill.getItemStack(EnumWatermill.MK4), 'S', advancedAlloy, 'P', "circuitAdvanced", 'M', ItemCrafting.get(EnumCrafting.updater_mk2), 'U',
                    ItemCrafting.get(EnumCrafting.water_uranium_plate_mk2))

            Recipes.craft(WPBlocks.watermill.getItemStack(EnumWatermill.MK6), "CUC", "SAS", "PMP", 'C', ICItemFinder.getItem("fluid_cell"), 'A',
                    WPBlocks.watermill.getItemStack(EnumWatermill.MK5), 'S', ICItemFinder.getItem("te,electric_kinetic_generator"), 'P', "circuitAdvanced", 'M', ItemCrafting.get(EnumCrafting.updater_mk2), 'U',
                    ItemCrafting.get(EnumCrafting.water_uranium_plate_mk3))

            Recipes.craft(WPBlocks.watermill.getItemStack(EnumWatermill.MK7), "CUC", "SAS", "PMP", 'C', ICItemFinder.getItem("fluid_cell"), 'A',
                    WPBlocks.watermill.getItemStack(EnumWatermill.MK6), 'S', ICItemFinder.getItem("lapotron_crystal")?.generalize(), 'P', "circuitAdvanced", 'M',
                    ItemCrafting.get(EnumCrafting.updater_mk3), 'U', ItemCrafting.get(EnumCrafting.water_uranium_plate_mk4))

        }

        private fun addWatermillRecipe(type: EnumWatermill, level: EnumLevel) {
            Recipes.craft(WPBlocks.watermill.getItemStack(type), "CBC", "IAT", "SRS", 'C', ItemComponent.get(EnumComponent.casing, level),
                    'I', ItemComponent.get(EnumComponent.outputInterface, level), 'A', ItemComponent.get(EnumComponent.rotationAxle, level), 'B',
                    ItemComponent.get(EnumComponent.paddleBase, level), 'S', ItemComponent.get(EnumComponent.stator, level), 'R',
                    ItemComponent.get(EnumComponent.rotor, level), 'T', ItemComponent.get(EnumComponent.circuit, level))
        }
    }
}