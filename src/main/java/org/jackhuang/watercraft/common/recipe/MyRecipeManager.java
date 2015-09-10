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
import java.util.LinkedList;
import java.util.Map;

import net.minecraft.item.ItemStack;

public class MyRecipeManager implements IRecipeManager {
    private final Map<IMyRecipeInput, MyRecipeOutput> recipes = new HashMap();
    private ArrayList<HashMap<ItemStack, ItemStack>> singleOutputRecipes = new ArrayList();

    @Override
    public boolean addRecipe(ItemStack input, ItemStack... outputs) {
        if (input == null)
            throw new NullPointerException("The recipe input is null");

        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] != null)
                continue;
            throw new NullPointerException("The output ItemStack #" + i + " is null (counting from 0)");
        }

        for (IMyRecipeInput existingInput : this.recipes.keySet())
            if (existingInput.matches(input))
                return false;
        this.recipes.put(new MyRecipeInputItemStack(input), new MyRecipeOutput(outputs));
        return true;
    }

    @Override
    public boolean removeRecipe(ItemStack input) {
        if (input == null)
            throw new NullPointerException("The recipe input is null");

        LinkedList<IMyRecipeInput> r = new LinkedList<IMyRecipeInput>();

        for (IMyRecipeInput existingInput : this.recipes.keySet())
            if (existingInput.matches(input))
                r.add(existingInput);
        for (IMyRecipeInput s : r)
            recipes.remove(s);
        return !r.isEmpty();
    }

    @Override
    public MyRecipeOutput getOutput(ItemStack input, boolean adjustInput) {
        if (input == null)
            return null;

        for (Map.Entry entry : this.recipes.entrySet()) {
            IMyRecipeInput recipeInput = (IMyRecipeInput) entry.getKey();

            if (recipeInput.matches(input)) {
                if ((input.stackSize < recipeInput.getInputAmount())
                        || ((input.getItem().hasContainerItem()) && (input.stackSize != recipeInput.getInputAmount())))
                    break;
                if (adjustInput) {
                    if (input.getItem().hasContainerItem()) {
                        ItemStack container = input.getItem().getContainerItem(input);

                        input = container.copy();
                    } else {
                        input.stackSize -= recipeInput.getInputAmount();
                    }
                }

                return (MyRecipeOutput) entry.getValue();
            }

        }

        return null;
    }

    @Override
    public Map<IMyRecipeInput, MyRecipeOutput> getAllRecipes() {
        return this.recipes;
    }

    public void registerSingleOutputRecipes(HashMap map) {
        if (map == null)
            return;
        this.singleOutputRecipes.add(map);
    }
}
