package cofh.api.energy;

import net.minecraftforge.common.ForgeDirection;

public abstract interface IEnergyHandler
{
  public abstract int receiveEnergy(ForgeDirection paramForgeDirection, int paramInt, boolean paramBoolean);

  public abstract int extractEnergy(ForgeDirection paramForgeDirection, int paramInt, boolean paramBoolean);

  public abstract boolean canInterface(ForgeDirection paramForgeDirection);

  public abstract int getEnergyStored(ForgeDirection paramForgeDirection);

  public abstract int getMaxEnergyStored(ForgeDirection paramForgeDirection);
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.energy.IEnergyHandler
 * JD-Core Version:    0.6.0
 */