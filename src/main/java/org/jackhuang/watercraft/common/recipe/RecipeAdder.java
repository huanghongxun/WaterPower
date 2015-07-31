/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.recipe;

import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import gregtech.api.GregTech_API;

import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.integration.TERecipeAdder;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import mekanism.api.recipe.RecipeHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class RecipeAdder {
	
	public static void lathe(ItemStack input, ItemStack output) {
		MyRecipes.lathe.addRecipe(input, output);
	}
	
	public static void macerator(ItemStack input, ItemStack output) {
		boolean need = true;
		if(Mods.IndustrialCraft2.isAvailable) {
			need = false;
			Recipes.macerator.addRecipe(new RecipeInputItemStack(input), null, output);
		}
		if(Mods.ThermalExpansion.isAvailable) {
			TERecipeAdder.addPulverizerRecipe(3200, input, output);
		}
		if(Mods.Factorization.isAvailable) {
			try {
		    	if(Block.getBlockFromItem(input.getItem()) != Blocks.air)
		    		factorization.oreprocessing.TileEntityGrinder.addRecipe(input, output, 1);
			} catch(Throwable ex) {
				FMLLog.warning("Failed to add pulverization recipe to factorization. Please report this error to https://github.com/huanghongxun/WaterPower/issues!");
			}
		}
		if(Mods.Mekanism.isAvailable) {
			RecipeHelper.addCrusherRecipe(input, output);
		}
		if(need) {
			MyRecipes.macerator.addRecipe(input, output);
		}
	}
	
	public static void cutter(ItemStack input, ItemStack output) {
		boolean need = true;
		if(Mods.IndustrialCraft2.isAvailable) {
			need = false;
			Recipes.blockcutter.addRecipe(new RecipeInputItemStack(input), null, output);
		}
		if(need) {
			MyRecipes.cutter.addRecipe(input, output);
		}
	}
	
	public static void compressor(ItemStack input, ItemStack output) {
		boolean need = true;
		if(Mods.IndustrialCraft2.isAvailable) {
			need = false;
			Recipes.compressor.addRecipe(new RecipeInputItemStack(input), null, output);
		}
		if(need) {
			MyRecipes.compressor.addRecipe(input, output);
		}
	}
	
	public static void bender(ItemStack input, ItemStack output) {
		boolean need = true;
                if(Mods.ExNihilo.isAvailable) {
                    //Incomplete
                    
                }
		if(Mods.GregTech.isAvailable) {
			need = false;
			GregTech_API.sRecipeAdder.addForgeHammerRecipe(input, output, 20, 32);
		}
		if(Mods.IndustrialCraft2.isAvailable) {
			need = false;
			Recipes.metalformerRolling.addRecipe(new RecipeInputItemStack(input), null, output);
		}
		if(need) {
			GameRegistry.addShapelessRecipe(output, ItemType.WoodenHammer.item(), input);
		}
	}

}
