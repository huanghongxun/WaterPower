/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.inventory;

import gregtech.api.util.GT_Recipe;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.ICannerBottleRecipeManager.Input;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.common.tileentity.TileEntityInventory;
import org.jackhuang.watercraft.util.Utils;
import org.objectweb.asm.tree.IntInsnNode;

public class InventorySlotProcessableGreg extends InventorySlotProcessableGeneric {
	public ArrayList<GT_Recipe> recipeName;
	private boolean alwaysAccept = false;

	public InventorySlotProcessableGreg(TileEntityInventory base,
			String name, int count,
			IMachineRecipeManager recipeManager,
			ArrayList<GT_Recipe> getRecipeName) {
		this(base, name, count, recipeManager, getRecipeName, false);
	}
	
	public InventorySlotProcessableGreg(TileEntityInventory base,
			String name, int count,
			IMachineRecipeManager recipeManager,
			ArrayList<GT_Recipe> getRecipeName, boolean alwaysAccept) {
		super(base, name, count, recipeManager);
		
		this.recipeName = getRecipeName;
		this.alwaysAccept = alwaysAccept;
	}

	public boolean accepts(ItemStack itemStack) {
		if(alwaysAccept) return true;
		if(Utils.isITNT(itemStack)) return false;
		ItemStack[] tmp = new ItemStack[contents.length];
		for(int i = 0; i < contents.length; i++) {
			tmp[i] = contents[i];
			if(tmp[i] != null)
				tmp[i].stackSize = 2147483647;
		}
		tmp[0] = itemStack;

		return getOutputFor(tmp[0], false, true) != null || getOutputFor(tmp, false) != null;
	}

	public RecipeOutput process() {
		ItemStack input = get();
		if ((input == null) && (!allowEmptyInput()))
			return null;

		RecipeOutput output = getOutputFor(input, false, false);
		if (output != null) {
			List itemsCopy = new ArrayList(output.items.size());
	
			for (ItemStack itemStack : output.items) {
				if(itemStack == null) continue;
				itemsCopy.add(itemStack.copy());
			}
			return new RecipeOutput(output.metadata, itemsCopy);
		} else {
			GT_Recipe output2 = getOutputFor(contents, false);
			if(output2 != null) {
				List itemsCopy = new ArrayList(output2.mOutputs.length);
				
				for (ItemStack itemStack : output2.mOutputs) {
					if(itemStack == null) continue;
					itemsCopy.add(itemStack.copy());
				}
				return new RecipeOutput(null, itemsCopy);
			}
		}
		return null;
	}

	public void consume() {
		ItemStack input = get();
		if ((input == null) && (!allowEmptyInput()))
			throw new IllegalStateException("consume from empty slot");

		RecipeOutput output = getOutputFor(input, true, false);
		if (output == null) {
			if(getOutputFor(contents, true) == null)
				throw new IllegalStateException(
					"consume without a processing result");
		}

		if ((input != null) && (input.stackSize <= 0))
			put(null);
	}

	protected GT_Recipe getOutputFor(ItemStack[] input, boolean adjustInput) {
		GT_Recipe recipe = GT_Recipe.findEqualRecipe(true, false, 2147483647, recipeName, input);
		if(recipe != null && adjustInput)
			for(int i = 0; i < recipe.mInputs.length; i++)
				input[i].stackSize -= recipe.mInputs[i].stackSize;
		return recipe;
	}
	
	public GT_Recipe getGTRecipeOutput() {
		//ItemStack input = get();
		if(contents == null) return null;
		return getOutputFor(contents, false);
	}
}
