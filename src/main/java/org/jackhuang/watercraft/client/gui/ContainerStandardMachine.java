package org.jackhuang.watercraft.client.gui;

import net.minecraft.entity.player.EntityPlayer;

import org.jackhuang.watercraft.common.inventory.SlotInventorySlot;
import org.jackhuang.watercraft.common.tileentity.TileEntityStandardWaterMachine;

public class ContainerStandardMachine extends ContainerFullInventory {

    public final TileEntityStandardWaterMachine tileEntity;
    protected int height, inputX, inputY, outputX, outputY, upgradeX, upgradeY, outputNum;

    public ContainerStandardMachine(EntityPlayer entityPlayer,
	    TileEntityStandardWaterMachine tileEntity) {
	this(entityPlayer, tileEntity, 2);
    }

    public ContainerStandardMachine(EntityPlayer entityPlayer,
	    TileEntityStandardWaterMachine tileEntity, int outputNum) {
	this(entityPlayer, tileEntity, 166, 26, 34, 86, 35, 152, 8, outputNum);
    }

    public ContainerStandardMachine(EntityPlayer entityPlayer,
	    TileEntityStandardWaterMachine tileEntity, int height, int inputX,
	    int inputY, int outputX, int outputY, int upgradeX, int upgradeY, int outputNum) {
	super(entityPlayer, tileEntity, height);

	this.tileEntity = tileEntity;
	this.inputX = inputX;
	this.height = height;
	this.inputY = inputY;
	this.outputX = outputX;
	this.outputY = outputY;
	this.upgradeX = upgradeX;
	this.upgradeY = upgradeY;
	this.outputNum = outputNum;

	paintLayout();
    }

    protected void paintLayout() {

	addSlotToContainer(new SlotInventorySlot(tileEntity.inputSlot, 0,
		inputX, inputY));

	for (int i = 0; i < outputNum; i++) {
	    addSlotToContainer(new SlotInventorySlot(tileEntity.outputSlot, i,
		    outputX + i * 18, outputY));
	}

	for (int i = 0; i < 4; i++) {
	    addSlotToContainer(new SlotInventorySlot(tileEntity.upgradeSlot, i,
		    upgradeX, upgradeY + i * 18, 2));
	}
    }
}
