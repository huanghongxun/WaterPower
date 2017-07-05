/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client.render

import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.WaterPower
import waterpower.annotations.Init

@Init
@SideOnly(Side.CLIENT)
object RecolorableTextures {
    val METAL = arrayOf<IIconContainer>(ItemIcons.DUST_TINY, ItemIcons.DUST_SMALL, ItemIcons.DUST, ItemIcons.PLATE, ItemIcons.PLATE_DENSE, ItemIcons.NUGGET, ItemIcons.INGOT, ItemIcons.STICK, ItemIcons.SCREW, ItemIcons.GEAR, ItemIcons.RING)
    val CRUSHED = arrayOf<IIconContainer>(ItemIcons.CRUSHED)
    val CRAFTING = arrayOf<IIconContainer>(ItemIcons.PADDLE_BASE, ItemIcons.DRAINAGE_PLATE, ItemIcons.FIXED_FRAME, ItemIcons.FIXED_TOOL, ItemIcons.ROTATION_AXLE, ItemIcons.OUTPUT_INTERFACE, ItemIcons.ROTOR, ItemIcons.STATOR, ItemIcons.CASING, ItemIcons.CIRCUIT)

    @JvmStatic
    @SideOnly(Side.CLIENT)
    fun preInit() {
        for (i in ItemIcons.values())
            IconRegisterService.items += i
    }

    @SideOnly(Side.CLIENT)
    enum class ItemIcons : IIconContainer, IIconRegister {
        INGOT, PLATE, PLATE_DENSE, NUGGET, BLOCK, STICK, GEAR, DUST, DUST_SMALL, DUST_TINY, SCREW, RING, PADDLE_BASE, DRAINAGE_PLATE, FIXED_FRAME, FIXED_TOOL, ROTATION_AXLE, OUTPUT_INTERFACE, ROTOR, STATOR, CASING, CIRCUIT, CRUSHED;

        protected var itemIcon: TextureAtlasSprite? = null

        override fun registerIcons(textureMap: TextureMap) {
            itemIcon = textureMap.registerSprite(ResourceLocation("${WaterPower.MOD_ID}:items/iconsets/${name.toLowerCase()}"))
        }

        override fun getIcon() = itemIcon
    }
}
