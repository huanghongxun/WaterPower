package org.jackhuang.watercraft.common.block.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.client.gui.ContainerStandardMachine;
import org.jackhuang.watercraft.client.gui.GuiMachineBase;
import org.jackhuang.watercraft.common.tileentity.TileEntityStandardWaterMachine;

public class GuiCentrifuge extends GuiMachineBase {
	public GuiCentrifuge(EntityPlayer player,
			TileEntityCentrifuge tileEntity) {
		super(new ContainerCentrifuge(player, tileEntity));

		this.name = StatCollector
				.translateToLocal("cptwtrml.machine.centrifuge.name");
		this.inv = StatCollector.translateToLocal("container.inventory");
		this.background = new ResourceLocation(Reference.ModID + ":textures/gui/GUICentrifuge.png");
	}
}