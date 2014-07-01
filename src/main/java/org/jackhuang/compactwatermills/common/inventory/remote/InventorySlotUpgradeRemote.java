package org.jackhuang.compactwatermills.common.inventory.remote;

import org.jackhuang.compactwatermills.api.IPluginHandler;
import org.jackhuang.compactwatermills.common.inventory.InventorySlot;
import org.jackhuang.compactwatermills.common.inventory.InventorySlot.Access;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityInventory;

import net.minecraft.item.ItemStack;

public class InventorySlotUpgradeRemote extends InventorySlotRemote {
	public InventorySlotUpgradeRemote(TileEntityInventory base, String name,
			int count) {
		super(base, name, InventorySlot.Access.NONE, count);
	}

	public boolean accepts(ItemStack itemStack) {
		return itemStack.getItem() instanceof IPluginHandler;
	}
}