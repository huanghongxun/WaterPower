/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.integration.nei;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.block.machines.*;
import org.jackhuang.watercraft.common.recipe.MyRecipes;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIWaterPowerConfig implements IConfigureNEI {

    @Override
    public String getName() {
        return Reference.ModName;
    }

    @Override
    public String getVersion() {
        return Reference.Version;
    }

    @Override
    public void loadConfig() {
        String id = Reference.ModID;
        new LatheRecipeHandler().registerSelf();
        new MaceratorRecipeHandler().registerSelf();
        new CompressorRecipeHandler().registerSelf();
        new SawmillRecipeHandler().registerSelf();
        new CutterRecipeHandler().registerSelf();
        
        API.registerGuiOverlay(GuiMacerator.class, id + ".macerator", 5, 11);
        API.registerGuiOverlay(GuiCompressor.class, id + ".compressor", 5, 11);
        API.registerGuiOverlay(GuiSawmill.class, id + ".sawmill", 5, 11);
        API.registerGuiOverlay(GuiLathe.class, id + ".lathe", 5, 11);
        API.registerGuiOverlay(GuiCutter.class, id + ".cutter", 5, 11);
    }
}
