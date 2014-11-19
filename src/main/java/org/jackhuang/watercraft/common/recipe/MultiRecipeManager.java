package org.jackhuang.watercraft.common.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

public class MultiRecipeManager implements IRecipeManager {
	ArrayList<IRecipeManager> container = new ArrayList();
	
	public MultiRecipeManager() {
		addRecipeManager(new MyRecipeManager());
	}

	@Override
	public boolean addRecipe(ItemStack input, ItemStack... outputs) {
		for(IRecipeManager r : container) {
			if(r.addRecipe(input, outputs)) return true;
		}
		return false;
	}

	@Override
	public MyRecipeOutput getOutput(ItemStack input, boolean adjustInput) {
		for(IRecipeManager r : container) {
			MyRecipeOutput a = r.getOutput(input, adjustInput);
			if(a != null) return a;
		}
		return null;
	}

	@Override
	public Map<IMyRecipeInput, MyRecipeOutput> getAllRecipes() {
		HashMap map = new HashMap();
		for(IRecipeManager r : container) {
			map.putAll(r.getAllRecipes());
		}
		return null;
	}
	
	public MultiRecipeManager addRecipeManager(IRecipeManager rm) {
		container.add(rm);
		return this;
	}
	
	public MultiRecipeManager addRecipeManager(boolean really, IRecipeManager rm) {
		if(really)
			container.add(rm);
		return this;
	}

}
