package org.jackhuang.watercraft.integration;

import java.util.HashMap;

import org.jackhuang.watercraft.util.mods.Mods;

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
