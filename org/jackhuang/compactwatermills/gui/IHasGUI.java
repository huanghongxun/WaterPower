package org.jackhuang.compactwatermills.gui;

import net.minecraft.inventory.IInventory;

public abstract interface IHasGUI extends IInventory {
	public abstract int getGuiId();
}