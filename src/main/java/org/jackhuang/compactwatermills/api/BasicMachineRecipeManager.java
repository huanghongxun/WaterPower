package org.jackhuang.compactwatermills.api;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class BasicMachineRecipeManager implements IMachineRecipeManager {
	private final Map<IRecipeInput, RecipeOutput> recipes = new HashMap();

	@Override
	public void addRecipe(IRecipeInput input, NBTTagCompound metadata,
			ItemStack... outputs) {
		if (input == null)
			throw new NullPointerException("The recipe input is null");

		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i] != null)
				continue;
			throw new NullPointerException("The output ItemStack #" + i
					+ " is null (counting from 0)");
		}

		for (IRecipeInput existingInput : this.recipes.keySet()) {
			for (ItemStack is : input.getInputs())
				if (existingInput.matches(is))
					throw new RuntimeException("ambiguous recipe: [" + is
							+ " -> " + Arrays.asList(outputs) + "]"
							+ ", conflicts with [" + existingInput.getInputs()
							+ " -> "
							+ ((RecipeOutput) this.recipes.get(input)).items
							+ "]");
		}
		this.recipes.put(input, new RecipeOutput(metadata, outputs));
	}

	@Override
	public RecipeOutput getOutputFor(ItemStack input, boolean adjustInput) {
		if (input == null)
			return null;

		for (Map.Entry entry : this.recipes.entrySet()) {
			IRecipeInput recipeInput = (IRecipeInput) entry.getKey();

			if (recipeInput.matches(input)) {
				if ((input.stackSize < recipeInput.getAmount())
						|| ((input.getItem().hasContainerItem()) && (input.stackSize != recipeInput
								.getAmount())))
					break;
				if (adjustInput) {
					if (input.getItem().hasContainerItem()) {
						ItemStack container = input.getItem()
								.getContainerItem(input);
						
						input = container.copy();
					} else {
						input.stackSize -= recipeInput.getAmount();
					}
				}

				return (RecipeOutput) entry.getValue();
			}

		}

		return null;
	}

	public Map<IRecipeInput, RecipeOutput> getRecipes() {
		return this.recipes;
	}
}
