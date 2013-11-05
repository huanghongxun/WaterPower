package org.jackhuang.compactwatermills;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ClientGUIRotor extends GuiContainer {
	
	public static ClientGUIRotor makeGUI(IInventory inventory,
		TileEntityBaseGenerator gen) {
		return new ClientGUIRotor(inventory, gen);
	}
	
	protected static Container makeContainer(IInventory playerInventory,
			TileEntityBaseGenerator gen) {
		return new ContainerRotor(playerInventory, gen);
	}
	
	private TileEntityBaseGenerator gen;
	
	private ContainerRotor container;
	
	private ClientGUIRotor(IInventory inventory, TileEntityBaseGenerator gen) {
		super(makeContainer(inventory, gen));
		this.gen = gen;
		allowUserInput = false;
		container = (ContainerRotor) makeContainer(inventory,
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