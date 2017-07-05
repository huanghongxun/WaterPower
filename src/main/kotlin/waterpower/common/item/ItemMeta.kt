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
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.WaterPower
import waterpower.client.render.item.IItemIconContainerProvider

abstract class ItemMeta(id: String) : ItemBase(id) {

    @SideOnly(Side.CLIENT)
    lateinit var textures: Array<TextureAtlasSprite?>

    @SideOnly(Side.CLIENT)
    open fun getTextureName(meta: Int) =
            if (hasSubtypes) getUnlocalizedName(getItemStack(meta))
            else if (meta == 0) getUnlocalizedName()
            else null

    @SideOnly(Side.CLIENT)
    open fun registerIcons(textureMap: TextureMap) {
        val folder = getTextureFolder()
        textures = Array(32768, { meta ->
            val name = getTextureName(meta)
            if (name != null)
                textureMap.registerSprite(ResourceLocation("${WaterPower.MOD_ID}:items/$folder/$name"))
            else
                null
        })
    }

    @SideOnly(Side.CLIENT)
    abstract fun getTextureFolder(): String

    @SideOnly(Side.CLIENT)
    open fun getIcon(stack: ItemStack, layer: Int): TextureAtlasSprite? {
        if (this is IItemIconContainerProvider) {
            val iconContainer = getIconContainer(stack)
            return when (layer) {
                0 -> iconContainer?.getIcon()
                1 -> iconContainer?.getOverlayIcon()
                else -> null
            }
        }
        if (stack.itemDamage < textures.size)
            return textures[stack.itemDamage]
        return if (textures.isEmpty()) null else textures.first()
    }

    override fun getSubItems(tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
        if (!isRightCreativeTab(tab)) return
        for (meta in 0 until 32767) {
            val stack = ItemStack(this, 1, meta)
            if (stopScanning(stack)) break
            if (validStack(stack)) subItems += stack
        }
    }

    open fun stopScanning(stack: ItemStack): Boolean
            = getUnlocalizedName(stack) == null

    open fun validStack(stack: ItemStack): Boolean
            = getUnlocalizedName(stack) != null

    open fun getItemStack(meta: Int, amount: Int = 1)
            = ItemStack(this, amount, meta)
}