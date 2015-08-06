package org.jackhuang.watercraft.integration;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.util.Mods;

import blusunrize.immersiveengineering.api.BlastFurnaceRecipe;
import cpw.mods.fml.common.Optional.Method;

public class ImmersiveEngineeringModule extends BaseModule {

    @Method(modid = Mods.IDs.ImmersiveEngineering)
    public static void blastFurnace(Object input, int cookTime, ItemStack output) {
        BlastFurnaceRecipe.addRecipe(output, input, cookTime);
    }
}
