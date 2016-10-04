/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.integration.ic2;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IMachineRecipeManager.RecipeIoContainer;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.RecipeOutput;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import waterpower.common.recipe.IMyRecipeInput;
import waterpower.common.recipe.IRecipeManager;
import waterpower.common.recipe.MyRecipeInputItemStack;
import waterpower.common.recipe.MyRecipeInputOreDictionary;
import waterpower.common.recipe.MyRecipeOutput;

public class IndustrialCraftRecipeManager implements IRecipeManager {

    Object containsIC2Recipe;

    public IndustrialCraftRecipeManager(Object ic2RecipeManager) {
        containsIC2Recipe = ic2RecipeManager;
    }

    @Override
    public boolean addRecipe(ItemStack input, ItemStack... outputs) {
        try {
            ((IMachineRecipeManager) containsIC2Recipe).addRecipe(new RecipeInputItemStack(input), null, false, outputs);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public MyRecipeOutput getOutput(ItemStack input, boolean adjustInput) {
        try {
            RecipeOutput r = ((IMachineRecipeManager) containsIC2Recipe).getOutputFor(input, adjustInput);
            if (r == null)
                return null;
            return new MyRecipeOutput(r.items);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<IMyRecipeInput, MyRecipeOutput> getAllRecipes() {
        Map<IMyRecipeInput, MyRecipeOutput> map = new HashMap<IMyRecipeInput, MyRecipeOutput>();
        try {
            for (RecipeIoContainer entry : ((IMachineRecipeManager) containsIC2Recipe).getRecipes()) {
                IMyRecipeInput input;
                IRecipeInput ic2Input = entry.input;
                if (ic2Input instanceof RecipeInputItemStack)
                    input = new MyRecipeInputItemStack(((RecipeInputItemStack) ic2Input).input);
                else if (ic2Input instanceof RecipeInputOreDict)
                    input = new MyRecipeInputOreDictionary(((RecipeInputOreDict) ic2Input).input, ((RecipeInputOreDict) ic2Input).amount,
                            ((RecipeInputOreDict) ic2Input).meta);
                else
                    input = null;
                if (input != null)
                    map.put(input, new MyRecipeOutput(entry.output.items));
            }
            return map;
        } catch (Throwable t) {
            t.printStackTrace();
            return map;
        }
    }

    @Override
    public boolean removeRecipe(ItemStack input) {
        return false;
    }

}
