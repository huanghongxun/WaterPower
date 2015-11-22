/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * WaterPower's References.
 * 
 * @author jackhuang
 */
public class Reference {

    public static final String ModChannel = "wtrpwr"; // Mod Server Channel

    public static final String ModID = "WaterPower"; // Mod ID

    public static final String ModName = "WaterPower"; // Mod Name

    public static final String Version = "0.3s"; // Mod Version

    /**
     * Generate Ores in World.
     */
    public static class WorldGen {
        public static boolean vanadiumOre = true, manganeseOre = true, monaziteOre = true, magnetOre = true, zincOre = true;

        public static float oreDensityFactor = 1;

        static void initConfig(Configuration config) {
            Property p = config.get("worldgen", "vanadiumOre", vanadiumOre);
            vanadiumOre = p.getBoolean(vanadiumOre);
            p = config.get("worldgen", "manganeseOre", manganeseOre);
            manganeseOre = p.getBoolean(manganeseOre);
            p = config.get("worldgen", "monaziteOre", monaziteOre);
            monaziteOre = p.getBoolean(monaziteOre);
            p = config.get("worldgen", "magnetOre", magnetOre);
            magnetOre = p.getBoolean(magnetOre);
            p = config.get("worldgen", "zincOre", zincOre);
            zincOre = p.getBoolean(zincOre);
        }
    }

    /**
     * Energy to Water mills.
     */
    public static class Energy {
        public static double mj = 2.5, rf = 0.12, charge = 0.1, ku = 0.25, hu = 1, steam = 0.1, water = 0.01, vis = 10000;

        static void initConfig(Configuration config) {
            Property p = config.get("energy", "mj", mj);
            mj = p.getDouble(mj);
            p = config.get("energy", "rf", rf);
            rf = p.getDouble(rf);
            p = config.get("energy", "charge", charge);
            charge = p.getDouble(charge);
            p = config.get("energy", "ku", ku);
            ku = p.getDouble(ku);
            p = config.get("energy", "steam", steam);
            steam = p.getDouble(steam);
            p = config.get("energy", "water", water);
            water = p.getDouble(water);
            p = config.get("energy", "hu", hu);
            hu = p.getDouble(hu);
            p = config.get("energy", "vis", vis);
            vis = p.getDouble(vis);
        }
    }

    public static class General {
        public static boolean enableMachines = true;

        public static boolean watermillNeedsRotor = true;

        /**
         * TileEntity update interval.
         */
        public static int updateTick = 20;

        static void initConfig(Configuration config) {
            Property p = config.get("rule", "watermillNeedRotor", true);
            watermillNeedsRotor = p.getBoolean(true);
            p = config.get("rule", "enableMachines", enableMachines);
            enableMachines = p.getBoolean(enableMachines);
            p = config.get("rule", "updateTick", updateTick);
            updateTick = p.getInt(updateTick);
        }
    }

    public static void initConfig(Configuration config) {
        WorldGen.initConfig(config);
        Energy.initConfig(config);
        General.initConfig(config);
    }
}
