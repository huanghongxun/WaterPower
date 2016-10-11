/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.minetweaker;

import net.minecraft.item.ItemStack;

import waterpower.common.recipe.IRecipeManager;

public class AddRecipeAction extends OneWayAction {

    IRecipeManager recipeManager;
    ItemStack in, out;
    String name;

    public AddRecipeAction(String name, IRecipeManager recipeManager, ItemStack input, ItemStack output) {
        this.recipeManager = recipeManager;
        this.name = name;
        in = input;
        out = output;
    }

    @Override
    public void apply() {
        recipeManager.addRecipe(in, out);
    }

    @Override
    public String describe() {
        return "Adding " + name + " recipe for " + out.getDisplayName();
    }

}
