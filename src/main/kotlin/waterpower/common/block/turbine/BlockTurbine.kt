/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.turbine

import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.LoaderState
import waterpower.annotations.Init
import waterpower.annotations.NewInstance
import waterpower.common.block.BlockRotor
import waterpower.common.block.watermill.EnumWatermill
import waterpower.common.init.WPBlocks
import waterpower.common.item.EnumCrafting
import waterpower.common.item.ItemCrafting
import waterpower.common.recipe.Recipes
import waterpower.integration.Mod
import waterpower.integration.ic2.ICItemFinder

@Init
@NewInstance(LoaderState.ModState.PREINITIALIZED)
class BlockTurbine : BlockRotor<Turbines>("turbine", Material.IRON, Turbines::class.java) {

    init {
        WPBlocks.turbine = this
        WPBlocks.blocks += this
    }

    companion object {
        @JvmStatic
        fun postInit() {
            val carbonPlate: Any = ICItemFinder.getItem("crafting", "carbon_plate") ?: "gemDiamond"
            val transformerUpgrade: Any = ICItemFinder.getItem("upgrade", "transformer") ?: "circuitBasic"
            val advancedMachine: Any = ICItemFinder.getItem("resource,advanced_machine") ?: "blockVanadiumSteel"

            Recipes.craft(WPBlocks.turbine.getItemStack(Turbines.MK1), "SAU", "CGA", "SAU", 'S', Blocks.IRON_BARS, 'A',
                    WPBlocks.watermill.getItemStack(EnumWatermill.MK5), 'G', ItemCrafting.get(EnumCrafting.reservoir_core),
                    'U', ItemCrafting.get(EnumCrafting.updater_mk1), 'C', Blocks.GLASS_PANE)
            Recipes.craft(WPBlocks.turbine.getItemStack(Turbines.MK2), "IUI", "TCT", "IRI", 'T', WPBlocks.turbine.getItemStack(Turbines.MK1), 'I',
                    if (Mod.IndustrialCraft2.isAvailable) "plateDenseIron" else "plateDenseSteel", 'U', transformerUpgrade, 'C', "circuitAdvanced", 'R',
                    ItemCrafting.get(EnumCrafting.base_rotor))
            if (Mod.IndustrialCraft2.isAvailable) {
                Recipes.craft(WPBlocks.turbine.getItemStack(Turbines.MK3), "IUI", "TCT", "IRI", 'T', WPBlocks.turbine.getItemStack(Turbines.MK2), 'I',
                        carbonPlate, 'U', "circuitAdvanced", 'C', advancedMachine, 'R', ItemCrafting.get(EnumCrafting.base_rotor))
                Recipes.craft(WPBlocks.turbine.getItemStack(Turbines.MK4), "IRI", "TCT", "IRI", 'T', WPBlocks.turbine.getItemStack(Turbines.MK3), 'I',
                        ICItemFinder.getItem("plating"), 'C', ICItemFinder.getItem("te,teleporter"), 'R', ItemCrafting.get(EnumCrafting.base_rotor))
            }


            for (i in 1 until Turbines.values().size) {
                Recipes.craft(WPBlocks.turbine.getItemStack(i), " W ", "WTW", " W ", 'W', WPBlocks.turbine.getItemStack(i - 1),
                        'T', transformerUpgrade)
            }

        }
    }

}