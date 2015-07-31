/**
 * Copyright (c) Huang Yuhui, 2014
 *
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.common.block.machines;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.jackhuang.watercraft.client.gui.ContainerStandardMachine;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiMachineBase extends GuiContainer {

    public GuiMachineBase(ContainerStandardMachine par1Container) {
	super(par1Container);

	this.container = par1Container;
    }

    public ContainerStandardMachine container;
    public String name;
    public String inv;
    protected ResourceLocation background;

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	this.fontRendererObj.drawString(this.name,
		(this.xSize - this.fontRendererObj.getStringWidth(this.name)) / 2,
		6, 0x404040);
	this.fontRendererObj
		.drawString(this.inv, 8, this.ySize - 96 + 2, 0x404040);

	this.fontRendererObj.drawString("Stored(mb): "
		+ this.container.tileEntity.water, 8, this.ySize - 105 + 2,
		0x404040);

	this.fontRendererObj.drawString("Using(mb/t): "
		+ this.container.tileEntity.energyConsume, 8, this.ySize - 114 + 2,
		0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	this.mc.getTextureManager().bindTexture(background);
	int j = (this.width - this.xSize) / 2;
	int k = (this.height - this.ySize) / 2;
	drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);

	int progress = (int) (24.0F * this.container.tileEntity.getProgress());

	if (progress > 0) {
	    drawTexturedModalRect(j + 79 - 30, k + 34, 176, 14, progress + 1, 16);
	}
    }

}
