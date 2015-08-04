package org.jackhuang.watercraft.integration;

public class CraftGuideModule extends BaseModule {

    @Override
    public void init() {
        new CraftGuideWaterPowerObject();
    }
}
