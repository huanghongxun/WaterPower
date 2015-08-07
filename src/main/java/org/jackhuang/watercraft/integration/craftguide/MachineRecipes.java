/**
 * Copyright (c) Huang Yuhui, 2014
 *
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.integration.craftguide;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.recipe.IMyRecipeInput;
import org.jackhuang.watercraft.common.recipe.IRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipeOutput;
import org.jackhuang.watercraft.common.recipe.MyRecipes;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.EUSlot;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class MachineRecipes extends CraftGuideAPIObject implements
	RecipeProvider {

    @Override
    public void generateRecipes(RecipeGenerator generator) {
	addMachineRecipes(generator, GlobalBlocks.lathe,
		Arrays.asList(GlobalBlocks.lathe), MyRecipes.lathe);
	addMachineRecipes(generator, GlobalBlocks.sawmill,
		Arrays.asList(GlobalBlocks.sawmill), MyRecipes.sawmill);
	addMachineRecipes(generator, GlobalBlocks.compressor,
		Arrays.asList(GlobalBlocks.compressor), MyRecipes.compressor);
	addMachineRecipes(generator, GlobalBlocks.macerator,
		Arrays.asList(GlobalBlocks.macerator), MyRecipes.macerator);
    }

    private void addMachineRecipes(RecipeGenerator generator, ItemStack type,
	    Object machine, IRecipeManager recipeManager) {
	int maxOutput = 1;

	for (MyRecipeOutput output : recipeManager.getAllRecipes().values()) {
	    maxOutput = Math.max(maxOutput, output.items.size());
	}

	int columns = (maxOutput + 1) / 2;

	Slot[] recipeSlots = new Slot[maxOutput + 3];

	recipeSlots[0] = new ItemSlot(columns > 1 ? 3 : 12, 21, 16, 16, true)
		.drawOwnBackground();
	recipeSlots[1] = new ExtraSlot(columns > 1 ? 23 : 31, 30, 16, 16,
		machine).clickable().showName()
		.setSlotType(SlotType.MACHINE_SLOT);
	recipeSlots[2] = new EUSlot(columns > 1 ? 23 : 31, 12)
		.setConstantPacketSize(2).setConstantEUValue(-800);

	for (int i = 0; i < maxOutput / 2; i++) {
	    recipeSlots[(i * 2 + 3)] = new ItemSlot((columns > 1 ? 41 : 50) + i
		    * 18, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT)
		    .drawOwnBackground();
	    recipeSlots[(i * 2 + 4)] = new ItemSlot((columns > 1 ? 41 : 50) + i
		    * 18, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT)
		    .drawOwnBackground();
	}

	if ((maxOutput & 0x1) == 1) {
	    recipeSlots[(columns * 2 + 1)] = new ItemSlot((columns > 1 ? 23
		    : 32) + columns * 18, 21, 16, 16, true).setSlotType(
			    SlotType.OUTPUT_SLOT).drawOwnBackground();
	}

	RecipeTemplate template = generator.createRecipeTemplate(recipeSlots,
		type);

	for (Map.Entry recipe : recipeManager.getAllRecipes().entrySet()) {
	    Object[] recipeContents = new Object[maxOutput + 3];
	    recipeContents[0] = ((IMyRecipeInput) recipe.getKey()).getInputs();
	    recipeContents[1] = machine;
	    recipeContents[2] = null;
	    List output = ((MyRecipeOutput) recipe.getValue()).items;

	    for (int i = 0; i < Math.min(maxOutput, output.size()); i++) {
		recipeContents[(i + 3)] = output.get(i);
	    }

	    generator.addRecipe(template, recipeContents);
	}
    }

}
