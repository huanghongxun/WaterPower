package cofh.api.tileentity;

public abstract interface IReconfigurableSides
{
  public abstract boolean decrSide(int paramInt);

  public abstract boolean incrSide(int paramInt);

  public abstract boolean setSide(int paramInt1, int paramInt2);

  public abstract boolean resetSides();

  public abstract int getNumConfig(int paramInt);
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.tileentity.IReconfigurableSides
 * JD-Core Version:    0.6.0
 */