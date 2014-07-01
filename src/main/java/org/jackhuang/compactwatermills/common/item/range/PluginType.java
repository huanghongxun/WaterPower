package org.jackhuang.compactwatermills.common.item.range;

import org.jackhuang.compactwatermills.common.item.GlobalItems;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public enum PluginType {
	UnderMK1(1, 0, 0, 0, 0, 0),
	UnderMK2(4, 0, 0, 0, 0, 0),
	UnderMK3(16, 0, 0, 0, 0, 0),
	UnderMK4(64, 0, 0, 0, 0, 0),
	OverMK1(0, 1, 0, 0, 0, 0),
	OverMK2(0, 4, 0, 0, 0, 0),
	OverMK3(0, 16, 0, 0, 0, 0),
	OverMK4(0, 64, 0, 0, 0, 0),
	RainMK1(0, 0, 1, 0, 0, 0),
	RainMK2(0, 0, 4, 0, 0, 0),
	RainMK3(0, 0, 16, 0, 0, 0),
	RainMK4(0, 0, 64, 0, 0, 0),
	StorageMK1(0, 0, 0, 0, 1, 0),
	StorageMK2(0, 0, 0, 0, 4, 0),
	StorageMK3(0, 0, 0, 0, 16, 0),
	StorageMK4(0, 0, 0, 0, 64, 0),
	AllRoundMK1(1, 1, 1, 0, 1, 0),
	AllRoundMK2(4, 4, 4, 0, 4, 0),
	AllRoundMK3(16, 16, 16, 0, 16, 0),
	AllRoundMK4(64, 64, 64, 0, 64, 0),
	SpeedMK1(0, 0, 0, 0.7, 0, 1.6),
	SpeedMK2(0, 0, 0, 0.49, 0, 2.56),
	SpeedMK3(0, 0, 0, 0.2401, 0, 6.5536),
	SpeedMK4(0, 0, 0, 0.05764801, 0, 42.94967296);
	
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
		return StatCollector.translateToLocal(getUnlocalizedName());
	}
	
	public ItemStack item() {
		return new ItemStack(GlobalItems.plugins, 1, this.ordinal());
	}
}
