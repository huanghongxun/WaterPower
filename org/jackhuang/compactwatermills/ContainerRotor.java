package org.jackhuang.compactwatermills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import org.jackhuang.compactwatermills.block.watermills.SlotWatermill;

public class ContainerRotor extends ContainerFullInventory {
	public TileEntityBaseGenerator tileEntity;
	
	public ContainerRotor(EntityPlayer player, TileEntityBaseGenerator tileEntityCW) {
		super(player, tileEntityCW, 166);
		tileEntity = tileEntityCW;
		layoutContainer(tileEntityCW);
	}

	private void layoutContainer(IInventory inventory) {
		
		//Rotor inventory drawing
		addSlotToContainer(new SlotWatermill(inventory, 0, 80, 26));
		
	}
}
