package org.jackhuang.watercraft.integration;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.util.Mods;

import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import cpw.mods.fml.common.Optional.Method;

public class ImmersiveEngineeringModule extends BaseModule {

    @Method(modid = Mods.IDs.ImmersiveEngineering)
    public static void blastFurnace(Object input, int cookTime, ItemStack output) {
        if(output == null || input == null) return;
        try {
            BlastFurnaceRecipe.addRecipe(output, input, cookTime);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}
