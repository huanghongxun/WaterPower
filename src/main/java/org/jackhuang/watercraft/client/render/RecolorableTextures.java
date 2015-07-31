/**
 * Copyright (c) Huang Yuhui, 2014
 *
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.client.render;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.client.ClientProxy;

import net.minecraft.client.renderer.texture.IIconRegister;
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
	ItemIcons.RING
    };

    public static final IIconContainer[] CRUSHED = {
	ItemIcons.CRUSHED
    };

    public static final IIconContainer[] CRAFTING = {
	ItemIcons.PADDLE_BASE,
	ItemIcons.DRAINAGE_PLATE,
	ItemIcons.FIXED_FRAME,
	ItemIcons.FIXED_TOOL,
	ItemIcons.ROTATION_AXLE,
	ItemIcons.OUTPUT_INTERFACE,
	ItemIcons.ROTOR,
	ItemIcons.STATOR,
	ItemIcons.CASING,
	ItemIcons.CIRCUIT
    };

    public static void load() {
	ClientProxy proxy = (ClientProxy) WaterPower.proxy;
	for (ItemIcons i : ItemIcons.values()) {
	    i.registerIcon(proxy.iconRegister);
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
	PADDLE_BASE,
	DRAINAGE_PLATE,
	FIXED_FRAME,
	FIXED_TOOL,
	ROTATION_AXLE,
	OUTPUT_INTERFACE,
	ROTOR,
	STATOR,
	CASING,
	CIRCUIT,
	CRUSHED;

	protected IIcon icon = null;

	public void registerIcon(IIconRegister iconRegister) {
	    icon = iconRegister.registerIcon(Reference.ModID + ":iconsets/" + name());
	}

	@Override
	public IIcon getIcon() {
	    return icon;
	}
    }
}
