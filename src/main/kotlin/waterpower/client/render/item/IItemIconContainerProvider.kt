/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client.render.item

import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.client.render.IIconContainer

interface IItemIconContainerProvider : IItemIconProvider {

    @SideOnly(Side.CLIENT)
    fun getIconContainer(stack: ItemStack): IIconContainer?

    @SideOnly(Side.CLIENT)
    override fun getIcon(stack: ItemStack, layer: Int): TextureAtlasSprite? {
        val iconContainer = getIconContainer(stack)
        return when (layer) {
            0 -> iconContainer?.getIcon()
            1 -> iconContainer?.getOverlayIcon()
            else -> null
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getRenderLayer(stack: ItemStack) = 1
}