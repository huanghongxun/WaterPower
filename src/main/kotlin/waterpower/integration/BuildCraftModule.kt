/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration

import buildcraft.api.tools.IToolWrench
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.math.RayTraceResult
import waterpower.annotations.Integration

@Integration(IDs.BuildCraftCore)
object BuildCraftModule : IModule() {
    fun isWrench(player: EntityPlayer, stack: ItemStack, hand: EnumHand, rayTrace: RayTraceResult?): Boolean {
        try {
            if (stack.item is IToolWrench)
                return (stack.item as IToolWrench).canWrench(player, hand, stack, rayTrace)
        } catch(ignore: Throwable) {
        }
        return false
    }
}