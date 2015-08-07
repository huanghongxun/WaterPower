package org.jackhuang.watercraft.common.inventory;

import org.jackhuang.watercraft.common.recipe.MyRecipeOutput;
import org.jackhuang.watercraft.common.tileentity.TileEntityInventory;

import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;

public abstract class InventorySlotProcessable extends InventorySlotConsumable {

    public InventorySlotProcessable(TileEntityInventory base, String name,
	    int count) {
	super(base, name, count);
    }

    public abstract boolean accepts(ItemStack paramItemStack);

    public abstract MyRecipeOutput process();

    public abstract void consume();
}
