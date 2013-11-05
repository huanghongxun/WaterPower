package org.jackhuang.compactwatermills;

import org.jackhuang.compactwatermills.block.watermills.SlotWatermill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerBase extends Container {
	public final IInventory base;

	public ContainerBase(IInventory base) {
		this.base = base;
		
		// Player's inventory drawing
				for (int inventoryRow = 0; inventoryRow < 3; inventoryRow++) {
					for (int inventoryColumn = 0; inventoryColumn < 9; inventoryColumn++) {
						addSlotToContainer(new Slot(base, inventoryColumn
								+ inventoryRow * 9 + 9, 8 + inventoryColumn * 18,
								84 + inventoryRow * 18));
					}
				}

				// Player's tools inventory drawing
				for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
					addSlotToContainer(new Slot(base, hotbarSlot,
							8 + hotbarSlot * 18, 142));
				}
	}

	public final ItemStack transferStackInSlot(EntityPlayer player,
			int sourceSlotIndex) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(sourceSlotIndex);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (sourceSlotIndex == 0) {
				if (! mergeItemStack(itemstack1, 1, 37, true)) {
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (sourceSlotIndex != 0) {
				if (((SlotWatermill) inventorySlots.get(0)).isItemValid(itemstack1)) {
					if (! mergeItemStack(itemstack1, 0, 1, false)) {
						return null;
					}
				}
				else if (sourceSlotIndex >= 1 && sourceSlotIndex < 28) {
					if (! mergeItemStack(itemstack1, 28, 37, false)) {
						return null;
					}
				}
				else if (sourceSlotIndex >= 28 && sourceSlotIndex < 37
					&& ! mergeItemStack(itemstack1, 1, 27, false)) {
					return null;
				}
			}
			else if (! mergeItemStack(itemstack1, 1, 28, false)) {
				return null;
			}
			
			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			}
			else {
				slot.onSlotChanged();
			}
			
			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}
			
			slot.onPickupFromSlot(player, itemstack1);
		}
		
		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

}
