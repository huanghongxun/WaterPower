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
		add(InternalName.blockCompactWatermill, 2048);
		add(InternalName.blockTurbine, 2047);
		add(InternalName.blockReservoir, 2046);
	}
}
