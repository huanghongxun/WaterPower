package org.jackhuang.watercraft.common.recipe;

import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import gregtech.api.GregTech_API;

import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.integration.thermalexpansion.TERecipeAdder;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import mekanism.api.RecipeHelper;
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
				factorization.oreprocessing.TileEntityGrinder.addRecipe(input, output, 1);
			} catch(Throwable ex) {
				FMLLog.warning("Failed to add pulverization recipe to factorization. Please report this error to the author of WaterPower!");
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
	
	public static void bender(ItemStack input, ItemStack output) {
		boolean need = true;
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
