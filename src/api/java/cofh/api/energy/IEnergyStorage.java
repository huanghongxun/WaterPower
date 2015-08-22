package cofh.api.energy;

public abstract interface IEnergyStorage
{
  public abstract int receiveEnergy(int paramInt, boolean paramBoolean);

  public abstract int extractEnergy(int paramInt, boolean paramBoolean);

  public abstract int getEnergyStored();

  public abstract int getMaxEnergyStored();
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.energy.IEnergyStorage
 * JD-Core Version:    0.6.0
 */