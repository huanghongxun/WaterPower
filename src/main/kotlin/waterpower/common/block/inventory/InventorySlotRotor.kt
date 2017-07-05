/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.inventory

import net.minecraft.item.ItemStack
import waterpower.common.block.tile.TileEntityInventory
import waterpower.common.item.ItemRotor
import waterpower.util.isStackEmpty

class InventorySlotRotor(te: TileEntityInventory, count: Int = 1)
    : InventorySlot(te, "rotor", Access.IO, count) {

    override fun accepts(stack: ItemStack): Boolean {
        if (isStackEmpty(stack)) return false
        return stack.item is ItemRotor
    }
}