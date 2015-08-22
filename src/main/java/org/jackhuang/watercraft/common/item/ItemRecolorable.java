/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.client.ClientProxy;
import org.jackhuang.watercraft.client.render.IIconContainer;
import org.jackhuang.watercraft.common.item.crafting.MaterialTypes;

public abstract class ItemRecolorable extends ItemWaterPower {

	
	public ItemRecolorable(String id) {
		super(id);
	}

	public abstract short[] getRGBA(ItemStack stack);

	public abstract IIconContainer getIconContainer(int meta);
	public abstract IIconContainer[] getIconContainers();
	
	@Override
	public Icon getIconFromDamage(int meta) {
		return getIconContainer(meta).getIcon();
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		if(WaterPower.proxy instanceof ClientProxy) {
			ClientProxy proxy = (ClientProxy) WaterPower.proxy;
			for(IIconContainer i : getIconContainers()) {
				i.registerIcon(iconRegister);
			}
		}
	}
}
