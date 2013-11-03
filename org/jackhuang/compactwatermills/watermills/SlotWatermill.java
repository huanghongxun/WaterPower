package org.jackhuang.compactwatermills.watermills;

import org.jackhuang.compactwatermills.rotors.ItemRotor;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWatermill extends Slot {
	
	public SlotWatermill(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public int getSlotStackLimit() {
		return 1;
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		if (itemStack == null) {
			return false;
		}
		if (itemStack.getItem() instanceof ItemRotor) {
			return true;
		}
		return false;
	}
}
