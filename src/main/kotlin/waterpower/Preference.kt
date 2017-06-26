package waterpower

import waterpower.config.ConfigCategory
import waterpower.config.ConfigValue
import waterpower.config.OreConfig

/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

object Preference {

    @ConfigCategory(category = "worldgen")
    object OreGeneration {
        @ConfigValue
        var vanadiumOre = OreConfig(4, 8, 10, 13)
        @ConfigValue
        var manganeseOre = OreConfig(8, 8, 6, 20)
        @ConfigValue
        var monaziteOre = OreConfig(4, 8, 6, 32)
        @ConfigValue
        var magnetiteOre = OreConfig(8, 8, 6, 64)
        @ConfigValue
        var zincOre = OreConfig(8, 8, 6, 64)
    }

    /**
     * Energy conversions. Set 1EU as the reference unit.
     */
    @ConfigCategory(category = "energy")
    object Energy {
    }

    @ConfigCategory(category = "general")
    object General {
        @ConfigValue
        var enableMachines = true
        @ConfigValue
        var rotorNeeded = true
        /**
         * interval of TileEntity updating.
         */
        @ConfigValue
        var updateTicks = 40
    }
}