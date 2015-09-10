package org.jackhuang.watercraft.common.block.inventory;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.common.block.tileentity.TileEntityInventory;
import org.jackhuang.watercraft.common.recipe.MyRecipeOutput;

public abstract class InventorySlotProcessable extends InventorySlotConsumable {
    public InventorySlotProcessable(TileEntityInventory base, String name, int count) {
        super(base, name, count);
    }

    public abstract boolean accepts(ItemStack paramItemStack);

    public abstract MyRecipeOutput process();

    public abstract void consume();
}
