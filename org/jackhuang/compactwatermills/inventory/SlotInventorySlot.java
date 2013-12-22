package org.jackhuang.compactwatermills.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInventorySlot extends Slot {
	public final InventorySlot invSlot;
	public final int index;

	public SlotInventorySlot(InventorySlot invSlot, int index,
			int xDisplayPosition, int yDisplayPosition) {
		super(invSlot.base, -1, xDisplayPosition, yDisplayPosition);

		this.invSlot = invSlot;
		this.index = index;
	}

	public boolean isItemValid(ItemStack itemStack) {
		return this.invSlot.accepts(itemStack);
	}

	public ItemStack getStack() {
		return this.invSlot.get(this.index);
	}

	public void putStack(ItemStack itemStack) {
		this.invSlot.put(this.index, itemStack);
		onSlotChanged();
	}

	public ItemStack decrStackSize(int amount) {
		ItemStack itemStack = this.invSlot.get(this.index);
		if (itemStack == null)
			return null;

		if (itemStack.stackSize <= amount) {
			this.invSlot.put(this.index, null);
			onSlotChanged();

			return itemStack;
		}
		ItemStack ret = itemStack.copy();
		ret.stackSize = amount;

		itemStack.stackSize -= amount;
		onSlotChanged();

		return ret;
	}

	public boolean isSlotInInventory(IInventory inventory, int index) {
		if (inventory != this.invSlot.base)
			return false;

		for (InventorySlot invSlot : this.invSlot.base.invSlots) {
			if (index < invSlot.size()) {
				return index == this.index;
			}
			index -= invSlot.size();
		}

		return false;
	}
}