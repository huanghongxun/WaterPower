/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package org.jackhuang.compactwatermills.watermills;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author Aroma1997
 * 
 */
public class ItemCompactWaterMill extends ItemBlock {
	
	public ItemCompactWaterMill(int id) {
		super(id);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
		List par3List, boolean par4) {
		par3List.add("最大输出: " + WaterType.values()[par1ItemStack.getItemDamage()].output
			+ "EU/t");
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return WaterType.values()[par1ItemStack.getItemDamage()].showedName;
	}
	
	@Override
	public int getMetadata(int i) {
		if (i < WaterType.values().length) {
			return i;
		}
		else {
			return 0;
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return WaterType.values()[itemstack.getItemDamage()].name();
	}
	
}
