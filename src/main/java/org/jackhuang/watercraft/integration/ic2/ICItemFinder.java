package org.jackhuang.watercraft.integration.ic2;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.util.WPLog;

public class ICItemFinder {

    private static Class<?> Ic2Items;

    public static ItemStack getIC2Item(String name) {
        try {
            if (Ic2Items == null)
                Ic2Items = Class.forName("ic2.core.Ic2Items");

            Object ret = Ic2Items.getField(name).get(null);

            if (ret instanceof ItemStack) {
                return (ItemStack) ret;
            }
            return null;
        } catch (Exception e) {
            WPLog.err("IC2Integration: Call getItem failed for " + name);

            return null;
        }
    }
}
