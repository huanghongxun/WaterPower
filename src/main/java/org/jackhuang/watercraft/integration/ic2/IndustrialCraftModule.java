package org.jackhuang.watercraft.integration.ic2;

import ic2.api.recipe.Recipes;

import org.jackhuang.watercraft.integration.BaseModule;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.WPLog;

import net.minecraft.item.ItemStack;

public class IndustrialCraftModule extends BaseModule {
    
    public static boolean IS_INDUSTRIAL_CRAFT_RECIPES_AVAILABLE = false;
    public static Object macerator, compressor, cutter;

    private static Class<?> Ic2Items;

    public static ItemStack getIC2Item(String name) {
        try {
            if (Ic2Items == null)
                Ic2Items = Class.forName("ic2.core.Ic2Items");

            Object ret = Ic2Items.getField(name).get(null);

            if (ret instanceof ItemStack) {
                return (ItemStack) ret;
            }
            return null;
        } catch (Exception e) {
            WPLog.err("IC2Integration: Call getItem failed for " + name);

            return null;
        }
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
