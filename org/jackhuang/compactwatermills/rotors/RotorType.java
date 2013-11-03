/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package org.jackhuang.compactwatermills.rotors;


import java.util.logging.Level;

import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/**
 * 
 * @author Aroma1997
 * 
 */
public enum RotorType {
	
	WOOD(0, 0.125, 1800 * 20, "木转子", "waterRotorWood", ItemRotor.class),
	STONE(1, 0.2, 500 * 20, "石转子", "waterRotorStone", ItemRotor.class),
	LEAD(2, 0.215, 500 * 20, "铅转子", "waterRotorLead", ItemRotor.class),
	TIN(3, 0.23, 700 * 20, "锡转子", "waterRotorTin", ItemRotor.class),
	GOLD(4, 0.7, 800 * 20, "金转子", "waterRotorGold", ItemRotor.class),
	COPPER(5, 0.25, 800 * 20, "铜转子", "waterRotorCopper", ItemRotor.class),
	SLIVER(6, 0.3, 1000 * 20, "银转子", "waterRotorSliver", ItemRotor.class),
	IRON(7, 0.35, 2000 * 20, "铁转子", "waterRotorIron", ItemRotor.class),
	REFINEDIRON(8, 0.4, 2600 * 20, "精炼铁转子", "waterRotorRefinedIron", ItemRotor.class),
	OBSIDIAN(9, 0.45, 2100 * 20, "黑曜石转子", "waterRotorObsidian", ItemRotor.class),
	BRONZE(10, 0.5, 2200 * 20, "青铜转子", "waterRotorBronze", ItemRotor.class),
	LAPIS(11, 0.55, 1800 * 20, "青金石转子", "waterRotorLapis", ItemRotor.class),
	QUARTZ(12, 0.6, 3000 * 20, "石英转子", "waterRotorQuartz", ItemRotor.class),
	CARBON(12, 0.65, 6000 * 20, "碳板转子", "waterRotorCarbon", ItemRotor.class),
	ADVANCED(13, 0.7, 3000 * 20, "高级合金转子", "waterRotorAdvanced", ItemRotor.class),
	EMERALD(14, 0.75, 7000 * 20, "绿宝石转子", "waterRotorEmerald", ItemRotor.class),
	DIAMOND(15, 0.8, 10000 * 20, "钻石转子", "waterRotorDiamond", ItemRotor.class),
	IRIDIUM(16, 0.9, 20000 * 20, "铱转子", "waterRotorIridium", ItemRotor.class);
	
	public static void initRotors() {
		for (RotorType type : RotorType.values()) {
			type.initRotor();
		}
	}
	
	public int number;
	
	public double efficiency;
	
	public int maxDamage;
	
	public String showedName;
	
	public String unlocalizedName;
	
	public Class<? extends ItemRotor> claSS;
	
	public int id;
	
	private ItemRotor rotor;
	
	private RotorType(int number, double efficiency, int maxDamage, String showedName,
		String unlocalizedName, Class<? extends ItemRotor> claSS) {
		this.number = number;
		this.efficiency = efficiency;
		this.maxDamage = maxDamage;
		this.showedName = showedName;
		this.unlocalizedName = unlocalizedName;
		this.claSS = claSS;
	}
	
	public void getConfig(Configuration config) {
		Property rotorId = config.getItem(this.unlocalizedName, Reference.defaultRotorID + number);
		rotorId.comment = "This is the id of " + showedName + " Item.";
		id = rotorId.getInt(Reference.defaultRotorID + number);
	}
	
	public Item getItem() {
		return rotor;
	}
	
	public ItemRotor getItemRotor() {
		return rotor;
	}
	
	public boolean isInfinite() {
		return maxDamage == 0;
	}
	
	private void initRotor() {
		try {
			rotor = (ItemRotor) claSS.getConstructor(RotorType.class).newInstance(this)
					.setUnlocalizedName(unlocalizedName)
					.setMaxDamage(maxDamage);
		}
		catch (Exception e) {
			e.printStackTrace();
			LogHelper.log(Level.SEVERE, "Failed to Register Rotor: " + showedName);
		}
	}
	
}
