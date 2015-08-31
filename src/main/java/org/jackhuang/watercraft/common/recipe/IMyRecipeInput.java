/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IMyRecipeInput {
    boolean matches(ItemStack input);
    
    int getInputAmount();
    
    List<ItemStack> getInputs();
    
    String getEnderIOXML();
}
