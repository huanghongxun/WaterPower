package org.jackhuang.compactwatermills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerFullInventory extends ContainerBase {

	public ContainerFullInventory(EntityPlayer entityPlayer,
			TileEntityInventory base, int height) {
		super(base);

		// Player's inventory drawing
		for (int inventoryRow = 0; inventoryRow < 3; inventoryRow++) {
			for (int inventoryColumn = 0; inventoryColumn < 9; inventoryColumn++) {
				addSlotToContainer(new Slot(entityPlayer.inventory,
						inventoryColumn + inventoryRow * 9 + 9,
						8 + inventoryColumn * 18, 84 + inventoryRow * 18));
			}
		}

		// Player's tools inventory drawing
		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			addSlotToContainer(new Slot(entityPlayer.inventory, hotbarSlot,
					8 + hotbarSlot * 18, 142));
		}
	}

}
