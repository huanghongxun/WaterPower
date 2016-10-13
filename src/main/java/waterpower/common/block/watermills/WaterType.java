package waterpower.common.block.watermills;

import waterpower.client.Local;
import waterpower.common.item.other.ItemTrouser;
import waterpower.util.WPLog;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.IStringSerializable;

/**
 * 
 * @author jackhuang1998
 * 
 */
public enum WaterType implements IStringSerializable {
    MK1(1, 5), MK2(8, 5), MK3(32, 17), MK4(128, 17), MK5(512, 33), MK6(2048, 33), MK7(8192, 65), MK8(32768, 65), MK9(131072, 129), MK10(524288, 129), MK11(
            2097152, 255), MK12(8388608, 255), MK13(33554432, 513), MK14(134217728, 513), MK15(536870912, 1025), MK16(2147483647, 1025);

    public int output, length, total;

    public ItemTrouser trousers;

    private WaterType(int output, int length) {
        this.output = output;
        this.length = length;

        this.total = length * length * length - 1;
    }

    public static void initTrousers() {
        for (WaterType type : WaterType.values()) {
            type.initTrouser();
        }
    }

    public String getTrousersUnlocalizedName() {
        return "item.watermill.trousers." + name();
    }

    private void initTrouser() {
        try {
            trousers = new ItemTrouser(this);
        } catch (Exception e) {
            e.printStackTrace();
            WPLog.err("Failed to Register Trousers: " + name());
        }
    }

    public String tileEntityName() {
        return "WaterType." + name();
    }

    public String getShowedName() {
        return Local.get("cptwtrml.watermill.WATERMILL") + ' ' + name();
    }
    
    @Override
    public String getName() {
    	return name().toLowerCase();
    }

}
