/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.integration;

import ic2.api.recipe.Recipes;

import org.jackhuang.watercraft.util.Mods;

public class IndustrialCraftRecipes {
	
	public static boolean IS_INDUSTRIAL_CRAFT_RECIPES_AVAILABLE = false;
	public static Object macerator, compressor, cutter;
	
	static {
		if(Mods.IndustrialCraft2.isAvailable)
		try {
			macerator = Recipes.macerator;
			compressor = Recipes.compressor;
			cutter = Recipes.matterAmplifier;
			IS_INDUSTRIAL_CRAFT_RECIPES_AVAILABLE = true;
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

}
