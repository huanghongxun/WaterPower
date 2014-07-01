package org.jackhuang.compactwatermills;

import java.util.HashMap;

public final class DefaultIds {
	private static HashMap<InternalName, Integer> idMap = new HashMap<InternalName, Integer>();

	public static int get(InternalName name) {
		if (!idMap.containsKey(name)) {
			throw new IllegalArgumentException("default id for " + name
					+ " not registered");
		}
		return ((Integer) idMap.get(name)).intValue();
	}

	private static void add(InternalName name, int id) {
		if (idMap.containsValue(Integer.valueOf(id))) {
			throw new RuntimeException("duplicate default id: " + id);
		}

		idMap.put(name, Integer.valueOf(id));
	}

	static {
		add(InternalName.cptBlockCompactWatermill, 2048);
		add(InternalName.cptBlockTurbine, 2047);
		add(InternalName.cptBlockReservoir, 2046);
		add(InternalName.cptBlockOre, 2045);
		add(InternalName.cptBlockMachine, 2044);
		add(InternalName.cptItemPlugins, 31699);
		add(InternalName.cptItemUpdaters, 31700);
		add(InternalName.cptItemMeterial, 31701);
		add(InternalName.cptItemOreDust, 31702);
		add(InternalName.cptItemCrafting, 31704);
		add(InternalName.cptItemRange, 31705);
		add(InternalName.cptItemTrousers, 31706);
	}
}
