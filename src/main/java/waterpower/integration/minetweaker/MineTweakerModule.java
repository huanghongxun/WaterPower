/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.minetweaker;

import minetweaker.MineTweakerAPI;

import waterpower.integration.BaseModule;

public class MineTweakerModule extends BaseModule {
    @Override
    public void init() {
        MineTweakerAPI.registerClass(Machines.class);
    }
}
