package org.jackhuang.watercraft.integration;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.recipe.IMyRecipeInput;
import org.jackhuang.watercraft.common.recipe.IRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipeOutput;
import org.jackhuang.watercraft.common.recipe.MyRecipes;

import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.EUSlot;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class CraftGuideWaterPowerObject extends CraftGuideAPIObject implements
		RecipeProvider {

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		addMachineRecipes(generator, GlobalBlocks.lathe, MyRecipes.lathe);
		addMachineRecipes(generator, GlobalBlocks.macerator,
				MyRecipes.macerator);
		addMachineRecipes(generator, GlobalBlocks.compressor,
				MyRecipes.compressor);
		addMachineRecipes(generator, GlobalBlocks.cutter, MyRecipes.cutter);
	}

	private void addMachineRecipes(RecipeGenerator generator, ItemStack type,
			IRecipeManager recipeManager) {
		addMachineRecipes(generator, type, type, recipeManager);
	}

	private void addMachineRecipes(RecipeGenerator generator, ItemStack type,
			Object machine, IRecipeManager recipeManager) {
		addMachineRecipes(generator, type, machine, recipeManager, 2, 800);
	}

	private void addMachineRecipes(RecipeGenerator generator, ItemStack type,
			Object machine, IRecipeManager recipeManager, int eut, int totalEU) {
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
				.setConstantPacketSize(eut).setConstantEUValue(-totalEU);

		for (int i = 0; i < maxOutput / 2; i++) {
			recipeSlots[(i * 2 + 1)] = new ItemSlot((columns > 1 ? 41 : 50) + i
					* 18, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT)
					.drawOwnBackground();
			recipeSlots[(i * 2 + 2)] = new ItemSlot((columns > 1 ? 41 : 50) + i
					* 18, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT)
					.drawOwnBackground();
		}

		if ((maxOutput & 1) == 1) {
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
