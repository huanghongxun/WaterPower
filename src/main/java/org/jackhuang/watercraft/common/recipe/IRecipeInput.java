package org.jackhuang.watercraft.common.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IRecipeInput {
	boolean matches(ItemStack input);
	
	int getInputAmount();
	
	List<ItemStack> getInputs();
}
