package waterpower.common.item.range;

import net.minecraft.item.ItemStack;
import waterpower.api.IUpgrade;
import waterpower.client.render.IIconRegister;
import waterpower.client.render.item.IItemIconProvider;
import waterpower.common.item.ItemBase;

public class ItemPlugins extends ItemBase implements IItemIconProvider, IIconRegister, IUpgrade {

    public ItemPlugins() {
        super("plugin");
        setHasSubtypes(true);
    }

    @Override
    public String getTextureFolder() {
        return "plugins";
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemstack) {
        if (itemstack.getItemDamage() >= PluginType.values().length)
            return null;
        return PluginType.values()[itemstack.getItemDamage()].getShowedName();
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if (itemstack.getItemDamage() >= PluginType.values().length)
            return null;
        return PluginType.values()[itemstack.getItemDamage()].getUnlocalizedName();
    }

    @Override
    public int getUnderworldAdditionalValue(ItemStack is) {
        return PluginType.values()[is.getItemDamage()].under;
    }

    @Override
    public int getOverworldAdditionalValue(ItemStack is) {
        return PluginType.values()[is.getItemDamage()].over;
    }

    @Override
    public int getRainAdditionalValue(ItemStack is) {
        return PluginType.values()[is.getItemDamage()].rain;
    }

    @Override
    public double getSpeedAdditionalValue(ItemStack is) {
        return PluginType.values()[is.getItemDamage()].speed;
    }

    @Override
    public int getStorageAdditionalValue(ItemStack is) {
        return PluginType.values()[is.getItemDamage()].storage * 10000;
    }

    @Override
    public double getEnergyDemandMultiplier(ItemStack is) {
        return PluginType.values()[is.getItemDamage()].demand;
    }
}
