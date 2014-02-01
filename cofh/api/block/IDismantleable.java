package cofh.api.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract interface IDismantleable
{
  public abstract ItemStack dismantleBlock(EntityPlayer paramEntityPlayer, World paramWorld, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);

  public abstract boolean canDismantle(EntityPlayer paramEntityPlayer, World paramWorld, int paramInt1, int paramInt2, int paramInt3);
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.block.IDismantleable
 * JD-Core Version:    0.6.0
 */