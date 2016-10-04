/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.ic2;

import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import waterpower.common.block.ore.OreType;
import waterpower.common.item.GlobalItems;
import waterpower.common.item.crafting.ItemMaterial;
import waterpower.common.item.crafting.MaterialForms;
import waterpower.integration.BaseModule;

public class IndustrialCraftModule extends BaseModule {

    public static boolean IS_INDUSTRIAL_CRAFT_RECIPES_AVAILABLE = false;
    public static Object macerator, compressor, cutter;

    public static void compressor(ItemStack input, ItemStack output) {
        Recipes.compressor.addRecipe(new RecipeInputItemStack(input), null, false, output);
    }

    public static void blastfurance(ItemStack input, ItemStack output) {
        Recipes.blastfurnace.addRecipe(new RecipeInputItemStack(input), null, false, output);
    }

    public static void macerator(ItemStack input, ItemStack output) {
        Recipes.macerator.addRecipe(new RecipeInputItemStack(input), null, false, output);
    }

    public static void metalformerRolling(ItemStack input, ItemStack output) {
        Recipes.metalformerRolling.addRecipe(new RecipeInputItemStack(input), null, false, output);
    }

    public static void blockcutter(ItemStack input, ItemStack output) {
        Recipes.blockcutter.addRecipe(new RecipeInputItemStack(input), null, false, output);
    }

    public static void oreWashing(ItemStack input, ItemStack... output) {
        NBTTagCompound metadata = new NBTTagCompound();
        metadata.setInteger("amount", 1000);
        Recipes.oreWashing.addRecipe(new RecipeInputItemStack(input), metadata, false, output);
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
        ItemStack item = ICItemFinder.getItem("dust", "stone"), iron = ICItemFinder.getItem("dust", "small_iron");

        for (OreType o : OreType.values())
            IndustrialCraftModule.oreWashing(new ItemStack(GlobalItems.oreDust, 1, o.ordinal()), ItemMaterial.get(o.t, MaterialForms.dust), iron, item);
    }

}
