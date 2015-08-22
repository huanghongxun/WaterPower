package cofh.api.item;

import java.util.List;
import net.minecraft.item.ItemStack;

public abstract interface ITinkerableItem
{
  public abstract List<ItemStack> getValidTinkers(ItemStack paramItemStack);

  public abstract boolean applyTinker(ItemStack paramItemStack1, ItemStack paramItemStack2);
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.item.ITinkerableItem
 * JD-Core Version:    0.6.0
 */