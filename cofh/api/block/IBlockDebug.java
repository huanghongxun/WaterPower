package cofh.api.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public abstract interface IBlockDebug
{
  public abstract void debugBlock(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection, EntityPlayer paramEntityPlayer);
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.block.IBlockDebug
 * JD-Core Version:    0.6.0
 */