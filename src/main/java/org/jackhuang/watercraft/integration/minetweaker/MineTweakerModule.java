package org.jackhuang.watercraft.integration.minetweaker;

import minetweaker.MineTweakerAPI;

import org.jackhuang.watercraft.integration.BaseModule;

public class MineTweakerModule extends BaseModule {
    @Override
    public void init() {
        MineTweakerAPI.registerClass(Machines.class);
    }
}
