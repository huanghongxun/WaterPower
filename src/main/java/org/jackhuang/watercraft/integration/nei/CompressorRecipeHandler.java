package org.jackhuang.watercraft.integration.nei;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.recipe.IRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipes;

public class CompressorRecipeHandler extends MachineRecipeHandler {

    @Override
    public String getRecipeName() {
	return "Water-Powered Compressor";
    }

    @Override
    public String getRecipeId() {
	return Reference.ModID + ".compressor";
    }

    @Override
    public String getGuiTexture() {
	return Reference.ModID + ":textures/gui/GUICompressor.png";
    }

    @Override
    public String getOverlayIdentifier() {
	return "compressor";
    }

    @Override
    public IRecipeManager getRecipeList() {
	return MyRecipes.compressor;
    }

}
