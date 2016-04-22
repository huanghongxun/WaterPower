package org.jackhuang.watercraft.client.gui;

import java.util.HashMap;

public final class DefaultGuiIds {

    private static final HashMap<String, Integer> idMap = new HashMap<String, Integer>();

    private DefaultGuiIds() {
    }

    public static int get(String name) {
        if (!idMap.containsKey(name)) {
            throw new IllegalArgumentException("default id for " + name + " is not registered.");
        }
        return idMap.get(name);
    }

    private static void add(String name, int id) {
        idMap.put(name, id);
    }

    static {
        int i = 0;
        add("tileEntityTurbine", i++);
        add("tileEntityWatermill", i++);
        add("tileEntityReservoir", i++);
        add("tileEntityMacerator", i++);
        add("tileEntityCompressor", i++);
        add("tileEntityLathe", i++);
        add("tileEntityCutter", i++);
        add("tileEntitySawmill", i++);
        add("tileEntityAdvancedCompressor", i++);
        add("tileEntityCentrifuge", i++);
    }
}
