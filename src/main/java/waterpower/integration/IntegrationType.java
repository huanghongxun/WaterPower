/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration;

import waterpower.integration.ic2.IndustrialCraftModule;
import waterpower.integration.minetweaker.MineTweakerModule;
import waterpower.integration.waila.WailaModule;
import waterpower.util.Mods;

public enum IntegrationType {
    IndustrialCraft(IndustrialCraftModule.class, Mods.IndustrialCraft2),
    BuildCraft(BuildCraftModule.class, Mods.BuildCraftCore),
    Mekanism(MekanismModule.class, Mods.Mekanism),
    Railcraft(RailcraftModule.class, Mods.Railcraft),
    MineTweaker(MineTweakerModule.class, Mods.MineTweaker3),
    ThermalExpansion(ThermalExpansionModule.class, Mods.ThermalExpansion),
    TinkersConstruct(TinkersConstructModule.class, Mods.TinkersConstruct),
    Thaumcraft(ThaumcraftModule.class, Mods.Thaumcraft),
    Waila(WailaModule.class, Mods.Waila);

    public Class<? extends BaseModule> clazz;
    public Mods.SimpleMod mod;
    public BaseModule module;

    private IntegrationType(Class<? extends BaseModule> clz, Mods.SimpleMod mod) {
        clazz = clz;
        this.mod = mod;
    }

    public BaseModule getModule() {
        if (mod.isAvailable && module == null)
            try {
                module = clazz.newInstance();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        return module;
    }
}
