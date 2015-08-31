package org.jackhuang.watercraft.common.block.inventory;

import java.util.List;

import org.jackhuang.watercraft.common.block.tileentity.TileEntityInventory;
import org.jackhuang.watercraft.util.StackUtil;

import net.minecraft.item.ItemStack;

public class InventorySlotOutput extends InventorySlot {
    public InventorySlotOutput(TileEntityInventory base, String name,
            int count) {
        super(base, name, InventorySlot.Access.O, count,
                InventorySlot.InvSide.BOTTOM);
    }

    public boolean accepts(ItemStack itemStack) {
        return false;
    }

    public int add(List<ItemStack> itemStacks) {
        return add((ItemStack[]) itemStacks.toArray(new ItemStack[0]), false);
    }

    public int add(ItemStack itemStack) {
        return add(new ItemStack[] { itemStack }, false);
    }

    public boolean canAdd(List<ItemStack> itemStacks) {
        return add((ItemStack[]) itemStacks.toArray(new ItemStack[0]), true) == 0;
    }

    public boolean canAdd(ItemStack itemStack) {
        return add(new ItemStack[] { itemStack }, true) == 0;
    }

    public int add(ItemStack[] itemStacks, boolean simulate) {
        if (itemStacks == null)
            return 0;

        int totalAmount = 0;

        for (ItemStack itemStack : itemStacks) {
            int amount = itemStack.stackSize;

            for (int pass = 0; pass < 2; pass++) {
                for (int i = 0; i < size(); i++) {
                    ItemStack existingItemStack = get(i);

                    if ((pass == 0) && (existingItemStack != null)) {
                        int space = existingItemStack.getMaxStackSize()
                                - existingItemStack.stackSize;

                        if ((space > 0)
                                && (StackUtil.isStackEqual(itemStack,
                                        existingItemStack))) {
                            if (space >= amount) {
                                if (!simulate)
                                    existingItemStack.stackSize += amount;

                                amount = 0;
                            } else {
                                amount -= space;

                                if (!simulate)
                                    existingItemStack.stackSize += space;
                            }
                        }
                    } else if ((pass == 1) && (existingItemStack == null)) {
                        if (!simulate) {
                            itemStack.stackSize = amount;
                            put(i, itemStack);
                        }

                        amount = 0;
                    }

                    if (amount == 0)
                        break;
                }
                if (amount == 0)
                    break;
            }
            totalAmount += amount;
        }

        return totalAmount;
    }
}