package org.jackhuang.watercraft.integration.nei;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.recipe.IRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipes;

public class CutterRecipeHandler extends MachineRecipeHandler {

    @Override
    public String getRecipeName() {
	return "Water-Powered Cutter";
    }

    @Override
    public String getRecipeId() {
	return Reference.ModID + ".cutter";
    }

    @Override
    public String getGuiTexture() {
	return Reference.ModID + ":textures/gui/GUICutter.png";
    }

    @Override
    public String getOverlayIdentifier() {
	return "cutter";
    }

    @Override
    public IRecipeManager getRecipeList() {
	return MyRecipes.cutter;
    }

}
