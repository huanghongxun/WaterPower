package org.jackhuang.watercraft.integration;

import buildcraft.api.tools.IToolWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BuildCraftIntegration {
    public static boolean isWrench(EntityPlayer player, ItemStack is, int x,
            int y, int z) {
        if (is != null) {
            try {
                if (is.getItem() instanceof IToolWrench) {
                    IToolWrench wrench = (IToolWrench) is.getItem();
                    return wrench.canWrench(player, x, y, z);
                }
            } catch (Throwable t) {

            }
        }
        return false;
    }
}
