/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.api;

import net.minecraft.item.ItemStack;

public interface IUpgrade {

    int getUnderworldAdditionalValue(ItemStack is);

    int getOverworldAdditionalValue(ItemStack is);

    int getRainAdditionalValue(ItemStack is);

    double getEnergyDemandMultiplier(ItemStack is);

    double getSpeedAdditionalValue(ItemStack is);

    int getStorageAdditionalValue(ItemStack is);

}
