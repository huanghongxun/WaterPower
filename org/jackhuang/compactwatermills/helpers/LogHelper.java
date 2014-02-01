
package org.jackhuang.compactwatermills.helpers;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.Reference;

import cpw.mods.fml.common.FMLLog;

public class LogHelper {
	
	private static Logger WatermillLog = Logger.getLogger(Reference.ModID);
	
	public static void debugLog(Level level, String message) {
		if (! Reference.DEBUG_MODE) {
			return;
		}
		log(level, message);
	}
	
	public static void debugLog(String message) {
		debugLog(Level.INFO, message);
	}
	
	public static void init() {
		WatermillLog.setParent(FMLLog.getLogger());
	}
	
	public static void log(Level level, String message) {
		WatermillLog.log(level, message);
	}
	
	public static void log(String message) {
		log(Level.INFO, message);
	}
	
}
