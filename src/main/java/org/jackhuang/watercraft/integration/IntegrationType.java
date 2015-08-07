package org.jackhuang.watercraft.integration;

import org.jackhuang.watercraft.integration.craftguide.CraftGuideModule;
import org.jackhuang.watercraft.integration.ic2.IndustrialCraftModule;
import org.jackhuang.watercraft.integration.minetweaker.MineTweakerModule;
import org.jackhuang.watercraft.integration.waila.WailaModule;
import org.jackhuang.watercraft.util.Mods;

public enum IntegrationType {
    IndustrialCraft(IndustrialCraftModule.class, Mods.IndustrialCraft2),
    BuildCraft(BuildCraftModule.class, Mods.BuildCraftCore),
    Mekanism(MekanismModule.class, Mods.Mekanism),
    Railcraft(RailcraftModule.class, Mods.Railcraft),
    ThermalExpansion(ThermalExpansionModule.class, Mods.ThermalExpansion),
    Waila(WailaModule.class, Mods.Waila),
    MineTweaker3(MineTweakerModule.class, Mods.MineTweaker3),
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
