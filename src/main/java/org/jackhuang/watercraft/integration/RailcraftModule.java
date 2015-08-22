package org.jackhuang.watercraft.integration;

import org.jackhuang.watercraft.util.Mods;

import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.item.ItemStack;

public class RailcraftModule extends BaseModule {
    
    public static void addRollingMachineRecipe(ItemStack output, Object... args) {
        RailcraftCraftingManager.rollingMachine.addRecipe(output, args);
    }
    
    public static void blastFurnace(ItemStack input, boolean matchDamage, boolean matchNBT, int cookTime, ItemStack output) {
        RailcraftCraftingManager.blastFurnace.addRecipe(input, matchDamage, matchNBT, cookTime, output);
    }
}
