package org.jackhuang.watercraft.common.block.machines;

import net.minecraft.entity.player.EntityPlayer;

import org.jackhuang.watercraft.client.gui.ContainerStandardMachine;
import org.jackhuang.watercraft.common.inventory.SlotInventorySlot;

public class ContainerCentrifuge extends ContainerStandardMachine {

    public ContainerCentrifuge(EntityPlayer entityPlayer,
	    TileEntityCentrifuge tileEntity) {
	super(entityPlayer, tileEntity);
    }

    @Override
    protected void paintLayout() {
	addSlotToContainer(new SlotInventorySlot(tileEntity.inputSlot, 0,
		26, 26));
	addSlotToContainer(new SlotInventorySlot(tileEntity.inputSlot, 1,
		26, 44));

	for (int i = 0; i < 2; i++) {
	    addSlotToContainer(new SlotInventorySlot(tileEntity.outputSlot, i,
		    86 + i * 18, 26));
	}
	for (int i = 2; i < 4; i++) {
	    addSlotToContainer(new SlotInventorySlot(tileEntity.outputSlot, i,
		    86 + (i - 2) * 18, 26 + 18));
	}

	for (int i = 0; i < 4; i++) {
	    addSlotToContainer(new SlotInventorySlot(tileEntity.upgradeSlot, i,
		    upgradeX, upgradeY + i * 18, 4));
	}
    }

}
