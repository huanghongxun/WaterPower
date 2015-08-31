/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.integration.minetweaker;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.common.recipe.IRecipeManager;

public class RemoveRecipeAction extends OneWayAction {
    
    IRecipeManager recipeManager;
    ItemStack in;
    String name;
    
    public RemoveRecipeAction(String name, IRecipeManager recipeManager, ItemStack input) {
        this.recipeManager = recipeManager;
        this.name = name;
        in = input;
    }

    @Override
    public void apply() {
        recipeManager.removeRecipe(in);
    }

    @Override
    public String describe() {
        return "Removing " + name + " recipe for " + in.getDisplayName(); 
    }

}
