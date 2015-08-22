/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.recipe;

import gregtechmod.api.GregTech_API;

import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.integration.RailcraftModule;
import org.jackhuang.watercraft.integration.ThermalExpansionModule;
import org.jackhuang.watercraft.integration.ic2.IndustrialCraftModule;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeAdder {

    public static void lathe(ItemStack input, ItemStack output) {
        MyRecipes.lathe.addRecipe(input, output);
    }

    public static void macerator(ItemStack input, ItemStack output) {
        boolean need = true;
        if (Mods.IndustrialCraft2.isAvailable) {
            need = false;
            IndustrialCraftModule.macerator(input, output);
        }
        if (Mods.ThermalExpansion.isAvailable) {
            ThermalExpansionModule.addPulverizerRecipe(3200, input, output);
        }
        if (need) {
            MyRecipes.macerator.addRecipe(input, output);
        }
    }

    public static void cutter(ItemStack input, ItemStack output) {
        boolean need = true;
        if (need) {
            MyRecipes.cutter.addRecipe(input, output);
        }
    }

    public static void compressor(ItemStack input, ItemStack output) {
        boolean need = true;
        if (Mods.IndustrialCraft2.isAvailable) {
            need = false;
            IndustrialCraftModule.compressor(input, output);
        }
        if (need) {
            MyRecipes.compressor.addRecipe(input, output);
        }
    }

    public static void bender(ItemStack input, ItemStack output) {
        if (Mods.GregTech.isAvailable) {
            GregTech_API.sRecipeAdder.addForgeHammerRecipe(input, output, 20,
                    32);
        }
        if (Mods.IndustrialCraft2.isAvailable) {
            IndustrialCraftModule.metalformerRolling(input, output);
        }
        if (Mods.Railcraft.isAvailable) {
            int sz = input.stackSize;
            input.stackSize = 1;
            switch (sz) {
            case 1:
                RailcraftModule
                        .addRollingMachineRecipe(output, "A", 'A', input);
                break;
            case 2:
                RailcraftModule.addRollingMachineRecipe(output, "AA", 'A',
                        input);
                break;
            case 3:
                RailcraftModule.addRollingMachineRecipe(output, "AAA", 'A',
                        input);
                break;
            case 4:
                RailcraftModule.addRollingMachineRecipe(output, "AA", "AA",
                        'A', input);
                break;
            case 5:
                RailcraftModule.addRollingMachineRecipe(output, "AAA", "AA ",
                        'A', input);
                break;
            case 6:
                RailcraftModule.addRollingMachineRecipe(output, "AAA", "AAA",
                        'A', input);
                break;
            case 7:
                RailcraftModule.addRollingMachineRecipe(output, "AAA", "AAA",
                        "A  ", 'A', input);
                break;
            case 8:
                RailcraftModule.addRollingMachineRecipe(output, "AAA", "AAA",
                        "AA ", 'A', input);
                break;
            case 9:
                RailcraftModule.addRollingMachineRecipe(output, "AAA", "AAA",
                        "AAA", 'A', input);
                break;
            }
        }
        GameRegistry.addShapelessRecipe(output, ItemType.WoodenHammer.item(),
                input);
    }

    public static boolean blastFurnace(ItemStack input, ItemStack output,
            int cookTime) {
        boolean flag = false;
        if (Mods.Railcraft.isAvailable) {
            RailcraftModule.blastFurnace(input, true, false, cookTime, output);
            flag = true;
        }
        if (!flag) {
            IRecipeRegistrar.addSmelting(input, output);
        }
        return flag;
    }

}
