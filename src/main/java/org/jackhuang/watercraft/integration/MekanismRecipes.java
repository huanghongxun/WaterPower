/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.integration;

import java.util.HashMap;

import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.item.ItemStack;

public class MekanismRecipes {
	public static boolean IS_MEKANISM_RECIPES_AVAILABLE = false;
	public static HashMap<ItemStack, ItemStack> compressor = null, crusher;
	
	static {
		if(Mods.Mekanism.isAvailable) {
			try {
				compressor = mekanism.common.recipe.RecipeHandler.Recipe.OSMIUM_COMPRESSOR.get();
				crusher = mekanism.common.recipe.RecipeHandler.Recipe.CRUSHER.get();
				IS_MEKANISM_RECIPES_AVAILABLE = true;
			} catch(Throwable ex) {
				FMLLog.warning("Mekanism have changed its code, please report this warning to the author of WaterPower.");
			}
		}
	}
}
