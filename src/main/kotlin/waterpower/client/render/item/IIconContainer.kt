/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client.render

import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

interface IIconContainer {
    @SideOnly(Side.CLIENT)
    fun getIcon(): TextureAtlasSprite?

    @SideOnly(Side.CLIENT)
    fun getOverlayIcon(): TextureAtlasSprite? = null
}