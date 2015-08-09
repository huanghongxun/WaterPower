package org.jackhuang.watercraft.common.block.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.client.gui.ContainerStandardMachine;

public class GuiCutter extends GuiMachineBase {
	public GuiCutter(EntityPlayer player,
			TileEntityStandardWaterMachine tileEntity) {
		super(new ContainerStandardMachine(player, tileEntity));

		this.name = StatCollector
				.translateToLocal("cptwtrml.machine.cutter.name");
		this.inv = StatCollector.translateToLocal("container.inventory");
		this.background = new ResourceLocation(Reference.ModID + ":textures/gui/GUICutter.png");
	}
}