package cofh.api.item;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

public abstract interface IInventoryHandler
{
  public abstract ItemStack insertItem(ForgeDirection paramForgeDirection, ItemStack paramItemStack, boolean paramBoolean);

  public abstract ItemStack extractItem(ForgeDirection paramForgeDirection, ItemStack paramItemStack, boolean paramBoolean);

  public abstract ItemStack extractItem(ForgeDirection paramForgeDirection, int paramInt, boolean paramBoolean);

  public abstract boolean canInterface(ForgeDirection paramForgeDirection);

  public abstract List<ItemStack> getInventoryContents(ForgeDirection paramForgeDirection);

  public abstract int getSizeInventory(ForgeDirection paramForgeDirection);

  public abstract boolean isEmpty(ForgeDirection paramForgeDirection);

  public abstract boolean isFull(ForgeDirection paramForgeDirection);
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.item.IInventoryHandler
 * JD-Core Version:    0.6.0
 */