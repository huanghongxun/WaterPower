package org.jackhuang.compactwatermills.item.updates;

import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.item.rotors.ItemRotor;

public enum UpdaterType {
	WaterUraniumIngot("水之铀锭", "", "watermillWaterUraniumIngot"),
	WaterUraniumPlateMK1("水之铀锭板MK1", "", "watermillWaterUraniumPlateMK1"),
	WaterUraniumPlateMK2("水之铀锭板MK2", "", "watermillWaterUraniumPlateMK2"),
	WaterUraniumPlateMK3("水之铀锭板MK3", "", "watermillWaterUraniumPlateMK3"),
	IR_FE("铱铁合金板", "", "watermillIrFePlate"),
	MK0("基本升级插件", "", "watermillUpdaterMK0"),
	MK1("升级插件MK1", "", "watermillUpdaterMK1"),
	MK2("升级插件MK2", "", "watermillUpdaterMK2"),
	MK3("升级插件MK3", "", "watermillUpdaterMK3"),
	RESERVOIR_CORE("水库方块核心", "", "watermillReservoirCore"),
	RESERVOIR_CORE_ADVANCED("高级水库方块核心", "", "watermillReservoirCoreAdvanced");
	
	public String showedName, information;
	
	public String unlocalizedName;
	
	private ItemWatermillUpdater updater;
	
	private UpdaterType(String showedName, String information, String unlocalizedName) {
		this.showedName = showedName;
		this.information = information;
		this.unlocalizedName = unlocalizedName;
	}

}
