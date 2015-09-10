package org.jackhuang.watercraft.client.gui;

import java.util.ListIterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.common.block.inventory.SlotInventorySlot;

public abstract class ContainerBase extends Container {
    public final IInventory base;

    public ContainerBase(IInventory base) {
        this.base = base;
    }

    public final ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex) {
        Slot sourceSlot = (Slot) this.inventorySlots.get(sourceSlotIndex);

        if ((sourceSlot != null) && (sourceSlot.getHasStack())) {
            ItemStack sourceItemStack = sourceSlot.getStack();
            int oldSourceItemStackSize = sourceItemStack.stackSize;

            if (sourceSlot.inventory == player.inventory) {
                for (int run = 0; (run < 4) && (sourceItemStack.stackSize > 0); run++)
                    if (run < 2)
                        for (Object o : inventorySlots) {
                            Slot targetSlot = (Slot) o;
                            if (((targetSlot instanceof SlotInventorySlot)) && (((SlotInventorySlot) targetSlot).invSlot.canInput())
                                    && (targetSlot.isItemValid(sourceItemStack))) {
                                if ((targetSlot.getStack() != null) || (run == 1)) {
                                    mergeItemStack(sourceItemStack, targetSlot.slotNumber, targetSlot.slotNumber + 1, false);

                                    if (sourceItemStack.stackSize == 0)
                                        break;
                                }
                            }
                        }
                    else
                        for (Object o : this.inventorySlots) {
                            Slot targetSlot = (Slot) o;
                            if ((targetSlot.inventory != player.inventory) && (targetSlot.isItemValid(sourceItemStack))) {
                                if ((targetSlot.getStack() != null) || (run == 3)) {
                                    mergeItemStack(sourceItemStack, targetSlot.slotNumber, targetSlot.slotNumber + 1, false);

                                    if (sourceItemStack.stackSize == 0)
                                        break;
                                }
                            }
                        }
            } else {
                ListIterator it;
                for (int run = 0; (run < 2) && (sourceItemStack.stackSize > 0); run++) {
                    for (it = this.inventorySlots.listIterator(this.inventorySlots.size()); it.hasPrevious();) {
                        Slot targetSlot = (Slot) it.previous();

                        if ((targetSlot.inventory == player.inventory) && (targetSlot.isItemValid(sourceItemStack))) {
                            if ((targetSlot.getStack() != null) || (run == 1)) {
                                mergeItemStack(sourceItemStack, targetSlot.slotNumber, targetSlot.slotNumber + 1, false);

                                if (sourceItemStack.stackSize == 0)
                                    break;
                            }
                        }
                    }
                }
            }
            if (sourceItemStack.stackSize != oldSourceItemStackSize) {
                if (sourceItemStack.stackSize == 0)
                    sourceSlot.putStack(null);
                else {
                    sourceSlot.onPickupFromSlot(player, sourceItemStack);
                }

                if (WaterPower.isServerSide()) {
                    detectAndSendChanges();
                }
            }
        }

        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }
}
