package org.jackhuang.watercraft.integration;

import org.jackhuang.watercraft.integration.waila.WailaModule;
import org.jackhuang.watercraft.util.Mods;

public enum IntegrationType {
    Waila(WailaModule.class, Mods.Waila),
    CraftGuide(CraftGuideModule.class, Mods.CraftGuide);
    
    public Class<? extends BaseModule> clazz;
    public Mods.SimpleMod mod;
    public BaseModule module;
    
    private IntegrationType(Class<? extends BaseModule> clz, Mods.SimpleMod mod) {
        clazz = clz;
        this.mod = mod;
    }
    
    public BaseModule getModule() {
        if(mod.isAvailable && module == null)
            try {
                module = clazz.newInstance();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        return module;
    }
}
