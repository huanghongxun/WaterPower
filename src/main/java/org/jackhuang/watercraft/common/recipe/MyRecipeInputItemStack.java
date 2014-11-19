package org.jackhuang.watercraft.common.recipe;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MyRecipeInputItemStack implements IMyRecipeInput {
	public MyRecipeInputItemStack(ItemStack aInput) {
		this(aInput, aInput.stackSize);
	}

	public MyRecipeInputItemStack(ItemStack input, int amount) {
		this.input = input.copy();
		this.amount = amount;
	}

	@Override
	public boolean matches(ItemStack subject) {
		return subject.getItem() == input.getItem() && (subject.getItemDamage() == input.getItemDamage() || input.getItemDamage() == OreDictionary.WILDCARD_VALUE);
	}

	@Override
	public int getInputAmount() {
		return amount;
	}

	@Override
	public List<ItemStack> getInputs() {
		return Arrays.asList(input);
	}

	public final ItemStack input;
	public final int amount;
}
