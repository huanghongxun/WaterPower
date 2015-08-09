package org.jackhuang.watercraft.integration.minetweaker;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.common.recipe.IRecipeManager;

public class AddRecipeAction extends OneWayAction {
    
    IRecipeManager recipeManager;
    ItemStack in, out;
    String name;
    
    public AddRecipeAction(String name, IRecipeManager recipeManager, ItemStack input, ItemStack output) {
        this.recipeManager = recipeManager;
        this.name = name;
        in = input; out = output;
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
