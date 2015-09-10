/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.integration.ic2;

import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;

import org.jackhuang.watercraft.common.block.ore.OreType;
import org.jackhuang.watercraft.common.item.GlobalItems;
import org.jackhuang.watercraft.common.item.crafting.ItemMaterial;
import org.jackhuang.watercraft.common.item.crafting.MaterialForms;
import org.jackhuang.watercraft.integration.BaseModule;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.WPLog;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class IndustrialCraftModule extends BaseModule {

    public static boolean IS_INDUSTRIAL_CRAFT_RECIPES_AVAILABLE = false;
    public static Object macerator, compressor, cutter;

    public static void compressor(ItemStack input, ItemStack output) {
        Recipes.compressor.addRecipe(new RecipeInputItemStack(input), null, output);
    }

    public static void blastfurance(ItemStack input, ItemStack output) {
        Recipes.blastfurance.addRecipe(new RecipeInputItemStack(input), null, output);
    }

    public static void macerator(ItemStack input, ItemStack output) {
        Recipes.macerator.addRecipe(new RecipeInputItemStack(input), null, output);
    }

    public static void metalformerRolling(ItemStack input, ItemStack output) {
        Recipes.metalformerRolling.addRecipe(new RecipeInputItemStack(input), null, output);
    }

    public static void blockcutter(ItemStack input, ItemStack output) {
        Recipes.blockcutter.addRecipe(new RecipeInputItemStack(input), null, output);
    }

    public static void oreWashing(ItemStack input, ItemStack... output) {
        NBTTagCompound metadata = new NBTTagCompound();
        metadata.setInteger("amount", 1000);
        Recipes.oreWashing.addRecipe(new RecipeInputItemStack(input), metadata, output);
    }

    @Override
    public void init() {
        try {
            macerator = Recipes.macerator;
            compressor = Recipes.compressor;
            cutter = Recipes.blockcutter;
            IS_INDUSTRIAL_CRAFT_RECIPES_AVAILABLE = true;
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void postInit() {
        ItemStack item = ICItemFinder.getIC2Item("stoneDust"), iron = ICItemFinder.getIC2Item("smallIronDust");

        for (OreType o : OreType.values())
            IndustrialCraftModule.oreWashing(new ItemStack(GlobalItems.oreDust, 1, o.ordinal()), ItemMaterial.get(o.t, MaterialForms.dust), iron, item);
    }

}
