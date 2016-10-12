package waterpower.integration.jei;

import java.util.List;

import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import waterpower.common.recipe.IRecipeManager;

public abstract class MyRecipeCategory implements IRecipeCategory {

	final String machineName;
	final IRecipeManager recipeManager;

	public MyRecipeCategory(String machineName, IRecipeManager recipeManager) {
		this.machineName = machineName;
		this.recipeManager = recipeManager;
	}

	@Override
	public String getUid() {
		return this.machineName;
	}

	@Override
	public String getTitle() {
		return I18n.format("waterpower." + this.machineName + ".gui.name");
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
	}

	@Override
	public void drawAnimations(Minecraft minecraft) {
	}

	public abstract boolean horizontalOutput();

	public abstract int getAmountOutputSlots();

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		itemStacks.init(0, true, 0, 1);
		int startX = 60;
		int startY = 19;
		for (int i = 0; i < getAmountOutputSlots(); i++) {
			itemStacks.init(1 + i, false, horizontalOutput() ? startX + 18 * i : startX,
					horizontalOutput() ? startY : startY + 18 * i);
		}
		if ((recipeWrapper instanceof MyRecipeWrapper)) {
			MyRecipeWrapper wrapper = (MyRecipeWrapper) recipeWrapper;
			itemStacks.setFromRecipe(0, wrapper.getInputs().get(0));
			for (int i = 0; (i < getAmountOutputSlots()) && (i < wrapper.getOutputs().size()); i++)
				itemStacks.setFromRecipe(1 + i, wrapper.getOutputs().get(i));
		}
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		itemStacks.init(0, true, 0, 1);
		int startX = 60;
		int startY = 19;
		for (int i = 0; i < getAmountOutputSlots(); i++) {
			itemStacks.init(1 + i, false, horizontalOutput() ? startX + 18 * i : startX,
					horizontalOutput() ? startY : startY + 18 * i);
		}
		List<ItemStack> inputs = ingredients.getInputs(ItemStack.class).get(0);
		List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class);
		if ((recipeWrapper instanceof MyRecipeWrapper)) {
			MyRecipeWrapper wrapper = (MyRecipeWrapper) recipeWrapper;
			itemStacks.setFromRecipe(0, inputs);
			for (int i = 0; (i < getAmountOutputSlots()) && (i < wrapper.getOutputs().size()); i++)
				itemStacks.setFromRecipe(1 + i, outputs.get(i));
		}
	}
}
