package waterpower.client.render.item;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.client.render.IIconContainer;

public interface IItemIconContainerProvider extends IItemIconProvider {

    @Override
	@SideOnly(Side.CLIENT)
    default TextureAtlasSprite getIcon(ItemStack stack, int pass) {
        IIconContainer iconContainer = getIconContainer(stack);
        if(iconContainer != null) {
            switch (pass) {
                case 0:
                    return iconContainer.getIcon();
                case 1:
                    return iconContainer.getOverlayIcon();
            }
        }
        return null;
    }

    @Override
	@SideOnly(Side.CLIENT)
    default int getRenderPasses(ItemStack stack) {
        return 1;
    }

	@SideOnly(Side.CLIENT)
    IIconContainer getIconContainer(ItemStack itemStack);

}