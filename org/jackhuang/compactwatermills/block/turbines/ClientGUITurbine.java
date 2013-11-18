package org.jackhuang.compactwatermills.block.turbines;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.jackhuang.compactwatermills.ContainerRotor;
import org.jackhuang.compactwatermills.Reference;
import org.lwjgl.opengl.GL11;

public class ClientGUITurbine extends GuiContainer {
	
	public static ClientGUITurbine makeGUI(EntityPlayer player,
			TileEntityTurbine gen) {
		return new ClientGUITurbine(player, gen);
	}

	public static Container makeContainer(EntityPlayer player,
			TileEntityTurbine gen) {
		return new ContainerRotor(player, gen);
	}

	private TileEntityTurbine gen;

	protected ClientGUITurbine(EntityPlayer player, TileEntityTurbine gen) {
		super(makeContainer(player, gen));
		this.gen = gen;
		allowUserInput = false;
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
		fontRenderer.drawString(gen.getInvName(), 8, 6, 0x404040);
		fontRenderer.drawString("转子:", 44, 30, 0x404040);
		fontRenderer.drawString("物品栏", 8, ySize - 96 + 2, 0x404040);
		fontRenderer.drawString("当前输出: " + gen.getOfferedEnergy() + "EU/t", 8,
				50, 0x404040);
		//Reservoir r = gen.getCurrentReservoir();
		//fontRenderer.drawString("当前容积: " + (r == null ? 0 : r.getMaxWater())
		//		+ "桶(b)", 8, 60, 0x404040);
		fontRenderer.drawString("当前转速: " + gen.speed + "r/t", 80, 50, 0x404040);
		//DecimalFormat dFormat = new DecimalFormat("#.00");
		//fontRenderer.drawString("当前水量: " + dFormat.format(gen.getWater())
		//		+ "桶(b)", 80, 60, 0x404040);
	}
}
