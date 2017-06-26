package waterpower.common

import waterpower.config.ConfigCategory
import waterpower.config.ConfigValue

/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

enum class Energy {
    MJ {
        override fun getFromEU(eu: Double) = EU2MJ(eu)
    },
    RF {
        override fun getFromEU(eu: Double) = EU2RF(eu)
    },
    KU {
        override fun getFromEU(eu: Double) = EU2KU(eu)
    },
    HU {
        override fun getFromEU(eu: Double) = EU2HU(eu)
    },
    EU {
        override fun getFromEU(eu: Double) = eu
    },
    STEAM {
        override fun getFromEU(eu: Double) = EU2Steam(eu)
    },
    WATER {
        override fun getFromEU(eu: Double) = EU2Water(eu)
    };

    abstract fun getFromEU(eu: Double): Double

    companion object {
        fun EU2MJ(eu: Double): Double {
            return eu / EnergyConfig.mj
        }

        fun MJ2EU(mj: Double): Double {
            return mj * EnergyConfig.mj
        }

        fun EU2RF(eu: Double): Double {
            return eu / EnergyConfig.rf
        }

        fun RF2EU(rf: Double): Double {
            return rf * EnergyConfig.rf
        }

        fun EU2KU(eu: Double): Double {
            return eu / EnergyConfig.ku
        }

        fun KU2EU(ku: Double): Double {
            return ku * EnergyConfig.ku
        }

        fun EU2HU(eu: Double): Double {
            return eu / EnergyConfig.hu
        }

        fun HU2EU(hu: Double): Double {
            return hu * EnergyConfig.hu
        }

        fun EU2Steam(eu: Double): Double {
            return eu / EnergyConfig.steam
        }

        fun Steam2EU(steam: Double): Double {
            return steam * EnergyConfig.steam
        }

        fun EU2Water(eu: Double): Double {
            return eu / EnergyConfig.water
        }

        fun Water2EU(water: Double): Double {
            return water * EnergyConfig.water
        }
    }
}

@ConfigCategory(category = "energy")
object EnergyConfig {
    @ConfigValue
    var mj = 2.5
    @ConfigValue
    var rf = 0.12
    @ConfigValue
    var ku = 0.25
    @ConfigValue
    var hu = 1.0
    @ConfigValue
    var steam = 0.1
    @ConfigValue
    var water = 0.01
}