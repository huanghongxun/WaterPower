/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.item.crafting;

import java.util.Arrays;

import org.jackhuang.watercraft.client.render.IIconContainer;
import org.jackhuang.watercraft.client.render.RecolorableTextures;

import net.minecraft.util.StatCollector;

public enum LevelTypes {
	/** Stone & Wood*/
	MK1(RecolorableTextures.CRAFTING, 255, 255, 255, 0),
	/** Brass & Zinc */
	MK3(RecolorableTextures.CRAFTING, 255, 255, 255, 0),
	/** Steel */
	MK4(RecolorableTextures.CRAFTING, 255, 255, 255, 0),
	/** Vanadium Steel & Maganese Steel */
	MK5(RecolorableTextures.CRAFTING, 255, 255, 255, 0),
	/** Most expansive! */
	MK7(RecolorableTextures.CRAFTING, 255, 255, 255, 0);
	
	public short R, G, B, A;
	
	public IIconContainer[] iconContainer;
	
	private LevelTypes(IIconContainer[] iconContainer, int R, int G, int B, int A) {
		this.R = (short)R;
		this.G = (short)G;
		this.B = (short)B;
		this.A = (short)A;
		this.iconContainer = (IIconContainer[]) Arrays.copyOf(iconContainer, 64);
	}
	
	public String getShowedName() {
		String format = "cptwtrml.level." + name();
		String s = StatCollector.translateToLocal(format);
		return s;
	}
}
