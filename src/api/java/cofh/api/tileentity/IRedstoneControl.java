package cofh.api.tileentity;

public abstract interface IRedstoneControl
{
  public abstract boolean getControlDisable();

  public abstract boolean getControlSetting();

  public abstract boolean setControlDisable(boolean paramBoolean);

  public abstract boolean setControlSetting(boolean paramBoolean);

  public abstract boolean setRedstoneConfig(boolean paramBoolean1, boolean paramBoolean2);

  public abstract boolean isPowered();

  public abstract void handlePowerUpdate(boolean paramBoolean);

  public abstract void handleConfigUpdate(boolean paramBoolean1, boolean paramBoolean2);
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.tileentity.IRedstoneControl
 * JD-Core Version:    0.6.0
 */