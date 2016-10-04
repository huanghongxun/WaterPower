package waterpower.common.item.range;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import waterpower.common.item.GlobalItems;

public enum PluginType {
    UnderMK1(1, 0, 0, 0, 0, 0), OverMK1(0, 1, 0, 0, 0, 0), RainMK1(0, 0, 1, 0, 0, 0), StorageMK1(0, 0, 0, 0, 1, 0), StorageMK2(0, 0, 0, 0, 4, 0), StorageMK3(0,
            0, 0, 0, 16, 0), StorageMK4(0, 0, 0, 0, 64, 0), AllRoundMK1(1, 1, 1, 0, 1, 0), SpeedMK1(0, 0, 0, 0.7, 0, 1.6), ;

    int under, over, rain, storage;
    double demand, speed;

    private PluginType(int under, int over, int rain, double speed, int storage, double demand) {
        this.under = under;
        this.over = over;
        this.rain = rain;
        this.speed = speed;
        this.storage = storage;
        this.demand = demand;
    }

    public String getUnlocalizedName() {
        return "cptwtrml.plugin." + name();
    }

    public String getShowedName() {
        return I18n.format(getUnlocalizedName());
    }

    public ItemStack item() {
        return new ItemStack(GlobalItems.plugins, 1, this.ordinal());
    }
}