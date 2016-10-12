package waterpower.client.render;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IIconRegister {

	@SideOnly(Side.CLIENT)
    void registerIcons(TextureMap textureMap);

}
