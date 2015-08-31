package org.jackhuang.watercraft.client.gui;

import java.util.HashMap;

import org.jackhuang.watercraft.common.block.machines.*;
import org.jackhuang.watercraft.common.block.reservoir.ContainerReservoir;
import org.jackhuang.watercraft.common.block.reservoir.GuiReservoir;
import org.jackhuang.watercraft.common.block.turbines.GuiTurbine;
import org.jackhuang.watercraft.common.block.watermills.ContainerWatermill;
import org.jackhuang.watercraft.common.block.watermills.GuiWatermill;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;

public final class DefaultGuiIds {
    
    private static HashMap<String, Integer> idMap = new HashMap<String, Integer>();

    public static int get(String name) {
        if (!idMap.containsKey(name)) {
            throw new IllegalArgumentException("default id for " + name
                    + " is not registered.");
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
