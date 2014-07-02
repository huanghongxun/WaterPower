package org.jackhuang.watercraft.common.inventory;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.RecipeOutput;

import java.util.ArrayList;
import java.util.List;

import org.jackhuang.watercraft.common.tileentity.TileEntityInventory;

import net.minecraft.item.ItemStack;

public class InventorySlotProcessableGeneric extends InventorySlotProcessable {
	public IMachineRecipeManager recipeManager;

	public InventorySlotProcessableGeneric(TileEntityInventory base,
			String name, int count,
			IMachineRecipeManager recipeManager) {
		super(base, name,  count);

		this.recipeManager = recipeManager;
	}

	public boolean accepts(ItemStack itemStack) {
		ItemStack tmp = itemStack.copy();
		tmp.stackSize = 2147483647;

		return getOutputFor(tmp, false, true) != null;
	}

	public RecipeOutput process() {
		ItemStack input = get();
		if ((input == null) && (!allowEmptyInput()))
			return null;

		RecipeOutput output = getOutputFor(input, false, false);
		if (output == null)
			return null;

		List itemsCopy = new ArrayList(output.items.size());

		for (ItemStack itemStack : output.items) {
			itemsCopy.add(itemStack.copy());
		}

		return new RecipeOutput(output.metadata, itemsCopy);
	}

	public void consume() {
		ItemStack input = get();
		if ((input == null) && (!allowEmptyInput()))
			throw new IllegalStateException("consume from empty slot");

		RecipeOutput output = getOutputFor(input, true, false);
		if (output == null)
			throw new IllegalStateException(
					"consume without a processing result");

		if ((input != null) && (input.stackSize <= 0))
			put(null);
	}

	public void setRecipeManager(IMachineRecipeManager recipeManager) {
		this.recipeManager = recipeManager;
	}

	protected RecipeOutput getOutputFor(ItemStack input, boolean adjustInput,
			boolean forAccept) {
		return this.recipeManager.getOutputFor(input, adjustInput);
	}
	
	public RecipeOutput getRecipeOutput() {
		ItemStack input = get();
		if(input == null) return null;
		return getOutputFor(input, false, false);
	}

	protected boolean allowEmptyInput() {
		return false;
	}
}
