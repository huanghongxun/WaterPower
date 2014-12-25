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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyRecipeInputItemStack other = (MyRecipeInputItemStack) obj;
		if (amount != other.amount)
			return false;
		if (input == null) {
			if (other.input != null)
				return false;
		} else if (!input.equals(other.input))
			return false;
		return true;
	}
}
