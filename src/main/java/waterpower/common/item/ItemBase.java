package waterpower.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.Reference;
import waterpower.WaterPower;
import waterpower.client.render.IIconContainer;
import waterpower.client.render.item.IItemIconContainerProvider;

/**
 * @author hyh
 */
public abstract class ItemBase extends Item {

    public static int id;
    protected TextureAtlasSprite[] textures;

    public ItemBase(String id) {
        super();

        setCreativeTab(WaterPower.creativeTabWaterPower);
        setNoRepair();

        GameRegistry.registerItem(this, id);
        GlobalItems.items.add(this);
    }

    public String getTextureName(int index) {
        if (this.hasSubtypes)
            return getUnlocalizedName(new ItemStack(this, 1, index));
        if (index == 0) {
            return getUnlocalizedName();
        }
        return null;
    }

    public abstract String getTextureFolder();

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap register) {

        this.textures = new TextureAtlasSprite[32768];
        String textureFolder = getTextureFolder() + "/";

        for (int index = 0; index < 32768; index++) {
            String resource = Reference.ModID + ":items/" + textureFolder + getTextureName(index) + "";
            this.textures[index] = getTextureName(index) != null ? register.registerSprite(new ResourceLocation(resource)) : null;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(ItemStack stack, int pass) {
        if (this instanceof IItemIconContainerProvider) {
            IItemIconContainerProvider iItemIconContainerProvider = (IItemIconContainerProvider) this;
            IIconContainer iconContainer = iItemIconContainerProvider.getIconContainer(stack);
            if (iconContainer != null) {
                switch (pass) {
                    case 0:
                        return iconContainer.getIcon();
                    case 1:
                        return iconContainer.getOverlayIcon();
                }
            }
            return null;
        }
        if (stack.getItemDamage() < this.textures.length) {
            return this.textures[stack.getItemDamage()];
        }
        return this.textures.length < 1 ? null : this.textures[0];
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getRenderTexture() {
        return new ResourceLocation(Reference.ModID + ":textures/items/" + this.getUnlocalizedName() + ".png");
    }

    @Override
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        for (int meta = 0; meta < 32767; meta++) {
            ItemStack stack = new ItemStack(item, 1, meta);
            if (stopScanning(stack))
                break;
            if (validStack(stack))
                par3List.add(stack);
        }
    }

    public boolean stopScanning(ItemStack stack) {
        return getUnlocalizedName(stack) == null;
    }

    public boolean validStack(ItemStack stack) {
        return getUnlocalizedName(stack) != null;
    }

    public ItemStack get(int i) {
        return get(i, 1);
    }

    public ItemStack get(int i, int j) {
        return new ItemStack(this, j, i);
    }
}
