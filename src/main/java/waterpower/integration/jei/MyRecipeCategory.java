package waterpower.integration.jei;

import java.awt.Point;
import java.util.List;

import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Ingredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import waterpower.client.Local;
import waterpower.common.recipe.IRecipeManager;

public abstract class MyRecipeCategory implements IRecipeCategory {

	final String uid, title;
	final IRecipeManager recipeManager;

	public MyRecipeCategory(String machineName, String title, IRecipeManager recipeManager) {
		this.uid = machineName;
		this.title = title;
		this.recipeManager = recipeManager;
	}

	@Override
	public String getUid() {
		return this.uid;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
	}

	@Override
	public void drawAnimations(Minecraft minecraft) {
	}

	public abstract List<Point> getInputSlotPositions();
	public abstract List<Point> getOutputSlotPositions();

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
		IIngredients ingredients = new Ingredients();
		recipeWrapper.getIngredients(ingredients);
		setRecipe(recipeLayout, recipeWrapper, ingredients);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		List<ItemStack> inputs = ingredients.getInputs(ItemStack.class).get(0);
		List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class);
		List<Point> inputSlots = getInputSlotPositions(), outputSlots = getOutputSlotPositions();
		itemStacks.init(0, true, 0, 1);
		int i, j = 0;
		for (i = 0; i < inputSlots.size(); i++, j++) {
			Point point = inputSlots.get(i);
			itemStacks.init(j, true, point.x, point.y);
			if (i < inputs.size())
				itemStacks.set(j, inputs.get(i));
		}
		for (i = 0; i < outputSlots.size(); i++, j++) {
			Point point = outputSlots.get(i);
			itemStacks.init(j, false, point.x, point.y);
			if (i < outputs.size())
				itemStacks.set(j, outputs.get(i));
		}
	}
}
