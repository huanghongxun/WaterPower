package waterpower.integration.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class MyRecipeHandler implements IRecipeHandler<MyRecipeWrapper> {

	@Override
	public Class<MyRecipeWrapper> getRecipeClass() {
		return MyRecipeWrapper.class;
	}

	@Override
	@Nonnull
	public String getRecipeCategoryUid() {
		return "waterpower";
	}

	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull MyRecipeWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull MyRecipeWrapper recipe) {
		return !recipe.getInputs().isEmpty();
	}

	@Override
	public String getRecipeCategoryUid(MyRecipeWrapper wrapper) {
		return wrapper.category.getUid();
	}

}
