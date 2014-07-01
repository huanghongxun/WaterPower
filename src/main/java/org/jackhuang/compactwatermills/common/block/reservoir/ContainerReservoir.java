package org.jackhuang.compactwatermills.common.block.reservoir;

import net.minecraft.entity.player.EntityPlayer;

import org.jackhuang.compactwatermills.client.gui.ContainerFullInventory;
import org.jackhuang.compactwatermills.common.inventory.SlotInventorySlot;

public class ContainerReservoir extends ContainerFullInventory {
	public TileEntityReservoir tileEntity;
	
	public ContainerReservoir(EntityPlayer player, TileEntityReservoir tileEntityCW) {
		super(player, tileEntityCW, 166);
		tileEntity = tileEntityCW;
		layoutContainer();
	}

	private void layoutContainer() {
		
		// Input slot drawing
		addSlotToContainer(new SlotInventorySlot(tileEntity.getFluidSlot(), 0, 80, 17));
		// Output slot drawing
		addSlotToContainer(new SlotInventorySlot(tileEntity.getOutputSlot(), 0, 80, 53));
		// Upgrade slot drawing
		for (int i = 0; i < 4; i++)
			addSlotToContainer(new SlotInventorySlot(tileEntity.getUpgradeSlot(), i,
					152, 8 + i * 18, 2));
	}
}
