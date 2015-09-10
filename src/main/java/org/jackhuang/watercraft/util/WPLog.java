/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.WaterPower;

/**
 * WaterPower log class.
 * 
 * @author lasm_
 */
public class WPLog {
    /**
     * setting logger.
     */
    private static final Logger log = LogManager.getLogger(Reference.ModID);

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
        log.log(level, message);
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
        log(Level.WARN, message);
    }

    /**
     * Error message.
     * 
     * @param message
     */
    public static void err(String message) {
        log(Level.ERROR, message);
    }

    /**
     * Debug message.
     * 
     * @param message
     */
    public static void debug(String message) {
        log(Level.DEBUG, message);
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
