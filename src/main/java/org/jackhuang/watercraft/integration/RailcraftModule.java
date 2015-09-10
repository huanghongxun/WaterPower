/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.integration;

import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.common.event.FMLInterModComms;

public class RailcraftModule extends BaseModule {

    @Method(modid = Mods.IDs.Railcraft)
    public static void rollingMachine(ItemStack output, Object... args) {
        for (Object o : args)
            if (o == null)
                return;
        if (output == null)
            return;
        RailcraftCraftingManager.rollingMachine.addRecipe(output, args);
    }

    @Method(modid = Mods.IDs.Railcraft)
    public static void blastFurnace(ItemStack input, boolean matchDamage, boolean matchNBT, int cookTime, ItemStack output) {
        if (input == null || output == null)
            return;
        RailcraftCraftingManager.blastFurnace.addRecipe(input, matchDamage, matchNBT, cookTime, output);
    }

    @Method(modid = Mods.IDs.Railcraft)
    public static void crusher(ItemStack input, boolean matchDamage, boolean matchNBT, ItemStack... output) {
        if (input == null || output == null)
            return;
        NBTTagCompound data = new NBTTagCompound();
        NBTTagCompound in = new NBTTagCompound();
        input.writeToNBT(in);
        data.setTag("input", in);
        data.setBoolean("matchMeta", matchDamage);
        data.setBoolean("matchNBT", matchNBT);
        for (int i = 0; i < output.length; i++) {
            NBTTagCompound out = new NBTTagCompound();
            output[i].writeToNBT(out);
            out.setFloat("chance", 1);
            data.setTag("output" + i, out);
        }

        FMLInterModComms.sendMessage(Mods.Railcraft.id, "rock-crusher", data);
    }
}
