/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.reservoir

import com.google.common.collect.ImmutableList
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import waterpower.WaterPower
import waterpower.client.i18n
import waterpower.client.render.getFluidSheet
import waterpower.client.render.getFluidTexture

@SideOnly(Side.CLIENT)
class GuiReservoir(player: EntityPlayer, private val gen: TileEntityReservoir) : GuiContainer(ContainerReservoir(player, gen)) {

    init {
        allowUserInput = false
    }

    override fun drawGuiContainerBackgroundLayer(f: Float, i: Int, j: Int) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        mc.renderEngine.bindTexture(ResourceLocation("${WaterPower.MOD_ID}:textures/gui/reservoir.png"))
        val l = (width - xSize) / 2
        val i1 = (height - ySize) / 2
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize)
    }

    override fun drawGuiContainerForegroundLayer(par1: Int, par2: Int) {
        val f = gen.getFluidTank()

        fontRendererObj.drawString(gen.name, 8, 6, 0x404040)
        fontRendererObj.drawString(i18n("container.inventory"), 8, ySize - 96 + 2, 0x404040)
        fontRendererObj.drawString(i18n("waterpower.gui.reservoir.add") + ": " + gen.getLastAddedWater(), 12, 20, 0x404040)
        fontRendererObj.drawString(i18n("waterpower.gui.capacity") + ": " + f.capacity, 12, 30, 0x404040)
        fontRendererObj.drawString(i18n("waterpower.gui.stored") + ": " + f.fluidAmount, 12, 40, 0x404040)

        val fluid = getFluidTexture(f.fluid, false)
        val percent = f.fluidAmount.toFloat() / f.capacity
        mc.renderEngine.bindTexture(getFluidSheet(f.fluid?.fluid))

        GL11.glColor4f(1f, 1f, 1f, 1f)
        val h = (13.0 * percent).toInt()
        drawTexturedModalRect(130, 49 - h, fluid, 12, h)

        val l = (width - xSize) / 2
        val i1 = (height - ySize) / 2
        val x = par1 - l
        val y = par2 - i1
        if (x in 130..141 && y in 36..48) {
            drawHoveringText(ImmutableList.of(if (f.fluid == null) i18n("waterpower.gui.empty") else f.fluid!!.localizedName, "${f.fluidAmount}/${f.capacity}mb"), x, y, fontRendererObj)
        }
    }
}