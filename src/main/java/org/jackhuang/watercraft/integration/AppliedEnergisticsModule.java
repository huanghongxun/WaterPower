package org.jackhuang.watercraft.integration;

import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class AppliedEnergisticsModule extends BaseModule {

    public static void crusher(ItemStack in, ItemStack out) {
        NBTTagCompound tag = new NBTTagCompound();
        
        NBTTagCompound inTag = new NBTTagCompound();
        in.writeToNBT(inTag);
        tag.setTag("in", inTag);
        
        NBTTagCompound outTag = new NBTTagCompound();
        out.writeToNBT(outTag);
        tag.setTag("out", outTag);
        
        tag.setInteger("turns", 7);
        FMLInterModComms.sendMessage(Mods.IDs.AppliedEnergistics2, "add-grindable", tag);
    }
}
