/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

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
    public boolean removeRecipe(ItemStack input) {
        for(IRecipeManager r : container) {
            if(r.removeRecipe(input)) return true;
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
		return map;
	}
	
	public MultiRecipeManager addRecipeManager(IRecipeManager rm) {
		container.add(rm);
		return this;
	}

}
