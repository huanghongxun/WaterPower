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

interface IItemIconProvider {
    @SideOnly(Side.CLIENT)
    fun getIcon(stack: ItemStack, layer: Int): TextureAtlasSprite?

    @SideOnly(Side.CLIENT)
    fun getRenderLayer(stack: ItemStack) = 0

    @SideOnly(Side.CLIENT)
    fun isHandHeld(stack: ItemStack) = false
}