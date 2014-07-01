package org.jackhuang.compactwatermills.common.block.machines;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.client.gui.ContainerStandardMachine;
import org.jackhuang.compactwatermills.client.gui.GuiMachineBase;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityStandardWaterMachine;

@SideOnly(Side.CLIENT)
public class GuiCompressor extends GuiMachineBase {

	public GuiCompressor(EntityPlayer player,
			TileEntityStandardWaterMachine tileEntity) {
		super(new ContainerStandardMachine(player, tileEntity));

		this.name = StatCollector
				.translateToLocal("cptwtrml.machine.compressor.name");
		this.inv = StatCollector.translateToLocal("container.inventory");
		this.background = new ResourceLocation(Reference.ModID + ":textures/gui/GUICompressor.png");
	}
}
