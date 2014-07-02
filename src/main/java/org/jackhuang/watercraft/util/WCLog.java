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
import org.jackhuang.watercraft.WaterCraft;
import org.jackhuang.watercraft.Reference;

public class WCLog {
	
	private static Logger log = LogManager.getLogger(Reference.ModID);
	
	public static void log(Level level, String message) {
		log.log(level, message);
	}
	
	public static void log(String message) {
		log(Level.INFO, message);
	}
	
	public static void warn(String message) {
		log(Level.WARN, message);
	}
	
	public static void err(String message) {
		log(Level.ERROR, message);
	}
	
	public static void debug(String message) {
		log(Level.DEBUG, message);
	}
	
	public static void debugLog(String message) {
		if(WaterCraft.isDeobf())
			log(message);
	}
	
}
