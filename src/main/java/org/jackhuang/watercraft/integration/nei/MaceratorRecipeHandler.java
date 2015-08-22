package org.jackhuang.watercraft.integration.nei;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.recipe.IRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipes;

public class MaceratorRecipeHandler extends MachineRecipeHandler {

	@Override
	public String getRecipeName() {
		return "Water-Powered Crusher";
	}

	@Override
	public String getRecipeId() {
		return Reference.ModID + ".macerator";
	}

	@Override
	public String getGuiTexture() {
		return Reference.ModID + ":textures/gui/GUIMacerator.png";
	}

	@Override
	public String getOverlayIdentifier() {
		return Reference.ModID + "。macerator";
	}

	@Override
	public IRecipeManager getRecipeList() {
		return MyRecipes.macerator;
	}

}
