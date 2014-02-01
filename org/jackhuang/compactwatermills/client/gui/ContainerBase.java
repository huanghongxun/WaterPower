package org.jackhuang.compactwatermills.client.gui;

import java.util.ListIterator;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.inventory.SlotInventorySlot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerBase extends Container {
	public final IInventory base;

	public ContainerBase(IInventory base) {
		this.base = base;
	}

	public final ItemStack transferStackInSlot(EntityPlayer player,
			int sourceSlotIndex) {
		/*
		 * ItemStack itemstack = null; Slot slot = (Slot)
		 * inventorySlots.get(sourceSlotIndex);
		 * 
		 * if (slot != null && slot.getHasStack()) { ItemStack itemstack1 =
		 * slot.getStack(); itemstack = itemstack1.copy();
		 * 
		 * if (sourceSlotIndex == 0) { if (! mergeItemStack(itemstack1, 1, 37,
		 * true)) { return null; }
		 * 
		 * slot.onSlotChange(itemstack1, itemstack); } else if (sourceSlotIndex
		 * != 0) { if (((SlotInventorySlot)
		 * inventorySlots.get(0)).isItemValid(itemstack1)) { if (!
		 * mergeItemStack(itemstack1, 0, 1, false)) { return null; } } else if
		 * (sourceSlotIndex >= 1 && sourceSlotIndex < 28) { if (!
		 * mergeItemStack(itemstack1, 28, 37, false)) { return null; } } else if
		 * (sourceSlotIndex >= 28 && sourceSlotIndex < 37 && !
		 * mergeItemStack(itemstack1, 1, 27, false)) { return null; } } else if
		 * (! mergeItemStack(itemstack1, 1, 28, false)) { return null; }
		 * 
		 * if (itemstack1.stackSize == 0) { slot.putStack((ItemStack) null); }
		 * else { slot.onSlotChanged(); }
		 * 
		 * if (itemstack1.stackSize == itemstack.stackSize) { return null; }
		 * 
		 * slot.onPickupFromSlot(player, itemstack1); }
		 * 
		 * return itemstack;
		 */
		Slot sourceSlot = (Slot) this.inventorySlots.get(sourceSlotIndex);

		if ((sourceSlot != null) && (sourceSlot.getHasStack())) {
			ItemStack sourceItemStack = sourceSlot.getStack();
			int oldSourceItemStackSize = sourceItemStack.stackSize;

			if (sourceSlot.inventory == player.inventory) {
				for (int run = 0; (run < 4) && (sourceItemStack.stackSize > 0); run++)
					if (run < 2)
						for (Object o : inventorySlots) {
							Slot targetSlot = (Slot) o;
							if (((targetSlot instanceof SlotInventorySlot))
									&& (((SlotInventorySlot) targetSlot).invSlot
											.canInput())
									&& (targetSlot.isItemValid(sourceItemStack))) {
								if ((targetSlot.getStack() != null)
										|| (run == 1)) {
									mergeItemStack(sourceItemStack,
											targetSlot.slotNumber,
											targetSlot.slotNumber + 1, false);

									if (sourceItemStack.stackSize == 0)
										break;
								}
							}
						}
					else
						for (Object o : this.inventorySlots) {
							Slot targetSlot = (Slot) o;
							if ((targetSlot.inventory != player.inventory)
									&& (targetSlot.isItemValid(sourceItemStack))) {
								if ((targetSlot.getStack() != null)
										|| (run == 3)) {
									mergeItemStack(sourceItemStack,
											targetSlot.slotNumber,
											targetSlot.slotNumber + 1, false);

									if (sourceItemStack.stackSize == 0)
										break;
								}
							}
						}
			} else {
				ListIterator it;
				for (int run = 0; (run < 2) && (sourceItemStack.stackSize > 0); run++) {
					for (it = this.inventorySlots
							.listIterator(this.inventorySlots.size()); it
							.hasPrevious();) {
						Slot targetSlot = (Slot) it.previous();

						if ((targetSlot.inventory == player.inventory)
								&& (targetSlot.isItemValid(sourceItemStack))) {
							if ((targetSlot.getStack() != null) || (run == 1)) {
								mergeItemStack(sourceItemStack,
										targetSlot.slotNumber,
										targetSlot.slotNumber + 1, false);

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

				if (CompactWatermills.isSimulating()) {
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
