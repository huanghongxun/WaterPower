package org.jackhuang.watercraft.common.item.rotors;

import org.jackhuang.watercraft.common.block.inventory.InventorySlot;
import org.jackhuang.watercraft.common.block.inventory.InventorySlot.Access;
import org.jackhuang.watercraft.common.block.tileentity.TileEntityInventory;

import net.minecraft.item.ItemStack;

public class RotorInventorySlot extends InventorySlot {

    public RotorInventorySlot(TileEntityInventory base) {
        this(base, 1);
    }
    
    public RotorInventorySlot(TileEntityInventory base, int count) {
        super(base, "rotor", Access.IO, count);
    }
    
    @Override
    public boolean accepts(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (itemStack.getItem() instanceof ItemRotor) {
            return true;
        }
        return false;
    }
}
