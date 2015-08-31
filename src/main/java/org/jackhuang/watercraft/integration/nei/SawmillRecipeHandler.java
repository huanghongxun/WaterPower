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

public class SawmillRecipeHandler extends MachineRecipeHandler {

    @Override
    public String getRecipeName() {
        return "Water-Powered Sawmill";
    }

    @Override
    public String getRecipeId() {
        return Reference.ModID + ".sawmill";
    }

    @Override
    public String getGuiTexture() {
        return Reference.ModID + ":textures/gui/GUISawmill.png";
    }

    @Override
    public String getOverlayIdentifier() {
        return "sawmill";
    }

    @Override
    public IRecipeManager getRecipeList() {
        return MyRecipes.sawmill;
    }

}
