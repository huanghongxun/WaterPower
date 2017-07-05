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
import waterpower.util.*


class InventorySlotOutput(base: TileEntityInventory, name: String, count: Int) : InventorySlot(base, name, InventorySlot.Access.O, count, InventorySlot.InvSide.BOTTOM) {

    override fun accepts(stack: ItemStack?): Boolean {
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
            var amount = getCount(itemStack)

            for (pass in 0..1) {
                for (i in 0..size() - 1) {
                    var existingItemStack = get(i)

                    if (pass == 0 && !isStackEmpty(existingItemStack)) {
                        val space = existingItemStack!!.maxStackSize - getCount(existingItemStack)

                        if (space > 0 && isStackEqual(itemStack, existingItemStack)) {
                            if (space >= amount) {
                                if (!simulate)
                                    existingItemStack = grow(existingItemStack)

                                amount = 0
                            } else {
                                amount -= space

                                if (!simulate)
                                    existingItemStack = grow(existingItemStack)
                            }
                        }
                    } else if (pass == 1 && isStackEmpty(existingItemStack)) {
                        if (!simulate)
                            put(i, itemStack.set(amount))

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