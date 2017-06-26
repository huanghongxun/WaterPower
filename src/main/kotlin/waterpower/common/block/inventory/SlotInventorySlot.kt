/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.inventory

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import waterpower.util.copyWithNewCount


class SlotInventorySlot(val invSlot: InventorySlot, val index: Int, xDisplayPosition: Int, yDisplayPosition: Int, val stackLimit: Int = -9999)
    : Slot(invSlot.tileEntity, -1, xDisplayPosition, yDisplayPosition) {

    override fun isItemValid(itemStack: ItemStack): Boolean {
        return this.invSlot.accepts(itemStack)
    }

    override fun getStack() = this.invSlot[this.index]

    override fun putStack(itemStack: ItemStack) {
        this.invSlot.put(this.index, itemStack)
        onSlotChanged()
    }

    override fun decrStackSize(amount: Int): ItemStack {
        val itemStack = this.invSlot[this.index]
        if (itemStack.isEmpty || amount <= 0) return ItemStack.EMPTY

        if (itemStack.count <= amount) {
            this.invSlot.put(this.index, ItemStack.EMPTY)
            onSlotChanged()

            return itemStack
        } else {
            val ret = itemStack.copyWithNewCount(amount)
            itemStack.shrink(amount)
            onSlotChanged()

            return ret
        }
    }

    fun isSlotInInventory(inventory: IInventory, index: Int): Boolean {
        var i = index
        if (inventory !== this.invSlot.tileEntity)
            return false

        for (s in this.invSlot.tileEntity.invSlots) {
            if (i < s.size()) {
                return i == this.index
            }
            i -= s.size()
        }

        return false
    }

    override fun getSlotStackLimit() =
            if (stackLimit == -9999)
                super.getSlotStackLimit()
            else stackLimit

}