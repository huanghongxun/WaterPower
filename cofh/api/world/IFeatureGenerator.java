package cofh.api.world;

import java.util.Random;
import net.minecraft.world.World;

public abstract interface IFeatureGenerator
{
  public abstract String getFeatureName();

  public abstract boolean generateFeature(Random paramRandom, int paramInt1, int paramInt2, World paramWorld, boolean paramBoolean);
}

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.world.IFeatureGenerator
 * JD-Core Version:    0.6.0
 */