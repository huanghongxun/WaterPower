package org.jackhuang.compactwatermills.common.inventory;

import org.jackhuang.compactwatermills.common.tileentity.TileEntityInventory;

import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;

public abstract class InventorySlotProcessable extends InventorySlotConsumable {
	public InventorySlotProcessable(TileEntityInventory base, String name,
			int count) {
		super(base, name, count);
	}

	public abstract boolean accepts(ItemStack paramItemStack);

	public abstract RecipeOutput process();

	public abstract void consume();
}
