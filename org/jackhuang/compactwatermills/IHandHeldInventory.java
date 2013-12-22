package org.jackhuang.compactwatermills;

import org.jackhuang.compactwatermills.gui.IHasGUI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract interface IHandHeldInventory {
	public abstract IHasGUI getInventory(EntityPlayer paramEntityPlayer,
			ItemStack paramItemStack);
}