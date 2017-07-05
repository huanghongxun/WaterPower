/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client.render.item

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.annotations.Init
import waterpower.common.init.WPItems
import waterpower.common.item.ItemColorable

@Init
@SideOnly(Side.CLIENT)
object ItemColor : IItemColor {

    override fun getColorFromItemstack(stack: ItemStack, tintIndex: Int): Int {
        if (stack.item is ItemColorable)
            return (stack.item as ItemColorable).getColorFromItemStack(stack, tintIndex)
        return 0
    }

    @JvmStatic
    @SideOnly(Side.CLIENT)
    fun init() {
        WPItems.items.filter { it is ItemColorable }
                .forEach { Minecraft.getMinecraft().itemColors.registerItemColorHandler(ItemColor, it) }
    }

}
