package org.jackhuang.compactwatermills.client.gui;

import net.minecraft.entity.player.EntityPlayer;

import org.jackhuang.compactwatermills.block.reservoir.TileEntityReservoir;
import org.jackhuang.compactwatermills.inventory.SlotInventorySlot;

public class ContainerReservoir extends ContainerFullInventory {
	public TileEntityReservoir tileEntity;
	
	public ContainerReservoir(EntityPlayer player, TileEntityReservoir tileEntityCW) {
		super(player, tileEntityCW, 166);
		tileEntity = tileEntityCW;
		layoutContainer();
	}

	private void layoutContainer() {
		
		//Input inventory drawing
		addSlotToContainer(new SlotInventorySlot(tileEntity.getFluidSlot(), 0, 80, 17));
		//Output inventory drawing
		addSlotToContainer(new SlotInventorySlot(tileEntity.getOutputSlot(), 0, 80, 53));
		
	}
}
