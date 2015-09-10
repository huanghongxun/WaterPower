/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.integration;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.Optional.Method;

public class ImmersiveEngineeringModule extends BaseModule {

    public static java.lang.reflect.Method addRecipe;

    @Method(modid = Mods.IDs.ImmersiveEngineering)
    public static void blastFurnace(Object input, int cookTime, ItemStack output) {
        if (addRecipe == null) {
            try {
                addRecipe = Class.forName("blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe").getDeclaredMethod("addRecipe", ItemStack.class, Object.class, int.class);
                addRecipe.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (output == null || input == null)
            return;
        try {
            addRecipe.invoke(null, output, input, cookTime);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
