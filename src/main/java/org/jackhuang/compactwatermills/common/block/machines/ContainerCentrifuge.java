package org.jackhuang.compactwatermills.common.block.machines;

import net.minecraft.entity.player.EntityPlayer;

import org.jackhuang.compactwatermills.client.gui.ContainerStandardMachine;
import org.jackhuang.compactwatermills.common.inventory.SlotInventorySlot;

public class ContainerCentrifuge extends ContainerStandardMachine {

	public ContainerCentrifuge(EntityPlayer entityPlayer,
			TileEntityCentrifuge tileEntity) {
		super(entityPlayer, tileEntity);
	}
	
	@Override
	protected void paintLayout() {
		addSlotToContainer(new SlotInventorySlot(tileEntity.inputSlot, 0,
				25, 25));
		addSlotToContainer(new SlotInventorySlot(tileEntity.inputSlot, 1,
				25, 43));
		
		for(int i = 0; i < 2; i++)
			addSlotToContainer(new SlotInventorySlot(tileEntity.outputSlot, i,
					86 + i * 18, 26));
		for(int i = 2; i < 4; i++)
			addSlotToContainer(new SlotInventorySlot(tileEntity.outputSlot, i,
					86 + i * 18, 26+18));
	}

}
