/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.util;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;

public class Mods {

    public static class IDs {
        public static final String BuildCraftPower = "BuildCraftAPI|power";
        public static final String BuildCraftCore = "BuildCraft|Core";
        public static final String BuildCraftFactory = "BuildCraft|Factory";
        public static final String Factorization = "factorization";
        public static final String IndustrialCraft2 = "IC2";
        public static final String IndustrialCraft2API = "IC2API";
        public static final String Thaumcraft = "Thaumcraft";
        public static final String ThermalExpansion = "ThermalExpansion";
        public static final String RotaryCraft = "RotaryCraft";
        public static final String CoFHAPIEnergy = "CoFHAPI|energy";
        public static final String GregTech = "gregtech";
        public static final String Waila = "Waila";
        public static final String Mekanism = "Mekanism";
        public static final String CraftGuide = "craftguide";
        public static final String ExNihilo = "ExNihilo";
        public static final String Railcraft = "Railcraft";
        public static final String ImmersiveEngineering = "ImmersiveEngineering";
        public static final String MineTweaker3 = "MineTweaker3";
    }

    public static final SimpleMod Factorization = new SimpleMod(
            IDs.Factorization);
    public static final SimpleMod ThermalExpansion = new SimpleMod(
            IDs.ThermalExpansion);
    public static final SimpleMod IndustrialCraft2 = new SimpleMod(
            IDs.IndustrialCraft2);
    public static final SimpleMod MineTweaker3 = new SimpleMod(
            IDs.MineTweaker3);
    public static final SimpleMod BuildCraftCore = new SimpleMod(
            IDs.BuildCraftCore);
    public static final SimpleMod BuildCraftFactory = new SimpleMod(
            IDs.BuildCraftFactory);
    public static final SimpleMod Railcraft = new SimpleMod(
            IDs.Railcraft);
    public static final SimpleMod BuildCraftPower = new SimpleMod(
            IDs.BuildCraftPower);
    public static final SimpleMod CoFHAPIEnergy = new SimpleMod(
            IDs.CoFHAPIEnergy);
    public static final SimpleMod ImmersiveEngineering = new SimpleMod(
            IDs.ImmersiveEngineering);
    public static final SimpleMod Thaumcraft = new SimpleMod(IDs.Thaumcraft);
    public static final SimpleMod GregTech = new SimpleMod(IDs.GregTech);
    public static final SimpleMod Waila = new SimpleMod(IDs.Waila);
    public static final SimpleMod Mekanism = new SimpleMod(IDs.Mekanism);
    public static final SimpleMod CraftGuide = new SimpleMod(IDs.CraftGuide);
    public static final SimpleMod ExNihilo = new SimpleMod(IDs.ExNihilo);

    public static class SimpleMod {
        public String id;
        public boolean isAvailable;

        public SimpleMod(String id) {
            this.id = id;

            ArtifactVersion version = VersionParser.parseVersionReference(id);
            if (Loader.isModLoaded(version.getLabel()))
                isAvailable = version.containsVersion(Loader.instance()
                        .getIndexedModList().get(version.getLabel())
                        .getProcessedVersion());
            else
                isAvailable = ModAPIManager.INSTANCE.hasAPI(version.getLabel());
        }
    }

}
