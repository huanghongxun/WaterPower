package org.jackhuang.compactwatermills.client.gui;

import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.block.reservoir.TileEntityReservoir;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class ClientGUIReservoir extends GuiContainer {
	public static ClientGUIReservoir makeGUI(EntityPlayer player,
			TileEntityReservoir gen) {
		return new ClientGUIReservoir(player, gen);
	}

	public static Container makeContainer(EntityPlayer player,
			TileEntityReservoir gen) {
		return new ContainerReservoir(player, gen);
	}

	private TileEntityReservoir gen;

	protected ClientGUIReservoir(EntityPlayer player, TileEntityReservoir gen) {
		super(makeContainer(player, gen));
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
		fontRenderer.drawString(gen.getInvName(), 8, 6, 0x404040);
		fontRenderer.drawString("物品栏", 8, ySize - 96 + 2, 0x404040);
		fontRenderer.drawString("当前容积/mb", 12, 20, 0x404040);
		fontRenderer.drawString("" + gen.getMaxWater(), 20, 30, 0x404040);
		fontRenderer.drawString("当前水量/mb", 12, 40, 0x404040);
		fontRenderer.drawString("" + gen.getWater(), 20, 50, 0x404040);
	}
}
