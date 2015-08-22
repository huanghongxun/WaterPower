package cofh.api.item;

import java.util.List;
import net.minecraft.item.ItemStack;

public abstract interface IInventoryContainerItem
{
  public abstract ItemStack insertItem(ItemStack paramItemStack1, ItemStack paramItemStack2, boolean paramBoolean);

  public abstract ItemStack extractItem(ItemStack paramItemStack1, ItemStack paramItemStack2, boolean paramBoolean);

  public abstract ItemStack extractItem(ItemStack paramItemStack, int paramInt, boolean paramBoolean);

  public abstract List<ItemStack> getInventoryContents(ItemStack paramItemStack);

  public abstract int getSizeInventory(ItemStack paramItemStack);
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.item.IInventoryContainerItem
 * JD-Core Version:    0.6.0
 */