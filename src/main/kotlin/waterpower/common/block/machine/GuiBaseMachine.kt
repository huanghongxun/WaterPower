/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.machine

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import waterpower.client.GuiBase
import waterpower.client.i18n

@SideOnly(Side.CLIENT)
abstract class GuiMachineBase(val container: ContainerBaseMachine,
                              val name: String,
                              val background: ResourceLocation) : GuiBase(container) {
    val inv = i18n("container.inventory")

    override fun drawGuiContainerForegroundLayer(par1: Int, par2: Int) {
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 6, 0x404040)
        this.fontRenderer.drawString(this.inv, 8, this.ySize - 96 + 2, 0x404040)

        this.fontRenderer.drawString(i18n("waterpower.gui.stored") + ": " + this.container.tileEntity.getEnergy().energyStored + "mb", 8,
                this.ySize - 105 + 2, 0x404040)

        this.fontRenderer.drawString(i18n("waterpower.gui.using") + ": " + this.container.tileEntity.energyConsume + "mb/t", 8,
                this.ySize - 114 + 2, 0x404040)
    }

    override fun drawGuiContainerBackgroundLayer(f: Float, x: Int, y: Int) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        this.mc.textureManager.bindTexture(background)
        val j = (this.width - this.xSize) / 2
        val k = (this.height - this.ySize) / 2
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize)

        val progress = (24.0f * this.container.tileEntity.getProgress()).toInt()

        if (progress > 0)
            drawTexturedModalRect(j + 79 - 30, k + 34, 176, 14, progress + 1, 16)
    }

}
