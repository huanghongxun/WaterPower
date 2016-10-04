package waterpower.client.render.item;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.client.render.IIconContainer;

@SideOnly(Side.CLIENT)
public interface IItemIconContainerProvider extends IItemIconProvider {

    @Override
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
    default int getRenderPasses(ItemStack stack) {
        return 1;
    }

    IIconContainer getIconContainer(ItemStack itemStack);

}