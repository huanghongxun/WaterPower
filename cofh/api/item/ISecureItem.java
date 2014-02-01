package cofh.api.item;

import net.minecraft.entity.player.EntityPlayer;

public abstract interface ISecureItem
{
  public abstract boolean canPlayerAccess(EntityPlayer paramEntityPlayer);

  public abstract String getOwnerString();
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.item.ISecureItem
 * JD-Core Version:    0.6.0
 */