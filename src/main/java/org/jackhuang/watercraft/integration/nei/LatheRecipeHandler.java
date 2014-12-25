package org.jackhuang.watercraft.integration.nei;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.recipe.IRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipes;

public class LatheRecipeHandler extends MachineRecipeHandler {

	@Override
	public String getRecipeName() {
		return "Water-Powered Lathe";
	}

	@Override
	public String getRecipeId() {
		return Reference.ModID + ".lathe";
	}

	@Override
	public String getGuiTexture() {
		return Reference.ModID + ":textures/gui/GUILathe.png";
	}

	@Override
	public String getOverlayIdentifier() {
		return "lathe";
	}

	@Override
	public IRecipeManager getRecipeList() {
		return MyRecipes.lathe;
	}

}
