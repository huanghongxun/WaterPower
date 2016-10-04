package waterpower.common.block.ore;

import java.awt.Color;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import waterpower.client.render.IIconContainer;
import waterpower.client.render.RecolorableTextures;
import waterpower.common.item.ItemRecolorable;

public class ItemOreDust extends ItemRecolorable {

    public ItemOreDust() {
        super("cptItemOreDust");

        setHasSubtypes(true);
        registerOreDict();
    }

    @Override
    public String getTextureFolder() {
        return "ore";
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if (itemstack.getItemDamage() >= OreType.values().length)
            return null;
        return "item.oreDust" + OreType.values()[itemstack.getItemDamage()].name();
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemstack) {
        return I18n.format(OreType.values()[itemstack.getItemDamage()].getUnlocalizedName()) + " "
                + I18n.format("cptwtrml.forms.dust");
    }

    public ItemStack get(OreType type) {
        return get(type.ordinal());
    }

    public ItemStack get(OreType type, int amount) {
        return get(type.ordinal(), amount);
    }

    public void registerOreDict() {
        for (OreType type : OreType.values()) {
            OreDictionary.registerOre("oreDust" + type.t.getName(), get(type));
        }
    }

    @Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        int meta = stack.getItemDamage();
        OreType o = OreType.values()[meta];
        return new Color(o.R, o.G, o.B, o.A).getRGB();
    }

    @Override
    public IIconContainer getIconContainer(ItemStack stack) {
        return getIconContainers()[0];
    }

    @Override
    public IIconContainer[] getIconContainers() {
        return RecolorableTextures.CRUSHED;
    }

}