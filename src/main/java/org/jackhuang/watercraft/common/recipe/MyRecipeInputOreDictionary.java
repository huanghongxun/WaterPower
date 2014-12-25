/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MyRecipeInputOreDictionary implements IMyRecipeInput {
	public final String input;
	public final int amount;
	public final Integer meta;

	public MyRecipeInputOreDictionary(String input1) {
		this(input1, 1);
	}

	public MyRecipeInputOreDictionary(String input1, int amount1) {
		this(input1, amount1, null);
	}

	public MyRecipeInputOreDictionary(String input1, int amount1, Integer meta) {
		this.input = input1;
		this.amount = amount1;
		this.meta = meta;
	}

	@Override
	public boolean matches(ItemStack subject) {
		List<ItemStack> inputs = OreDictionary.getOres(this.input);

		for (ItemStack input1 : inputs) {
			if (input1.getItem() != null) {
				int metaRequired = this.meta == null ? input1.getItemDamage()
						: this.meta.intValue();

				if ((subject.getItem() == input1.getItem())
						&& ((subject.getItemDamage() == metaRequired) || (metaRequired == 32767))) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((meta == null) ? 0 : meta.hashCode());
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
		MyRecipeInputOreDictionary other = (MyRecipeInputOreDictionary) obj;
		if (amount != other.amount)
			return false;
		if (input == null) {
			if (other.input != null)
				return false;
		} else if (!input.equals(other.input))
			return false;
		if (meta == null) {
			if (other.meta != null)
				return false;
		} else if (!meta.equals(other.meta))
			return false;
		return true;
	}

	@Override
	public int getInputAmount() {
		return this.amount;
	}

	@Override
	public List<ItemStack> getInputs() {
		List<ItemStack> ores = OreDictionary.getOres(this.input);
		List ret = new ArrayList(ores.size());

		for (ItemStack stack : ores) {
			if (stack.getItem() != null)
				ret.add(stack);
		}

		return ret;
	}
}
