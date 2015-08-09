/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import org.jackhuang.watercraft.common.block.inventory.SlotInventorySlot;
import org.jackhuang.watercraft.common.block.tileentity.TileEntityRotor;

public class ContainerRotor extends ContainerFullInventory {
	public TileEntityRotor tileEntity;
	
	public ContainerRotor(EntityPlayer player, TileEntityRotor tileEntityCW) {
		super(player, tileEntityCW, 166);
		tileEntity = tileEntityCW;
		layoutContainer();
	}

	protected void layoutContainer() {
		
		//Rotor inventory drawing
		addSlotToContainer(new SlotInventorySlot(tileEntity.slotRotor, 0, 80, 26));
		
	}
}
