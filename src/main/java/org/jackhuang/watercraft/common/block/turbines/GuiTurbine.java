package org.jackhuang.watercraft.common.block.turbines;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.client.gui.ContainerRotor;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTurbine extends GuiContainer {
	private TileEntityTurbine gen;

	public GuiTurbine(EntityPlayer player, TileEntityTurbine gen) {
		super(new ContainerRotor(player, gen));
		this.gen = gen;
		allowUserInput = false;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.renderEngine.bindTexture(new ResourceLocation(Reference.ModID
				+ ":textures/gui/GUITurbine.png"));
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRendererObj.drawString(gen.getInventoryName(), 8, 6, 0x404040);
		fontRendererObj.drawString(StatCollector.translateToLocal("cptwtrml.watermill.ROTOR") + ":", 44, 30, 0x404040);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 0x404040);
		fontRendererObj.drawString(StatCollector.translateToLocal("cptwtrml.watermill.OUTPUT") + ": " + gen.getOfferedEnergy() + "EU/t", 8,
				50, 0x404040);
	}
}
