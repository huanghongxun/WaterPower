/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.recipe;

import static org.jackhuang.watercraft.common.item.crafting.MaterialForms.ingot;
import static org.jackhuang.watercraft.common.item.crafting.MaterialTypes.IndustrialSteel;

import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.item.crafting.CraftingTypes;
import org.jackhuang.watercraft.common.item.crafting.ItemCrafting;
import org.jackhuang.watercraft.common.item.crafting.ItemMaterial;
import org.jackhuang.watercraft.common.item.crafting.LevelTypes;
import org.jackhuang.watercraft.common.item.crafting.MaterialForms;
import org.jackhuang.watercraft.common.item.crafting.MaterialTypes;
import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.integration.MekanismModule;
import org.jackhuang.watercraft.util.Mods;

import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import static org.jackhuang.watercraft.common.item.crafting.CraftingTypes.*;
import static org.jackhuang.watercraft.common.item.crafting.LevelTypes.*;

public class NormalRecipeRegistrar extends EasyRecipeRegistrar {

    public NormalRecipeRegistrar(Configuration c) {
        super(c);
    }

    @Override
    public void registerWatermills() {
        // MK1
        addWatermillRecipe(0, LevelTypes.MK1);

        // MK3
        addWatermillRecipe(2, MK3);

        // MK4
        addWatermillRecipe(3, LevelTypes.MK4);

        // MK5
        addWatermillRecipe(4, LevelTypes.MK5);

        // MK7
        addWatermillRecipe(6, LevelTypes.MK7);
    }

    private void addWatermillRecipe(int meta, LevelTypes level) {
        this.addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, meta), "CBC", "IAT", "SRS", 'C', ItemCrafting.get(CraftingTypes.casing, level), 'I', ItemCrafting.get(CraftingTypes.outputInterface, level), 'A', ItemCrafting.get(CraftingTypes.rotationAxle, level), 'B', ItemCrafting.get(CraftingTypes.paddleBase, level), 'S', ItemCrafting.get(CraftingTypes.stator, level), 'R', ItemCrafting.get(CraftingTypes.rotor, level), 'T', ItemCrafting.get(CraftingTypes.circuit, level));
    }

}
