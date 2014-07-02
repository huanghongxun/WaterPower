/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.client.render;

import org.jackhuang.watercraft.WaterCraft;
import org.jackhuang.watercraft.Reference;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class RecolorableTextures {
	public static final IIconContainer[] METAL = {
		ItemIcons.PLATE,
		ItemIcons.DUST,
		ItemIcons.DUST_TINY,
		ItemIcons.DUST_SMALL,
		ItemIcons.PLATE_DENSE,
		ItemIcons.INGOT,
		ItemIcons.BLOCK,
		ItemIcons.STICK,
		ItemIcons.SCREW,
		ItemIcons.GEAR,
		ItemIcons.NUGGET,
		ItemIcons.RING,
		ItemIcons.CRUSHED
	};
	
	public static void load() {
		for(ItemIcons i : ItemIcons.values()) {
			i.icon = WaterCraft.instance.iconRegister.registerIcon(Reference.ModID + ":iconsets/" + i.name());
		}
	}
	
	public static enum ItemIcons implements IIconContainer {
		INGOT,
		PLATE,
		PLATE_DENSE,
		NUGGET,
		BLOCK,
		STICK,
		GEAR,
		DUST,
		DUST_SMALL,
		DUST_TINY,
		SCREW,
		RING,
		CRUSHED;
		
		protected IIcon icon = null;

		@Override
		public IIcon getIcon() {
			return icon;
		}
	}
}
