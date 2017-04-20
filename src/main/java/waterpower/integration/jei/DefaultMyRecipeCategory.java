package waterpower.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;
import waterpower.Reference;
import waterpower.common.recipe.IRecipeManager;

public class DefaultMyRecipeCategory extends MyRecipeCategory {
	private IDrawable background;

	public DefaultMyRecipeCategory(String machineName, IRecipeManager recipeManager, IGuiHelper guiHelper, ResourceLocation image) {
		super(machineName, recipeManager);
		this.background = guiHelper.createDrawable(image, 55, 15, 85, 56);
	}

	@Override
	public boolean horizontalOutput() {
		return false;
	}

	@Override
	public int getAmountOutputSlots() {
		return 1;
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}
}