package waterpower.integration.jei;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

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

	public DefaultMyRecipeCategory(String uid, String title, IRecipeManager recipeManager, IGuiHelper guiHelper, ResourceLocation image) {
		super(uid, title, recipeManager);
		this.background = guiHelper.createDrawable(image, 3, 15, 140, 60);
	}

	@Override
	public List<Point> getInputSlotPositions() {
		return Arrays.asList(new Point(22, 34 - 16));
	}
	
	@Override
	public List<Point> getOutputSlotPositions() {
		return Arrays.asList(new Point(82, 35 - 16));
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}
}