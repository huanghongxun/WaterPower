/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.ic2;

import ic2.api.item.IC2Items;
import net.minecraft.item.ItemStack;
import waterpower.util.WPLog;

public class ICItemFinder {

    private ICItemFinder() {
    }

    public static ItemStack getItem(String name) {
        try {
        	String[] strings = name.split(",");
        	if (strings.length == 2)
        		return IC2Items.getItem(strings[0], strings[1]);
        	else
        		return IC2Items.getItem(name);
        } catch (Throwable e) {
        	WPLog.warn("Failed to get Item: " + name);
            return null;
        }
    }

    public static ItemStack getItem(String name, String variant) {
        try {
            return IC2Items.getItem(name, variant);
        } catch (Throwable e) {
            return null;
        }
    }
}
