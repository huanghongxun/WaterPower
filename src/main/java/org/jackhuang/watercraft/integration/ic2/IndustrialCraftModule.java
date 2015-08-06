package org.jackhuang.watercraft.integration.ic2;

import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;

import org.jackhuang.watercraft.integration.BaseModule;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.WPLog;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class IndustrialCraftModule extends BaseModule {
    
    public static boolean IS_INDUSTRIAL_CRAFT_RECIPES_AVAILABLE = false;
    public static Object macerator, compressor, cutter;
    
    public static void compressor(ItemStack input, ItemStack output) {
        Recipes.compressor.addRecipe(new RecipeInputItemStack(input), null,
                output);
    }
    
    public static void blastfurance(ItemStack input, ItemStack output) {
        Recipes.blastfurance.addRecipe(new RecipeInputItemStack(input), null,
                output);
    }
    
    public static void macerator(ItemStack input, ItemStack output) {
        Recipes.macerator.addRecipe(new RecipeInputItemStack(input), null,
                output);
    }
    
    public static void metalformerRolling(ItemStack input, ItemStack output) {
        Recipes.metalformerRolling.addRecipe(new RecipeInputItemStack(input), null,
                output);
    }
    
    public static void blockcutter(ItemStack input, ItemStack output) {
        Recipes.blockcutter.addRecipe(new RecipeInputItemStack(input), null,
                output);
    }
    
    public static void oreWashing(ItemStack input, ItemStack... output) {
        NBTTagCompound metadata = new NBTTagCompound();
        metadata.setInteger("amount", 1000);
        Recipes.oreWashing.addRecipe(new RecipeInputItemStack(input), metadata,
                output);
    }
    
    @Override
    public void init() {
        try {
            macerator = Recipes.macerator;
            compressor = Recipes.compressor;
            cutter = Recipes.blockcutter;
            IS_INDUSTRIAL_CRAFT_RECIPES_AVAILABLE = true;
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

}
