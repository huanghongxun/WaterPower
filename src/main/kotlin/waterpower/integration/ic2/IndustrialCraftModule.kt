/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.ic2

import ic2.api.recipe.Recipes
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.Optional
import waterpower.annotations.Integration
import waterpower.common.block.ore.Ores
import waterpower.common.init.WPItems
import waterpower.common.item.MaterialForms
import waterpower.integration.IDs
import waterpower.integration.IModule

@Integration(IDs.IndustrialCraft2)
object IndustrialCraftModule : IModule() {
    val RECIPE_AVAILABLE = true

    @Optional.Method(modid = IDs.IndustrialCraft2)
    fun compressor(input: ItemStack, output: ItemStack): Boolean {
        if (RECIPE_AVAILABLE)
            try {
                return Recipes.compressor.addRecipe(Recipes.inputFactory.forStack(input), null, false, output)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        return false
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    fun blastFurnace(input: ItemStack, output: ItemStack, time: Int)
            = blastFurnace(input, output)

    @Optional.Method(modid = IDs.IndustrialCraft2)
    fun blastFurnace(input: ItemStack, output: ItemStack): Boolean {
        if (RECIPE_AVAILABLE)
            try {
                return Recipes.blastfurnace.addRecipe(Recipes.inputFactory.forStack(input), null, false, output)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        return false
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    fun blastFurnace(input: String, output: ItemStack, time: Int): Boolean {
        if (RECIPE_AVAILABLE)
            try {
                return Recipes.blastfurnace.addRecipe(Recipes.inputFactory.forOreDict(input), null, false, output)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        return false
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    fun macerator(input: ItemStack, output: ItemStack): Boolean {
        if (RECIPE_AVAILABLE)
            try {
                return Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(input), null, false, output)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        return false
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    fun metalformerRolling(input: ItemStack, output: ItemStack): Boolean {
        if (RECIPE_AVAILABLE)
            try {
                return Recipes.metalformerRolling.addRecipe(Recipes.inputFactory.forStack(input), null, false, output)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        return false
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    fun blockcutter(input: ItemStack, output: ItemStack): Boolean {
        if (RECIPE_AVAILABLE)
            try {
                return Recipes.blockcutter.addRecipe(Recipes.inputFactory.forStack(input), null, false, output)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        return false
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    fun oreWashing(input: ItemStack, vararg output: ItemStack): Boolean {
        if (RECIPE_AVAILABLE)
            try {
                val metadata = NBTTagCompound()
                metadata.setInteger("amount", 1000)
                return Recipes.oreWashing.addRecipe(Recipes.inputFactory.forStack(input), metadata, false, *output)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        return false
    }

    fun getMaceratorMachineManager(): Any? {
        try {
            return Recipes.macerator
        } catch (t: Throwable) {
            return null
        }
    }

    fun getCompressorMachineManager(): Any? {
        try {
            return Recipes.compressor
        } catch (t: Throwable) {
            return null
        }
    }

    fun getCutterMachineManager(): Any? {
        try {
            return Recipes.blockcutter
        } catch (t: Throwable) {
            return null
        }
    }

    override fun onPreInit() {
        waterpower.common.recipe.Recipes.benders += this::metalformerRolling
        waterpower.common.recipe.Recipes.blastFurnaces += this::blastFurnace
        waterpower.common.recipe.Recipes.blastFurnacesOreDict += this::blastFurnace
        waterpower.common.recipe.Recipes.crushers += this::macerator
        waterpower.common.recipe.Recipes.compressors += this::compressor
        waterpower.common.recipe.Recipes.cutters += this::blockcutter
    }

    override fun onPostInit() {
        try {
            val item = ICItemFinder.getItem("dust", "stone")
            val iron = ICItemFinder.getItem("dust", "small_iron")

            if (item == null || iron == null)
                return

            for (o in Ores.values())
                oreWashing(WPItems.crushed.getItemStack(o),
                        WPItems.material.getItemStack(o.material, MaterialForms.dust), iron, item)
        } catch (t: Throwable) {
            t.printStackTrace()
        }

    }
}