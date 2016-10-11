/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration;

import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional.Method;
import waterpower.util.Mods;

public class ImmersiveEngineeringModule extends BaseModule {

    @Method(modid = Mods.IDs.ImmersiveEngineering)
    public static void blastFurnace(Object input, int cookTime, ItemStack output, ItemStack slag) {
        try {
        	BlastFurnaceRecipe.addRecipe(output, input, cookTime, slag);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
