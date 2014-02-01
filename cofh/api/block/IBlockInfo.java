package cofh.api.block;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public abstract interface IBlockInfo
{
  public abstract void getBlockInfo(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection, EntityPlayer paramEntityPlayer, List<String> paramList, boolean paramBoolean);
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.block.IBlockInfo
 * JD-Core Version:    0.6.0
 */