package org.jackhuang.watercraft.common.block.watermills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import org.jackhuang.watercraft.client.gui.ContainerFullInventory;
import org.jackhuang.watercraft.common.inventory.SlotInventorySlot;

public class ContainerWatermill extends ContainerFullInventory {
	public TileEntityWatermill tileEntity;
	
	public ContainerWatermill(EntityPlayer player, TileEntityWatermill tileEntityCW) {
		super(player, tileEntityCW, 166);
		tileEntity = tileEntityCW;
		layoutContainer();
	}

	private void layoutContainer() {
		
		//Rotor inventory drawing
		addSlotToContainer(new SlotInventorySlot(tileEntity.slotRotor, 0, 80, 26));
		//addSlotToContainer(new SlotInventorySlot(tileEntity.slotUpdater, 0, 140, 26));

		for (int i = 0; i < 4; i++)
			addSlotToContainer(new SlotInventorySlot(tileEntity.slotUpdater, i,
					152, 8 + i * 18, 2));
	}
}
