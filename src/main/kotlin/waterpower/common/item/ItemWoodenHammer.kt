/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.LoaderState
import waterpower.WaterPower
import waterpower.annotations.NewInstance
import waterpower.client.i18n
import waterpower.client.render.IIconRegister
import waterpower.client.render.item.IItemIconProvider
import waterpower.common.init.WPItems

@NewInstance(LoaderState.ModState.PREINITIALIZED)
class ItemWoodenHammer() : ItemTool("wooden_hammer", 20), IItemIconProvider, IIconRegister {

    init {
        unlocalizedName = "waterpower.crafting.wooden_hammer"

        WPItems.hammer = this
        WPItems.items += this
    }

    lateinit var icon: TextureAtlasSprite

    override fun registerIcons(textureMap: TextureMap) {
        icon = textureMap.registerSprite(ResourceLocation("${WaterPower.MOD_ID}:items/wooden_hammer"))
    }

    override fun getIcon(stack: ItemStack, layer: Int) = icon

    override fun getItemStackDisplayName(stack: ItemStack?)
            = i18n("waterpower.crafting.wooden_hammer")
}