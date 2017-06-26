/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.inventory

import net.minecraft.item.ItemStack
import waterpower.api.IUpgrade
import waterpower.common.block.tile.TileEntityInventory

class InventorySlotUpgrade(base: TileEntityInventory, name: String, count: Int)
    : InventorySlot(base, name, InventorySlot.Access.NONE, count) {

    override fun accepts(stack: ItemStack): Boolean {
        return stack.item is IUpgrade
    }
}