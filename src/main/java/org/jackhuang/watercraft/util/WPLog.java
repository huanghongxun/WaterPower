/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.util;

import java.util.logging.Level;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.Reference;

import cpw.mods.fml.common.FMLLog;

/**
 * WaterPower log class.
 * 
 * @author lasm_
 */
public class WPLog {

    /**
     * Log Message.
     * 
     * @see org.apache.logging.log4j.Level
     * @param level
     *            log levels
     * @param message
     *            log message
     */
    public static void log(Level level, String message) {
    	FMLLog.log(level, message);
    }

    /**
     * Log INFO Message.
     * 
     * @param message
     */
    public static void log(String message) {
        log(Level.INFO, message);
    }

    /**
     * Warn player massage.
     * 
     * @param message
     */
    public static void warn(String message) {
        log(Level.WARNING, message);
    }

    /**
     * Error message.
     * 
     * @param message
     */
    public static void err(String message) {
        log(Level.SEVERE, message);
    }

    /**
     * Debug message.
     * 
     * @param message
     */
    public static void debug(String message) {
        log(Level.CONFIG, message);
    }

    /**
     * if this is a experience version then debug message.
     * 
     * @param message
     */
    public static void debugLog(String message) {
        if (WaterPower.isDeobf())
            log(message);
    }
}
