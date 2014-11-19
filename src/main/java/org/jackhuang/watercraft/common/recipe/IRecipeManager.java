package org.jackhuang.watercraft.common.recipe;

import java.util.Map;

import net.minecraft.item.ItemStack;

public interface IRecipeManager {
	
	boolean addRecipe(ItemStack input, ItemStack... outputs);
	
	MyRecipeOutput getOutput(ItemStack input, boolean adjustInput);
	
	Map<IRecipeInput, MyRecipeOutput> getAllRecipes();

}
