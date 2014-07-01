package org.jackhuang.compactwatermills.common.inventory;

import net.minecraft.item.ItemStack;

import org.jackhuang.compactwatermills.api.IPluginHandler;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityInventory;

public class InventorySlotUpgrade extends InventorySlot {
	public InventorySlotUpgrade(TileEntityInventory base, String name,
			int count) {
		super(base, name, InventorySlot.Access.NONE, count);
	}

	public boolean accepts(ItemStack itemStack) {
		return itemStack.getItem() instanceof IPluginHandler;
	}
}
