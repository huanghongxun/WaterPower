/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.item.crafting;


import net.minecraft.util.StatCollector;

public enum MaterialTypes {
	Zinc(182, 201, 206, 0, false),
	ZincAlloy(226,226,226,0,false),
	Neodymium(210,221,221,0,false),
	NeodymiumMagnet(162,170,171,0,true),
	IndustrialSteel(221,243,249,0,true),
	Magnet(80,83,91,0,false),
	Vanadium(189,197,202,0,true),
	VanadiumSteel(166,176,183,0,true),
	Manganese(137,156,167,0,false),
	ManganeseSteel(174,181,194,0,true),
	Steel(75,83,94,0,false);
	
	public static int space = 100;
	
	public short R, G, B, A;
	public boolean blastFurnaceRequired;
	
	private MaterialTypes(int R, int G, int B, int A, boolean blastFurnaceRequired) {
		this.R = (short)R;
		this.G = (short)G;
		this.B = (short)B;
		this.A = (short)A;
		this.blastFurnaceRequired = blastFurnaceRequired;
	}
	
	public int ind() {
		return ordinal() * space;
	}
	
	public String getShowedName() {
		String format = "cptwtrml.material." + name();
		String s = StatCollector.translateToLocal(format);
		return s;
	}
}