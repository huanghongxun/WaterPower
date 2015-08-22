package org.jackhuang.watercraft.common;

import java.util.HashMap;
import java.util.Properties;

import org.jackhuang.watercraft.WaterPower;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public final class DefaultIDs {
	private static HashMap<String, Integer> idMap = new HashMap();

	public static int get(String name) {
		if (!idMap.containsKey(name)) {
			throw new IllegalArgumentException("default id for " + name
					+ " not registered");
		}

		return ((Integer) idMap.get(name)).intValue();
	}

	private static void add(String name, int id) {
		if (idMap.containsValue(Integer.valueOf(id))) {
			throw new RuntimeException("duplicate default id: " + id);
		}

		idMap.put(name, Integer.valueOf(id));
	}

	static {
		add("cptBlockCompactWatermill", 2048);
		add("cptBlockTurbine", 2047);
		add("cptBlockReservoir", 2046);
		add("cptBlockMachine", 2045);
		add("cptBlockOre", 2044);
		add("cptItemUpdaters", 31700);
		add("cptItemPlugins", 31701);
		add("cptItemRange", 31702);
		add("cptItemCrafting", 31703);
		add("cptItemMeterial", 31704);
		add("cptItemOreDust", 31705);
	}

	public static Properties runtimeIdProperties = new Properties();
	
	public static int getBlockIdFor(String id) {
		return getBlockIdFor(id, get(id));
	}

	public static int getBlockIdFor(String internalName, int standardId) {
		Configuration config = WaterPower.instance.config;
		String name = internalName;

		Property prop = null;
		Integer ret;
		if (config == null) {
			ret = Integer.valueOf(standardId);
		} else {
			prop = config.getBlock(name, standardId);
			ret = Integer.valueOf(prop.getInt(standardId));
		}

		if ((ret.intValue() <= 0) || (ret.intValue() > Block.blocksList.length)) {
			displayError("An invalid block ID has been detected on your Compact Watermills\nconfiguration file. Block IDs cannot be higher than "
					+ (Block.blocksList.length - 1)
					+ ".\n"
					+ "\n"
					+ "Block with invalid ID: "
					+ name
					+ "\n"
					+ "Invalid ID: "
					+ ret);
		}

		if ((Block.blocksList[ret.intValue()] != null)
				|| (Item.itemsList[ret.intValue()] != null)) {
			String occupiedBy;
			if (Block.blocksList[ret.intValue()] != null) {
				occupiedBy = "block " + Block.blocksList[ret.intValue()] + " ("
						+ Block.blocksList[ret.intValue()].getUnlocalizedName()
						+ ")";
			} else {
				occupiedBy = "item " + Item.itemsList[ret.intValue()] + " ("
						+ Item.itemsList[ret.intValue()].getUnlocalizedName()
						+ ")";
			}

			displayError("A conflicting block ID has been detected on your WaterPower\nconfiguration file. Block IDs cannot be used more than once.\n\nBlock with invalid ID: "
					+ name
					+ "\n"
					+ "Invalid ID: "
					+ ret
					+ "\n"
					+ "Already occupied by: " + occupiedBy);
		}

		runtimeIdProperties.setProperty("block." + name, ret.toString());

		return ret.intValue();
	}
	
	public static int getItemIdFor(String id) {
		return getItemIdFor(id, get(id));
	}

	public static int getItemIdFor(String internalName, int standardId) {
		Configuration config = WaterPower.instance.config;
		String name = internalName;

		Property prop = null;
		Integer ret;
		if (config == null) {
			ret = Integer.valueOf(standardId);
		} else {
			prop = config.getItem(name, standardId);
			ret = Integer.valueOf(prop.getInt(standardId));
		}

		if ((ret.intValue() <= 0) || (ret.intValue() > Item.itemsList.length)) {
			displayError("An invalid item ID has been detected on your Compact Watermills\nconfiguration file. Item IDs cannot be higher than "
					+ (Item.itemsList.length - 1)
					+ ".\n"
					+ "\n"
					+ "Item with invalid ID: "
					+ name
					+ "\n"
					+ "Invalid ID: "
					+ ret);
		}

		if (Item.itemsList[ret.intValue()] != null) {
			String occupiedBy = "item " + Item.itemsList[ret.intValue()] + " ("
					+ Item.itemsList[ret.intValue()].getUnlocalizedName() + ")";

			displayError("A conflicting item ID has been detected on your Compact Watermills\nconfiguration file. Item IDs cannot be used more than once.\n\nItem with invalid ID: "
					+ name
					+ "\n"
					+ "Invalid ID: "
					+ ret
					+ "\n"
					+ "Already occupied by: " + occupiedBy);
		}

		runtimeIdProperties.setProperty("item." + name, ret.toString());

		return ret.intValue();
	}

	public static void displayError(String msg) {
		throw new RuntimeException("\n=========Compact Watermills=========\n"
				+ msg);
	}
}