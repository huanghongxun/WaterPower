/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.LoaderState
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import waterpower.annotations.NewInstance
import waterpower.client.i18n
import waterpower.client.render.IIconContainer
import waterpower.client.render.RecolorableTextures
import waterpower.common.block.ore.Ores
import waterpower.common.init.WPItems

@NewInstance(LoaderState.ModState.PREINITIALIZED)
class ItemOreDust() : ItemColorable("crushed") {
    init {
        WPItems.crushed = this
        WPItems.items += this
        setHasSubtypes(true)

        for (ore in Ores.values())
            OreDictionary.registerOre("crushed${ore.material.getName()}", getItemStack(ore))
    }

    fun getItemStack(ore: Ores, amount: Int = 1)
            = getItemStack(ore.ordinal, amount)

    @SideOnly(Side.CLIENT)
    override fun getColorFromItemStack(stack: ItemStack, tintIndex: Int)
            = Ores.values()[stack.itemDamage].material.color

    @SideOnly(Side.CLIENT)
    override fun getIconContainer(stack: ItemStack): IIconContainer?
            = getIconContainers().first()

    @SideOnly(Side.CLIENT)
    override fun getIconContainers(): Array<IIconContainer>
            = RecolorableTextures.CRUSHED

    override fun getItemStackDisplayName(stack: ItemStack)
            = i18n(Ores.values()[stack.itemDamage].getUnlocalizedName()) + " " +
            i18n("waterpower.forms.crushed")

    override fun getUnlocalizedName(stack: ItemStack) =
            if (stack.itemDamage >= Ores.values().size)
                null
            else
                "waterpower.crushed." + Ores.values()[stack.itemDamage].name

    override fun stopScanning(stack: ItemStack)
            = stack.itemDamage >= Ores.values().size

    override fun getTextureFolder() = "ore"
}