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
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.LoaderState
import waterpower.WaterPower
import waterpower.annotations.Init
import waterpower.client.i18n
import waterpower.client.render.IIconRegister
import waterpower.client.render.item.IItemIconProvider
import waterpower.common.INameable
import waterpower.common.init.WPItems

class ItemRotor(val rotor: EnumRotor) : ItemBase("rotor.${rotor.name}"), IItemIconProvider, IIconRegister {
    init {
        WPItems.items += this

        setMaxStackSize(1)
        maxDamage = rotor.maxDamage
    }

    lateinit var icon: TextureAtlasSprite
    override fun registerIcons(textureMap: TextureMap) {
        icon = textureMap.registerSprite(ResourceLocation("${WaterPower.MOD_ID}:items/rotor/${rotor.name}"))
    }

    override fun getIcon(stack: ItemStack, layer: Int) = icon

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        if (rotor.isDamageable()) {
            val leftOverTicks = stack.getMaxDamage() - stack.getItemDamage()
            tooltip.add(i18n("waterpower.rotor.remain", leftOverTicks))

            val str = StringBuilder("(")
            str.append(leftOverTicks / 72000).append(' ').append(i18n("waterpower.rotor.hour"))
            str.append(leftOverTicks % 72000 / 1200).append(' ').append(i18n("waterpower.rotor.minute"))
            str.append(leftOverTicks % 1200 / 20).append(' ').append(i18n("waterpower.rotor.second"))
            str.append(").")
            tooltip.add(str.toString())
        } else {
            tooltip.add(i18n("waterpower.rotor.infinite"))
        }
        tooltip.add(i18n("waterpower.rotor.efficiency", (rotor.efficiency * 100).toInt()))
    }

    override fun getItemStackDisplayName(stack: ItemStack) = rotor.getLocalizedName()

    companion object {
        @JvmStatic
        @Init(LoaderState.ModState.PREINITIALIZED)
        fun init() {
            for (type in EnumRotor.values())
                ItemRotor(type)
        }
    }
}

enum class EnumRotor(val efficiency: Double, val maxDamage: Int) : INameable {
    wood(0.125, 120000),
    stone(0.2, 100000),
    lapis(0.55, 300000),
    tin(0.23, 600000),
    copper(0.25, 800000),
    quartz(0.6, 1800000),
    zinc_alloy(0.5, 600000),
    bronze(0.5, 700000),
    iron(0.35, 1000000),
    obsidian(0.45, 2000000),
    steel(0.5, 1500000),
    gold(0.7, 400000),
    manganese_steel(0.65, 1000000),
    diamond(0.8, -1),
    vanadium_steel(1.0, 10000000);

    override fun getName() = name.toLowerCase()
    override fun getUnlocalizedName() = "waterpower.rotor." + name
    override fun getLocalizedName() = i18n("waterpower.reservoir.$name") + ' ' + i18n("waterpower.rotor")
    fun isDamageable() = maxDamage != -1
}