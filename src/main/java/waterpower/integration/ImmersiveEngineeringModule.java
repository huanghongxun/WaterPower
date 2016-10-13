/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional.Method;
import waterpower.util.Mods;

public class ImmersiveEngineeringModule extends BaseModule {
	private static java.lang.reflect.Method method = null;

    @Method(modid = Mods.IDs.ImmersiveEngineering)
    public static void blastFurnace(Object input, int cookTime, ItemStack output, ItemStack slag) {
		try {
	    	if (method == null) {
				Class clz = Class.forName("blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe");
	    		method = clz.getMethod("addRecipe", ItemStack.class, Object.class, int.class, ItemStack.class);
	    	}
        	method.invoke(null, output, input, cookTime, slag);
		} catch (Throwable e) {
		}
    }
}
