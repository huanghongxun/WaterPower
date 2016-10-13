package waterpower.common.item.range;

import net.minecraft.client.resources.I18n;
import waterpower.client.Local;

public enum RangeType {
    MK1("watermillRangeMK1", 2), MK2("watermillRangeMK2", 16), MK3("watermillRangeMK3", 128);

    public String unlocalizedName;
    public int range;

    RangeType(String unlocalizedName, int get) {
        this.unlocalizedName = unlocalizedName;
        this.range = get;
    }

    public String getInfo() {
        return Local.get("cptwtrml.range.info." + name());
    }

    public String getShowedName() {
        return Local.get("cptwtrml.range.name." + name());
    }

}