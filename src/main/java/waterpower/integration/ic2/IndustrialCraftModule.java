/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.ic2;

import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import waterpower.common.block.ore.OreType;
import waterpower.common.item.GlobalItems;
import waterpower.common.item.crafting.ItemMaterial;
import waterpower.common.item.crafting.MaterialForms;
import waterpower.integration.BaseModule;

public class IndustrialCraftModule extends BaseModule {

	public static boolean IS_INDUSTRIAL_CRAFT_RECIPES_AVAILABLE = false;

	public static void compressor(ItemStack input, ItemStack output) {
		try {
			Recipes.compressor.addRecipe(new RecipeInputItemStack(input), null, false, output);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void blastfurance(ItemStack input, ItemStack output) {
		try {
			Recipes.blastfurnace.addRecipe(new RecipeInputItemStack(input), null, false, output);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void blastfurance(String input, ItemStack output) {
		try {
			Recipes.blastfurnace.addRecipe(new RecipeInputOreDict(input), null, false, output);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void macerator(ItemStack input, ItemStack output) {
		try {
			Recipes.macerator.addRecipe(new RecipeInputItemStack(input), null, false, output);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void metalformerRolling(ItemStack input, ItemStack output) {
		try {
			Recipes.metalformerRolling.addRecipe(new RecipeInputItemStack(input), null, false, output);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void blockcutter(ItemStack input, ItemStack output) {
		try {
			Recipes.blockcutter.addRecipe(new RecipeInputItemStack(input), null, false, output);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void oreWashing(ItemStack input, ItemStack... output) {
		try {
			NBTTagCompound metadata = new NBTTagCompound();
			metadata.setInteger("amount", 1000);
			Recipes.oreWashing.addRecipe(new RecipeInputItemStack(input), metadata, false, output);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static Object getMaceratorMachineManager() {
		try {
			return Recipes.macerator;
		} catch (Throwable t) {
			return null;
		}
	}

	public static Object getCompressorMachineManager() {
		try {
			return Recipes.compressor;
		} catch (Throwable t) {
			return null;
		}
	}

	public static Object getCutterMachineManager() {
		try {
			return Recipes.blockcutter;
		} catch (Throwable t) {
			return null;
		}
	}

	@Override
	public void postInit() {
		try {
			ItemStack item = ICItemFinder.getItem("dust", "stone"), iron = ICItemFinder.getItem("dust", "small_iron");

			for (OreType o : OreType.values())
				IndustrialCraftModule.oreWashing(new ItemStack(GlobalItems.oreDust, 1, o.ordinal()),
						ItemMaterial.get(o.t, MaterialForms.dust), iron, item);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
