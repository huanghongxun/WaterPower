package org.jackhuang.compactwatermills.watermills;

import org.jackhuang.compactwatermills.Reference;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ClientGUIWatermill extends GuiContainer {
	
	public static ClientGUIWatermill makeGUI(WaterType type, IInventory inventory,
		TileEntityWatermill tileEntityCW) {
		return new ClientGUIWatermill(type, inventory, tileEntityCW);
	}
	
	protected static Container makeContainer(IInventory playerInventory, TileEntityWatermill watermill) {
		return new ContainerCompactWatermills(playerInventory, watermill);
	}
	
	private WaterType type;
	
	private ContainerCompactWatermills container;
	
	private ClientGUIWatermill(WaterType type, IInventory inventory, TileEntityWatermill tileEntityCW) {
		super(makeContainer(inventory, tileEntityCW));
		this.type = type;
		allowUserInput = false;
		container = (ContainerCompactWatermills) makeContainer(inventory,
			tileEntityCW);
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
		fontRenderer.drawString(type.showedName, 8, 6, 0x404040);
		fontRenderer.drawString("转子:", 44, 30, 0x404040);
		fontRenderer.drawString("物品栏", 8, ySize - 96 + 2, 0x404040);
		fontRenderer.drawString(
			"当前输出: "
				+ container.tileEntity.getOutputUntilNexttTick()
				+ "EU/t", 8, 50, 0x404040);
	}
	
}
