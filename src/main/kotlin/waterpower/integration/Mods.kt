/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration

import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.ModAPIManager
import net.minecraftforge.fml.common.versioning.VersionParser

class Mod(val id: String) {
    val isAvailable: Boolean by lazy {
        val version = VersionParser.parseVersionReference(id)
        if (Loader.isModLoaded(version.label))
            version.containsVersion(Loader.instance().indexedModList[version.label]?.processedVersion)
        else
            ModAPIManager.INSTANCE.hasAPI(version.label)
    }

    companion object {
        val AppliedEnergistics2 = Mod(IDs.AppliedEnergistics2)
        val BuildCraftCore = Mod(IDs.BuildCraftCore)
        val BuildCraftFactory = Mod(IDs.BuildCraftFactory)
        val BuildCraftPower = Mod(IDs.BuildCraftPower)
        val CoFHAPIEnergy = Mod(IDs.CoFHAPIEnergy)
        val CraftGuide = Mod(IDs.CraftGuide)
        val EnderIO = Mod(IDs.EnderIO)
        val ExNihilo = Mod(IDs.ExNihilo)
        val Factorization = Mod(IDs.Factorization)
        val GregTech = Mod(IDs.GregTech)
        val ImmersiveEngineering = Mod(IDs.ImmersiveEngineering)
        val IndustrialCraft2 = Mod(IDs.IndustrialCraft2)
        val Mekanism = Mod(IDs.Mekanism)
        val CraftTweaker = Mod(IDs.CraftTweaker)
        val RailCraft = Mod(IDs.RailCraft)
        val Thaumcraft = Mod(IDs.Thaumcraft)
        val ThermalExpansion = Mod(IDs.ThermalExpansion)
        val TinkersConstruct = Mod(IDs.TinkersConstruct)
        val Waila = Mod(IDs.Waila)
    }
}

object IDs {
    const val AppliedEnergistics2 = "appliedenergistics2"
    const val BuildCraftCore = "BuildCraft|Core"
    const val BuildCraftFactory = "BuildCraft|Factory"
    const val BuildCraftPower = "BuildCraftAPI|power"
    const val CoFHAPIEnergy = "CoFHAPI|energy"
    const val CraftGuide = "craftguide"
    const val CraftTweaker = "MineTweaker3"
    const val EnderIO = "EnderIO"
    const val ExNihilo = "exnihilo"
    const val Factorization = "factorization"
    const val GregTech = "gregtech"
    const val ImmersiveEngineering = "ImmersiveEngineering"
    const val IndustrialCraft2 = "IC2"
    const val Mekanism = "Mekanism"
    const val RailCraft = "Railcraft"
    const val RotaryCraft = "RotaryCraft"
    const val Thaumcraft = "Thaumcraft"
    const val ThermalExpansion = "ThermalExpansion"
    const val TinkersConstruct = "TConstruct"
    const val Waila = "Waila"
}