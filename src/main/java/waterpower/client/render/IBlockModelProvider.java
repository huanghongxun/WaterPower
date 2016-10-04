package waterpower.client.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IBlockModelProvider {
    @SideOnly(Side.CLIENT)
    public abstract void registerModels();
}
