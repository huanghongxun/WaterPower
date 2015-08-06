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
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}
