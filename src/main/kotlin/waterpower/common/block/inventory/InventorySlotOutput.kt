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
import waterpower.util.isStackEqual


class InventorySlotOutput(base: TileEntityInventory, name: String, count: Int) : InventorySlot(base, name, InventorySlot.Access.O, count, InventorySlot.InvSide.BOTTOM) {

    override fun accepts(stack: ItemStack): Boolean {
        return false
    }

    fun add(itemStacks: Collection<ItemStack>): Int {
        return add(itemStacks.toTypedArray(), false)
    }

    fun add(itemStack: ItemStack): Int {
        return add(arrayOf(itemStack), false)
    }

    fun canAdd(itemStacks: Collection<ItemStack>): Boolean {
        return add(itemStacks.toTypedArray(), true) == 0
    }

    fun canAdd(itemStack: ItemStack): Boolean {
        return add(arrayOf(itemStack), true) == 0
    }

    fun add(itemStacks: Array<ItemStack>?, simulate: Boolean): Int {
        if (itemStacks == null)
            return 0

        var totalAmount = 0

        for (itemStack in itemStacks) {
            var amount = itemStack.count

            for (pass in 0..1) {
                for (i in 0..size() - 1) {
                    val existingItemStack: ItemStack = get(i)

                    if (pass == 0 && !existingItemStack.isEmpty) {
                        val space = existingItemStack.maxStackSize - existingItemStack.count

                        if (space > 0 && isStackEqual(itemStack, existingItemStack)) {
                            if (space >= amount) {
                                if (!simulate)
                                    existingItemStack.grow(amount)

                                amount = 0
                            } else {
                                amount -= space

                                if (!simulate)
                                    existingItemStack.grow(space)
                            }
                        }
                    } else if (pass == 1 && existingItemStack.isEmpty) {
                        if (!simulate) {
                            itemStack.count = amount
                            put(i, itemStack)
                        }

                        amount = 0
                    }

                    if (amount == 0)
                        break
                }
                if (amount == 0)
                    break
            }
            totalAmount += amount
        }

        return totalAmount
    }
}