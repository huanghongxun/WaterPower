package org.jackhuang.compactwatermills;

import net.minecraft.inventory.IInventory;

import org.jackhuang.compactwatermills.block.watermills.SlotWatermill;

public class ContainerRotor extends ContainerBase {
	public TileEntityBaseGenerator tileEntity;
	
	public ContainerRotor(IInventory inventory, TileEntityBaseGenerator tileEntityCW) {
		super(inventory);
		tileEntity = tileEntityCW;
		layoutContainer(inventory, tileEntityCW);
	}

	private void layoutContainer(IInventory playerInventory, IInventory inventory) {
		
		//Rotor inventory drawing
		addSlotToContainer(new SlotWatermill(inventory, 0, 80, 26));
		
	}
}
