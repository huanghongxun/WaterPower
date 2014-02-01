package cofh.api.tileentity;

public abstract interface IReconfigurableFacing
{
  public abstract int getFacing();

  public abstract boolean allowYAxisFacing();

  public abstract boolean rotateBlock();

  public abstract boolean setFacing(int paramInt);
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.tileentity.IReconfigurableFacing
 * JD-Core Version:    0.6.0
 */