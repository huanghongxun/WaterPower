package waterpower.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import waterpower.common.recipe.IMyRecipeInput;
import waterpower.common.recipe.MyRecipeOutput;

public class MyRecipeWrapper extends BlankRecipeWrapper {

	private final Map.Entry<IMyRecipeInput, MyRecipeOutput> container;
	final MyRecipeCategory category;

	private MyRecipeWrapper(Map.Entry<IMyRecipeInput, MyRecipeOutput> container, MyRecipeCategory category) {
		if (container == null)
			throw new NullPointerException();
		this.container = container;
		this.category = category;
	}

	@Override
	@Nonnull
	public List<List<ItemStack>> getInputs() {
		return Collections.singletonList(this.container.getKey().getInputs());
	}

	@Override
	@Nonnull
	public List<ItemStack> getOutputs() {
		return this.container.getValue().items;
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		return this.container.equals(((MyRecipeWrapper) obj).container);
	}

	public static List<MyRecipeWrapper> createRecipes(MyRecipeCategory category) {
		List recipes = new ArrayList();
		for (Map.Entry<IMyRecipeInput, MyRecipeOutput> container : category.recipeManager.getAllRecipes().entrySet()) {
			recipes.add(new MyRecipeWrapper(container, category));
		}
		return recipes;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, getInputs());
		ingredients.setOutputs(ItemStack.class, getOutputs());
	}
}
