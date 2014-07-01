package org.jackhuang.compactwatermills.common.inventory.remote;

import java.util.List;

import net.minecraft.item.ItemStack;

import org.jackhuang.compactwatermills.common.inventory.InventorySlot;
import org.jackhuang.compactwatermills.common.inventory.InventorySlotOutput;
import org.jackhuang.compactwatermills.common.inventory.InventorySlot.Access;
import org.jackhuang.compactwatermills.common.inventory.InventorySlot.InvSide;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityInventory;
import org.jackhuang.compactwatermills.util.StackUtil;

public class InventorySlotOutputRemote extends InventorySlotRemote {
	public InventorySlotOutputRemote(TileEntityInventory base, String name,
			int count) {
		super(base, name, InventorySlot.Access.O, count,
				InventorySlot.InvSide.BOTTOM);
	}

	public boolean accepts(ItemStack itemStack) {
		return false;
	}

	public int add(List<ItemStack> itemStacks) {
		return add((ItemStack[]) itemStacks.toArray(new ItemStack[0]), false);
	}

	public int add(ItemStack itemStack) {
		return add(new ItemStack[] { itemStack }, false);
	}

	public boolean canAdd(List<ItemStack> itemStacks) {
		return add((ItemStack[]) itemStacks.toArray(new ItemStack[0]), true) == 0;
	}

	public boolean canAdd(ItemStack itemStack) {
		return add(new ItemStack[] { itemStack }, true) == 0;
	}

	private int add(ItemStack[] itemStacks, boolean simulate) {
		if(getParent() instanceof InventorySlotOutput)
			return ((InventorySlotOutput)getParent()).add(itemStacks, simulate);
		return 0;
	}

}
