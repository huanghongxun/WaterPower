/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.recipe;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.util.StackUtil;

public class HashMapRecipeManager implements IRecipeManager {
    HashMap<ItemStack, ItemStack> recipes;

    public HashMapRecipeManager(HashMap<ItemStack, ItemStack> recipes) {
        this.recipes = recipes;
    }

    @Override
    public boolean addRecipe(ItemStack input, ItemStack... outputs) {
        for (ItemStack s : recipes.keySet()) {
            if (StackUtil.isStackEqual(s, input))
                return false;
        }
        recipes.put(input, outputs[0]);
        return true;
    }

    @Override
    public MyRecipeOutput getOutput(ItemStack input, boolean adjustInput) {
        for (Map.Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
            if (StackUtil.isStackEqual(entry.getKey(), input)) {
                if (entry.getKey().stackSize > input.stackSize)
                    break;
                if (adjustInput) {
                    input.stackSize -= entry.getKey().stackSize;
                }
                return new MyRecipeOutput(entry.getValue());
            }
        }
        return null;
    }

    @Override
    public Map<IMyRecipeInput, MyRecipeOutput> getAllRecipes() {
        HashMap<IMyRecipeInput, MyRecipeOutput> map = new HashMap<IMyRecipeInput, MyRecipeOutput>();
        for (Map.Entry entry : recipes.entrySet()) {
            if (entry.getKey() instanceof ItemStack)
                map.put(new MyRecipeInputItemStack((ItemStack) entry.getKey()), new MyRecipeOutput((ItemStack) entry.getValue()));
        }
        return map;
    }

    @Override
    public boolean removeRecipe(ItemStack input) {
        LinkedList<ItemStack> l = new LinkedList<ItemStack>();
        for (ItemStack s : recipes.keySet())
            if (StackUtil.isStackEqual(s, input))
                l.add(s);
        for (ItemStack s : l)
            recipes.remove(s);
        return !l.isEmpty();
    }

}
