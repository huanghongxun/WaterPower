/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
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
