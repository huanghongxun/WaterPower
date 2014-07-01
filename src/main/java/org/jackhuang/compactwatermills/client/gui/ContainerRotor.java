package org.jackhuang.compactwatermills.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import org.jackhuang.compactwatermills.common.inventory.SlotInventorySlot;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityBaseGenerator;

public class ContainerRotor extends ContainerFullInventory {
	public TileEntityBaseGenerator tileEntity;
	
	public ContainerRotor(EntityPlayer player, TileEntityBaseGenerator tileEntityCW) {
		super(player, tileEntityCW, 166);
		tileEntity = tileEntityCW;
		layoutContainer();
	}

	private void layoutContainer() {
		
		//Rotor inventory drawing
		addSlotToContainer(new SlotInventorySlot(tileEntity.invSlots.get(0), 0, 80, 26));
		
	}
}
