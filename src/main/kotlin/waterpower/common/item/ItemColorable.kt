/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.client.render.IIconContainer
import waterpower.client.render.item.IItemIconContainerProvider


abstract class ItemColorable(id: String) : ItemMeta(id), IItemIconContainerProvider {

    @SideOnly(Side.CLIENT)
    abstract fun getIconContainers(): Array<IIconContainer>

    @SideOnly(Side.CLIENT)
    abstract fun getColorFromItemStack(stack: ItemStack, tintIndex: Int): Int

    @SideOnly(Side.CLIENT)
    override fun getIcon(stack: ItemStack, layer: Int) = getIconContainer(stack)?.getIcon()
}