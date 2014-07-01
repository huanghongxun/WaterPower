package org.jackhuang.compactwatermills.client.gui;

import java.util.HashMap;

import org.jackhuang.compactwatermills.common.block.machines.*;
import org.jackhuang.compactwatermills.common.block.reservoir.ContainerReservoir;
import org.jackhuang.compactwatermills.common.block.reservoir.GuiReservoir;
import org.jackhuang.compactwatermills.common.block.turbines.GuiTurbine;
import org.jackhuang.compactwatermills.common.block.watermills.ContainerWatermill;
import org.jackhuang.compactwatermills.common.block.watermills.GuiWatermill;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;

public final class DefaultGuiIds {
	public static final class Data {
		public int id;
		public Class<? extends Container> containerClass;
		public Class<? extends GuiScreen> guiClass;
		
		public Data(int id, Class<? extends Container> containerClass,
				Class<? extends GuiScreen> guiClass) {
			super();
			this.id = id;
			this.containerClass = containerClass;
			this.guiClass = guiClass;
		}
	}
	
	private static HashMap<String, Data> idMap = new HashMap<String, Data>();

	public static Data get(String name) {
		if (!idMap.containsKey(name)) {
			throw new IllegalArgumentException("default id for " + name
					+ " not registered");
		}
		return idMap.get(name);
	}

	private static void add(String name, Data id) {
		idMap.put(name, id);
	}

	static {
		int i = 0;
		add("tileEntityTurbine", new Data(i++, ContainerRotor.class, GuiTurbine.class));
		add("tileEntityWatermill", new Data(i++, ContainerWatermill.class, GuiWatermill.class));
		add("tileEntityReservoir", new Data(i++, ContainerReservoir.class, GuiReservoir.class));
		add("tileEntityMacerator", new Data(i++, ContainerStandardMachine.class, GuiMacerator.class));
		add("tileEntityCompressor", new Data(i++, ContainerStandardMachine.class, GuiCompressor.class));
		add("tileEntityLathe", new Data(i++, ContainerStandardMachine.class, GuiLathe.class));
		add("tileEntityCutter", new Data(i++, ContainerStandardMachine.class, GuiCutter.class));
		add("tileEntitySawmill", new Data(i++, ContainerStandardMachine.class, GuiSawmill.class));
		add("tileEntityAdvancedCompressor", new Data(i++, ContainerStandardMachine.class, GuiCompressor.class));
		//add("tileEntityCentrifuge", new Data(i++, ContainerStandardMachine.class, GuiTurbine.class));
	}
}
