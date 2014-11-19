package org.jackhuang.watercraft.integration;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.RecipeOutput;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.common.recipe.IMyRecipeInput;
import org.jackhuang.watercraft.common.recipe.IMyRecipeInput;
import org.jackhuang.watercraft.common.recipe.IRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipeInputItemStack;
import org.jackhuang.watercraft.common.recipe.MyRecipeInputOreDictionary;
import org.jackhuang.watercraft.common.recipe.MyRecipeOutput;

public class IndustrialCraftRecipeManager implements IRecipeManager {
	
	Object containsIC2Recipe;
	
	public IndustrialCraftRecipeManager(Object ic2RecipeManager) {
		containsIC2Recipe = ic2RecipeManager;
	}

	@Override
	public boolean addRecipe(ItemStack input, ItemStack... outputs) {
		try {
			((IMachineRecipeManager) containsIC2Recipe).addRecipe(new RecipeInputItemStack(input), null, outputs);
			return true;
		} catch(Throwable t) {
			return false;
		}
	}

	@Override
	public MyRecipeOutput getOutput(ItemStack input, boolean adjustInput) {
		try {
			return new MyRecipeOutput(((IMachineRecipeManager) containsIC2Recipe).getOutputFor(input, adjustInput).items);
		} catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<IMyRecipeInput, MyRecipeOutput> getAllRecipes() {
		HashMap<IMyRecipeInput, MyRecipeOutput> map = new HashMap<IMyRecipeInput, MyRecipeOutput>();
		try {
			for(Map.Entry<IRecipeInput, RecipeOutput> entry : ((IMachineRecipeManager) containsIC2Recipe).getRecipes().entrySet()) {
				IMyRecipeInput input;
				IRecipeInput ic2Input = entry.getKey();
				if(ic2Input instanceof RecipeInputItemStack)
					input = new MyRecipeInputItemStack(((RecipeInputItemStack) ic2Input).input);
				else if(ic2Input instanceof RecipeInputOreDict)
					input = new MyRecipeInputOreDictionary(((RecipeInputOreDict)ic2Input).input, ((RecipeInputOreDict)ic2Input).amount, ((RecipeInputOreDict)ic2Input).meta);
				else input = null;
				if(input != null)
					map.put(input, new MyRecipeOutput(entry.getValue().items));
			}
			return map;
		} catch(Throwable t) {
			t.printStackTrace();
			return map;
		}
	}

}
