package org.jackhuang.compactwatermills.common.block.turbines;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.client.gui.ContainerRotor;
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
		fontRendererObj.drawString("转子:", 44, 30, 0x404040);
		fontRendererObj.drawString("物品栏", 8, ySize - 96 + 2, 0x404040);
		fontRendererObj.drawString("当前输出: " + gen.getOfferedEnergy() + "EU/t", 8,
				50, 0x404040);
		//Reservoir r = gen.getCurrentReservoir();
		//fontRenderer.drawString("当前容积: " + (r == null ? 0 : r.getMaxWater())
		//		+ "桶(b)", 8, 60, 0x404040);
		//fontRenderer.drawString("当前转速: " + gen.speed + "r/t", 80, 50, 0x404040);
		//DecimalFormat dFormat = new DecimalFormat("#.00");
		//fontRenderer.drawString("当前水量: " + dFormat.format(gen.getWater())
		//		+ "桶(b)", 80, 60, 0x404040);
	}
}
