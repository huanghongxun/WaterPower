/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft;

public class Reference {
	
	public static final String ModChannel = "wtrpwr";
	
	public static final String ModID = "CompactWatermills";
	
	public static final String ModName = "WaterPower";
	
	public static final String Version = "0.3d";
	
	public static final String MCVersion = "1.7.10";
	
	public static final String 
			configNeedRotor = "watermillNeedRotor";
	
	public static boolean watermillNeedRotor = true;
	
	public static class WorldGen {
		public static boolean vanadiumOre = true,
				manganeseOre = true,
				monaziteOre = true,
				magnetOre = true,
				zincOre = true;
		
		public static float oreDensityFactor = 1;
	}
	
	public static class Energy {
		public static float mj = 2.5f,
				rf = 0.12f,
				charge = 0.1f,
				ku = 0.25f,
				steam = 0.1f,
				water = 0.01f;
	}
}
