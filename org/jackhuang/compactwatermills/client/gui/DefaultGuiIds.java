package org.jackhuang.compactwatermills.client.gui;

import java.util.HashMap;

public final class DefaultGuiIds {
	private static HashMap<String, Integer> idMap = new HashMap<String, Integer>();

	public static int get(String name) {
		if (!idMap.containsKey(name)) {
			throw new IllegalArgumentException("default id for " + name
					+ " not registered");
		}
		return idMap.get(name);
	}

	private static void add(String name, int id) {
		if (idMap.containsValue(Integer.valueOf(id))) {
			throw new RuntimeException("duplicate default id: " + id);
		}

		idMap.put(name, Integer.valueOf(id));
	}

	static {
		int i = 0;
		add("tileEntityTurbine", i++);
		add("tileEntityWatermill", i++);
		add("tileEntityReservoir", i++);
	}
}
