package cofh.api.transport;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

public abstract interface IItemConduit
{
  public abstract ItemStack insertItem(ForgeDirection paramForgeDirection, ItemStack paramItemStack, boolean paramBoolean);

  @Deprecated
  public abstract ItemStack sendItems(ItemStack paramItemStack, ForgeDirection paramForgeDirection);
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.transport.IItemConduit
 * JD-Core Version:    0.6.0
 */