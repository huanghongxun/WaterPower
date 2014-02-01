package cofh.api.core;

public abstract interface IInitializer
{
  public abstract boolean preInit();

  public abstract boolean initialize();

  public abstract boolean postInit();
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.core.IInitializer
 * JD-Core Version:    0.6.0
 */