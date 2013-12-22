package org.jackhuang.compactwatermills.gui;

import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.tileentity.TileEntityBaseGenerator;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ClientGUIRotor extends GuiContainer {
	
	public static ClientGUIRotor makeGUI(EntityPlayer player,
		TileEntityBaseGenerator gen) {
		return new ClientGUIRotor(player, gen);
	}
	
	public static Container makeContainer(EntityPlayer player,
			TileEntityBaseGenerator gen) {
		return new ContainerRotor(player, gen);
	}
	
	private TileEntityBaseGenerator gen;
	
	private ContainerRotor container;
	
	protected ClientGUIRotor(EntityPlayer player, TileEntityBaseGenerator gen) {
		super(makeContainer(player, gen));
		this.gen = gen;
		allowUserInput = false;
		container = (ContainerRotor) makeContainer(player,
				gen);
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
		fontRenderer.drawString(
			"当前输出: "
				+ container.tileEntity.getOfferedEnergy()
				+ "EU/t", 8, 50, 0x404040);
	}
	
}