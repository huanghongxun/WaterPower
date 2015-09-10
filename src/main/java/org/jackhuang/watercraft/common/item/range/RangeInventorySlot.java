package org.jackhuang.watercraft.common.item.range;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.common.block.inventory.InventorySlot;
import org.jackhuang.watercraft.common.block.inventory.InventorySlot.Access;
import org.jackhuang.watercraft.common.block.tileentity.TileEntityInventory;

public class RangeInventorySlot extends InventorySlot {

    public RangeInventorySlot(TileEntityInventory base) {
        this(base, 1);
    }

    public RangeInventorySlot(TileEntityInventory base, int count) {
        super(base, "range", Access.IO, count);
    }

    @Override
    public boolean accepts(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (itemStack.getItem() instanceof ItemRange) {
            return true;
        }
        return false;
    }
}
