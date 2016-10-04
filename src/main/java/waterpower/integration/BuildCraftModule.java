/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class BuildCraftModule extends BaseModule {
    public static boolean isWrench(EntityPlayer player, ItemStack is, BlockPos pos) {
        /*if (is != null) {
            try {
                if (is.getItem() instanceof IToolWrench) {
                    IToolWrench wrench = (IToolWrench) is.getItem();
                    return wrench.canWrench(player, pos);
                }
            } catch (Throwable t) {
            }
        }*/
        return false;
    }
}