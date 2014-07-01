package org.jackhuang.compactwatermills.api;

import net.minecraft.item.ItemStack;

public interface IPluginHandler {

	int getUnderworldAdditionalValue(ItemStack is);
	int getOverworldAdditionalValue(ItemStack is);
	int getRainAdditionalValue(ItemStack is);
	double getEnergyDemandMultiplier(ItemStack is);
	double getSpeedAdditionalValue(ItemStack is);
	int getStorageAdditionalValue(ItemStack is);
	
}
