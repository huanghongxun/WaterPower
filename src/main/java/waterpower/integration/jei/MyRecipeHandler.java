package waterpower.integration.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class MyRecipeHandler implements IRecipeHandler<MyRecipeWrapper> {

	private final MyRecipeCategory category;

	public MyRecipeHandler(MyRecipeCategory category) {
		this.category = category;
	}

	@Override
	public Class<MyRecipeWrapper> getRecipeClass() {
		return MyRecipeWrapper.class;
	}

	@Override
	@Nonnull
	public String getRecipeCategoryUid() {
		return this.category.getUid();
	}

	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull MyRecipeWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull MyRecipeWrapper recipe) {
		return recipe.category == this.category;
	}

	@Override
	public String getRecipeCategoryUid(MyRecipeWrapper arg0) {
		return this.category.getUid();
	}

}
