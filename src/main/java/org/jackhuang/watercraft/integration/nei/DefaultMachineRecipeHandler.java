/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.integration.nei;

import net.minecraft.client.gui.inventory.GuiContainer;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.recipe.IRecipeManager;

public class DefaultMachineRecipeHandler extends MachineRecipeHandler {

    Class<? extends GuiContainer> guiClass;
    String recipeName, recipeId, guiTexture, overlayIdentifier;
    IRecipeManager recipeManager;

    public DefaultMachineRecipeHandler() {
        super();
    }

    public DefaultMachineRecipeHandler(Class<? extends GuiContainer> guiClass, String id, IRecipeManager recipeManager) {
        this(guiClass, id, Reference.ModID + "." + id.toLowerCase(), Reference.ModID + ":textures/gui/GUI" + id + ".png", id.toLowerCase(), recipeManager);
    }

    public DefaultMachineRecipeHandler(Class<? extends GuiContainer> guiClass, String recipeName, String recipeId, String guiTexture, String overlayIdentifier,
            IRecipeManager recipeManager) {
        super();
        this.guiClass = guiClass;
        this.recipeName = recipeName;
        this.recipeId = recipeId;
        this.guiTexture = guiTexture;
        this.overlayIdentifier = overlayIdentifier;
        this.recipeManager = recipeManager;
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass == null ? super.getGuiClass() : guiClass;
    }

    @Override
    public String getRecipeName() {
        return recipeName;
    }

    public IRecipeManager getRecipeManager() {
        return recipeManager;
    }

    public DefaultMachineRecipeHandler setGuiClass(Class<? extends GuiContainer> guiClass) {
        this.guiClass = guiClass;
        return this;
    }

    public DefaultMachineRecipeHandler setRecipeManager(IRecipeManager recipeManager) {
        this.recipeManager = recipeManager;
        return this;
    }

    public DefaultMachineRecipeHandler setRecipeName(String recipeName) {
        this.recipeName = recipeName;
        return this;
    }

    public DefaultMachineRecipeHandler setRecipeId(String recipeId) {
        this.recipeId = recipeId;
        return this;
    }

    public DefaultMachineRecipeHandler setGuiTexture(String guiTexture) {
        this.guiTexture = guiTexture;
        return this;
    }

    public DefaultMachineRecipeHandler setOverlayIdentifier(String overlayIdentifier) {
        this.overlayIdentifier = overlayIdentifier;
        return this;
    }

    @Override
    public String getRecipeId() {
        return recipeId;
    }

    @Override
    public String getGuiTexture() {
        return guiTexture;
    }

    @Override
    public String getOverlayIdentifier() {
        return overlayIdentifier;
    }

    @Override
    public IRecipeManager getRecipeList() {
        return recipeManager;
    }

}
