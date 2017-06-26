/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import waterpower.common.block.inventory.SlotInventorySlot


abstract class ContainerBase(val inv: IInventory) : Container() {

    protected fun addPlayerInventorySlots(player: EntityPlayer, height: Int) {
        addPlayerInventorySlots(player, 178, height)
    }

    protected fun addPlayerInventorySlots(player: EntityPlayer, width: Int, height: Int) {
        val xStart = (width - 162) / 2
        for (row in 0..2) {
            for (col in 0..8) {
                addSlotToContainer(Slot(player.inventory, col + row * 9 + 9, xStart + col * 18, height + -82 + row * 18))
            }
        }
        for (col in 0..8) {
            addSlotToContainer(Slot(player.inventory, col, xStart + col * 18, height + -24))
        }
    }

    fun transferStackFromPlayerSlot(player: EntityPlayer, sourceStack: ItemStack): ItemStack {
        for (run in 0..3) {
            if (sourceStack.isEmpty) break
            if (run < 2)
                for (targetSlot in inventorySlots) {
                    if (targetSlot is SlotInventorySlot && targetSlot.invSlot.canInput()
                            && targetSlot.isItemValid(sourceStack)) {
                        if (!targetSlot.stack.isEmpty || run == 1) {
                            mergeItemStack(sourceStack, targetSlot.slotNumber, targetSlot.slotNumber + 1, false)

                            if (sourceStack.isEmpty)
                                break
                        }
                    }
                }
            else
                for (targetSlot in inventorySlots) {
                    if (targetSlot.inventory != player.inventory && targetSlot.isItemValid(sourceStack)) {
                        if (!targetSlot.stack.isEmpty || run == 3) {
                            mergeItemStack(sourceStack, targetSlot.slotNumber, targetSlot.slotNumber + 1, false)

                            if (sourceStack.isEmpty)
                                break
                        }
                    }
                }
        }
        return sourceStack
    }

    fun transferStackFromGuiSlotToPlayerSlot(player: EntityPlayer, sourceStack: ItemStack): ItemStack {
        var it: ListIterator<*>
        for (run in 0..1) {
            if (sourceStack.isEmpty) break
            it = this.inventorySlots.listIterator(this.inventorySlots.size)
            while (it.hasPrevious()) {
                val targetSlot = it.previous()

                if (targetSlot.inventory == player.inventory && targetSlot.isItemValid(sourceStack)) {
                    if (!targetSlot.stack.isEmpty || run == 1) {
                        mergeItemStack(sourceStack, targetSlot.slotNumber, targetSlot.slotNumber + 1, false)

                        if (sourceStack.isEmpty)
                            break
                    }
                }
            }
        }
        return sourceStack
    }

    override fun transferStackInSlot(player: EntityPlayer, sourceSlotIndex: Int): ItemStack {
        val sourceSlot = this.inventorySlots[sourceSlotIndex]

        if (sourceSlot != null && sourceSlot.hasStack) {
            val sourceItemStack = sourceSlot.stack
            val oldSourceItemStackSize = sourceItemStack.count
            val resultStack: ItemStack
            if (sourceSlot.inventory == player.inventory) {
                resultStack = transferStackFromPlayerSlot(player, sourceItemStack)
            } else {
                resultStack = transferStackFromGuiSlotToPlayerSlot(player, sourceItemStack)
            }
            if (sourceItemStack.count != oldSourceItemStackSize) {
                sourceSlot.putStack(resultStack)
                sourceSlot.onTake(player, sourceItemStack)

                if (!player.entityWorld.isRemote) {
                    detectAndSendChanges()
                }
            }
        }

        return ItemStack.EMPTY
    }

    override fun canInteractWith(entityplayer: EntityPlayer): Boolean {
        return true
    }
}