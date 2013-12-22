package org.jackhuang.compactwatermills.item.updates;

import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.item.rotors.ItemRotor;

public enum UpdaterType {
	
	MK1("升级插件MK1", "", "watermillUpdaterMK1"),
	MK2("升级插件MK2", "", "watermillUpdaterMK2"),
	MK3("升级插件MK3", "", "watermillUpdaterMK3");
	
	public String showedName, information;
	
	public String unlocalizedName;
	
	private ItemWatermillUpdater updater;
	
	private UpdaterType(String showedName, String information, String unlocalizedName) {
		this.showedName = showedName;
		this.information = information;
		this.unlocalizedName = unlocalizedName;
	}
	
	private void initUpdater(Configuration config) {
		try {
			updater = (ItemWatermillUpdater) new ItemWatermillUpdater(config, InternalName.itemUpdaters, this)
				.setUnlocalizedName(unlocalizedName);
		} catch (Exception e) {
			e.printStackTrace();
			LogHelper.log(Level.SEVERE, "Failed to Register Watermill Updater: " + showedName);
		}
	}
	
	public static void initUpdaters(Configuration config) {
		for (UpdaterType type : values()) {
			type.initUpdater(config);
		}
	}

}
