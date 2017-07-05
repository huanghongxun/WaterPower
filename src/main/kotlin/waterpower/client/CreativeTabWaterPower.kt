/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import waterpower.common.block.watermill.EnumWatermill
import waterpower.common.init.WPBlocks

class CreativeTabWaterPower(label: String) : CreativeTabs(label) {
    override fun getTabIconItem(): Item? = Item.getItemFromBlock(WPBlocks.watermill)

    override fun getIconItemStack(): ItemStack = WPBlocks.watermill.getItemStack(EnumWatermill.MK1)
}