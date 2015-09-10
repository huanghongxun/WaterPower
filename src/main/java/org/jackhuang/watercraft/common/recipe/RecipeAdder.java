/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.recipe;

import gregtech.api.GregTech_API;

import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.integration.AppliedEnergisticsModule;
import org.jackhuang.watercraft.integration.ImmersiveEngineeringModule;
import org.jackhuang.watercraft.integration.MekanismModule;
import org.jackhuang.watercraft.integration.RailcraftModule;
import org.jackhuang.watercraft.integration.ThermalExpansionModule;
import org.jackhuang.watercraft.integration.ic2.IndustrialCraftModule;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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
        if (Mods.Factorization.isAvailable) {
            try {
                if (Block.getBlockFromItem(input.getItem()) != Blocks.air)
                    factorization.oreprocessing.TileEntityGrinder.addRecipe(input, output, 1);
            } catch (Throwable ex) {
                FMLLog.warning("Failed to add pulverization recipe to factorization. Please report this error to https://github.com/huanghongxun/WaterPower/issues!");
            }
        }
        if (Mods.Mekanism.isAvailable) {
            MekanismModule.crusher(input, output);
        }
        if (Mods.Railcraft.isAvailable) {
            RailcraftModule.crusher(input, true, false, output);
        }
        if (Mods.AppliedEnergistics2.isAvailable) {
            AppliedEnergisticsModule.crusher(input, output);
        }
        if (need) {
            MyRecipes.macerator.addRecipe(input, output);
        }
    }

    public static void cutter(ItemStack input, ItemStack output) {
        boolean need = true;
        if (Mods.IndustrialCraft2.isAvailable) {
            need = false;
            IndustrialCraftModule.blockcutter(input, output);
        }
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
        input = input.copy();
        if (Mods.ExNihilo.isAvailable) {
            // Incomplete

        }
        if (Mods.GregTech.isAvailable) {
            GregTech_API.sRecipeAdder.addForgeHammerRecipe(input, output, 20, 32);
        }
        if (Mods.IndustrialCraft2.isAvailable) {
            IndustrialCraftModule.metalformerRolling(input, output);
        }
        if (Mods.Railcraft.isAvailable) {
            int sz = input.stackSize;
            input.stackSize = 1;
            switch (sz) {
            case 1:
                RailcraftModule.rollingMachine(output, "A", 'A', input);
                break;
            case 2:
                RailcraftModule.rollingMachine(output, "AA", 'A', input);
                break;
            case 3:
                RailcraftModule.rollingMachine(output, "AAA", 'A', input);
                break;
            case 4:
                RailcraftModule.rollingMachine(output, "AA", "AA", 'A', input);
                break;
            case 5:
                RailcraftModule.rollingMachine(output, "AAA", "AA ", 'A', input);
                break;
            case 6:
                RailcraftModule.rollingMachine(output, "AAA", "AAA", 'A', input);
                break;
            case 7:
                RailcraftModule.rollingMachine(output, "AAA", "AAA", "A  ", 'A', input);
                break;
            case 8:
                RailcraftModule.rollingMachine(output, "AAA", "AAA", "AA ", 'A', input);
                break;
            case 9:
                RailcraftModule.rollingMachine(output, "AAA", "AAA", "AAA", 'A', input);
                break;
            }
        }
        int sz = input.stackSize;
        input.stackSize = 1;
        switch (sz) {
        case 1:
            GameRegistry.addShapelessRecipe(output, ItemType.WoodenHammer.item(), input);
            break;
        case 2:
            GameRegistry.addShapelessRecipe(output, ItemType.WoodenHammer.item(), input, input);
            break;
        case 3:
            GameRegistry.addShapelessRecipe(output, ItemType.WoodenHammer.item(), input, input, input);
            break;
        case 4:
            GameRegistry.addShapelessRecipe(output, ItemType.WoodenHammer.item(), input, input, input, input);
            break;
        case 5:
            GameRegistry.addShapelessRecipe(output, ItemType.WoodenHammer.item(), input, input, input, input, input);
            break;
        case 6:
            GameRegistry.addShapelessRecipe(output, ItemType.WoodenHammer.item(), input, input, input, input, input, input);
            break;
        case 7:
            GameRegistry.addShapelessRecipe(output, ItemType.WoodenHammer.item(), input, input, input, input, input, input, input);
            break;
        case 8:
            GameRegistry.addShapelessRecipe(output, ItemType.WoodenHammer.item(), input, input, input, input, input, input, input, input);
            break;
        }
    }

    public static boolean blastFurnace(ItemStack input, ItemStack output, int cookTime) {
        boolean flag = false;
        if (Mods.IndustrialCraft2.isAvailable) {
            IndustrialCraftModule.blastfurance(input, output);
            flag = true;
        }
        if (Mods.Railcraft.isAvailable) {
            RailcraftModule.blastFurnace(input, true, false, cookTime, output);
            flag = true;
        }
        if (Mods.ImmersiveEngineering.isAvailable) {
            ImmersiveEngineeringModule.blastFurnace(input, 200, output);
            flag = true;
        }
        if (Mods.Mekanism.isAvailable) {
            MekanismModule.metallurgicInfuser("CARBON", Math.round(((float) cookTime) / 100.0f), input, output);
            flag = true;
        }
        if (!flag) {
            FurnaceRecipes.smelting().func_151394_a(input, output, 0);
        }
        return flag;
    }

}
