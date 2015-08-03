package org.jackhuang.watercraft.common.block;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IDroppable {
    public ItemStack getDroppedItemStack();
}
