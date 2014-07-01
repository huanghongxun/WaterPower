package org.jackhuang.compactwatermills.common.block.watermills;

import java.text.DecimalFormat;

import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.client.gui.ContainerRotor;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityBaseGenerator;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public class GuiWatermill extends GuiContainer {
	private TileEntityWatermill gen;

	private ContainerWatermill container;
	private DecimalFormat df;

	public GuiWatermill(EntityPlayer player, TileEntityWatermill gen) {
		super(new ContainerWatermill(player, gen));
		this.gen = gen;
		allowUserInput = false;
		container = new ContainerWatermill(player, gen);
		df = new DecimalFormat("#.00");
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.renderEngine.bindTexture(new ResourceLocation(Reference.ModID
				+ ":textures/gui/GUIWatermill.png"));
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {

		fontRendererObj.drawString(gen.getInventoryName(), 8, 6, 0x404040);
		fontRendererObj.drawString(
				StatCollector.translateToLocal("cptwtrml.rotor.ROTOR") + ":",
				44, 30, 0x404040);
		fontRendererObj.drawString(
				StatCollector.translateToLocal("container.inventory"), 8,
				ySize - 96 + 2, 0x404040);
		fontRendererObj.drawString(
				StatCollector.translateToLocal("cptwtrml.watermill.OUTPUT")
						+ ": "
						+ df.format(container.tileEntity.getOfferedEnergy())
						+ "EU/t", 8, 40, 0x404040);
		boolean w = gen.waterBlocks == -1;
		fontRendererObj
				.drawString(
						StatCollector
								.translateToLocal("cptwtrml.watermill.CHECK_WATER")
								+ ","
								+ StatCollector
										.translateToLocal("cptwtrml.watermill.CHECK_LAVA")
								+ ":"
								+ (w ? StatCollector
										.translateToLocal("cptwtrml.watermill.CANNOT_CHECK")
										: gen.waterBlocks + ","
												+ gen.lavaBlocks), 8, 50,
						0x404040);
		fontRendererObj.drawString(StatCollector.translateToLocal("cptwtrml.watermill.PRODUCTION") + ":" + gen.production, 8, 70, 0x404040);
		int a = gen.getRange();
		int b = a * a * a - 1;
		fontRendererObj.drawString(
				StatCollector.translateToLocal("cptwtrml.watermill.NEED") + ":"
						+ b + "=" + a + "^3-1", 8, 60, 0x404040);
	}

}