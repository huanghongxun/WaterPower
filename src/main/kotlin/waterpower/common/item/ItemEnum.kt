/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import waterpower.client.render.IIconRegister
import waterpower.client.render.item.IItemIconProvider
import waterpower.common.INameable

abstract class ItemEnum<T>(id: String, val types: Array<T>) : ItemMeta(id), IItemIconProvider, IIconRegister
where T : Enum<T>, T : INameable {

    init {
        hasSubtypes = true
    }

    override fun getTextureName(meta: Int) =
            if (meta >= types.size) null
            else types[meta].getName()

    override fun getItemStackDisplayName(stack: ItemStack) =
            if (stack.itemDamage >= types.size)
                null
            else
                types[stack.itemDamage].getLocalizedName()

    override fun getUnlocalizedName(stack: ItemStack) =
            if (stack.itemDamage >= types.size)
                null
            else
                types[stack.itemDamage].getUnlocalizedName()

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        super.addInformation(stack, playerIn, tooltip, advanced)
        types[stack.itemDamage].addInformation(tooltip)
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
        for (meta in 0 until types.size)
            subItems.add(getItemStack(meta))
    }

    fun getItemStack(type: T, amount: Int = 1) = getItemStack(type.ordinal, amount)

}
