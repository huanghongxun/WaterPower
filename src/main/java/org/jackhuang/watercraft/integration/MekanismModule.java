/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.integration;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInterModComms;

public class MekanismModule extends BaseModule {

    public static void metallurgicInfuser(String infuse, int amount,
            ItemStack input, ItemStack output) {
        NBTTagCompound sendTag = convertToSimpleRecipe(input, output);
        sendTag.setString("infuseType", infuse);
        sendTag.setInteger("infuseAmount", amount);

        FMLInterModComms.sendMessage(Mods.Mekanism.id, "MetallurgicInfuserRecipe", sendTag);
    }

    public static void crusher(ItemStack in, ItemStack out) {
        NBTTagCompound sendTag = convertToSimpleRecipe(in, out);

        FMLInterModComms.sendMessage(Mods.Mekanism.id, "CrusherRecipe", sendTag);
    }

    private static NBTTagCompound convertToSimpleRecipe(ItemStack in, ItemStack out) {
        NBTTagCompound sendTag = new NBTTagCompound();
        NBTTagCompound inputTagDummy = new NBTTagCompound();
        NBTTagCompound outputTagDummy = new NBTTagCompound();

        NBTTagCompound inputTag = in.writeToNBT(inputTagDummy);
        NBTTagCompound outputTag = out.writeToNBT(outputTagDummy);

        sendTag.setTag("input", inputTag);
        sendTag.setTag("output", outputTag);

        return sendTag;
    }
}
