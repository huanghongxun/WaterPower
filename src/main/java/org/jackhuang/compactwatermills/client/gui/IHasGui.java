package org.jackhuang.compactwatermills.client.gui;

import net.minecraft.inventory.IInventory;

public abstract interface IHasGui extends IInventory {
	public abstract int getGuiId();
}