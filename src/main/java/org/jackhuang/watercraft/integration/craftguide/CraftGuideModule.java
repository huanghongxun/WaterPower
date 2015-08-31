/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.integration.craftguide;

import org.jackhuang.watercraft.integration.BaseModule;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.Optional.Method;

public class CraftGuideModule extends BaseModule {

    @Method(modid = Mods.IDs.CraftGuide)
    public void register() {
        new CraftGuideWaterPowerObject();
    }

    @Override
    public void init() {
        try {
            register();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
