package org.jackhuang.watercraft.integration;

import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.Optional.Method;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.item.ItemStack;

public class RailcraftModule extends BaseModule {

    @Method(modid = Mods.IDs.Railcraft)
    public static void addRollingMachineRecipe(ItemStack output, Object... args) {
        for (Object o : args)
            if (o == null)
                return;
        if (output == null) return;
        RailcraftCraftingManager.rollingMachine.addRecipe(output, args);
    }

    @Method(modid = Mods.IDs.Railcraft)
    public static void blastFurnace(ItemStack input, boolean matchDamage,
            boolean matchNBT, int cookTime, ItemStack output) {
        if(input == null || output == null) return;
        RailcraftCraftingManager.blastFurnace.addRecipe(input, matchDamage,
                matchNBT, cookTime, output);
    }
}
