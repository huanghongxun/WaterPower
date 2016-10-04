/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.common.recipe;

import static waterpower.common.item.crafting.LevelTypes.MK3;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import waterpower.common.block.GlobalBlocks;
import waterpower.common.item.crafting.CraftingTypes;
import waterpower.common.item.crafting.ItemCrafting;
import waterpower.common.item.crafting.LevelTypes;

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
        IRecipeRegistrar.addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, meta), "CBC", "IAT", "SRS", 'C', ItemCrafting.get(CraftingTypes.casing, level),
                'I', ItemCrafting.get(CraftingTypes.outputInterface, level), 'A', ItemCrafting.get(CraftingTypes.rotationAxle, level), 'B',
                ItemCrafting.get(CraftingTypes.paddleBase, level), 'S', ItemCrafting.get(CraftingTypes.stator, level), 'R',
                ItemCrafting.get(CraftingTypes.rotor, level), 'T', ItemCrafting.get(CraftingTypes.circuit, level));
    }

}
