/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.watermill

import net.minecraft.client.gui.GuiButton
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import waterpower.WaterPower
import waterpower.client.GuiBase
import waterpower.client.i18n
import waterpower.common.Energy
import waterpower.util.DEFAULT_DECIMAL_FORMAT
import java.io.IOException

@SideOnly(Side.CLIENT)
class GuiWatermill(player: EntityPlayer, val te: TileEntityWatermill)
    : GuiBase(ContainerWatermill(player, te)) {

    lateinit var btnEnergyType: GuiButton

    override fun initGui() {
        super.initGui()

        val l = (width - xSize) / 2
        val i1 = (height - ySize) / 2
        btnEnergyType = GuiButton(1, l + 100, i1 + 24, 30, 20, te.getEnergyUnit().name)
        this.buttonList.add(btnEnergyType)
    }

    override fun drawGuiContainerBackgroundLayer(f: Float, i: Int, j: Int) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        mc.renderEngine.bindTexture(ResourceLocation("${WaterPower.MOD_ID}:textures/gui/watermill.png"))
        val l = (width - xSize) / 2
        val i1 = (height - ySize) / 2
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize)
    }

    override fun drawGuiContainerForegroundLayer(par1: Int, par2: Int) {

        fontRenderer.drawString(te.getName(), 8, 6, 0x404040)
        fontRenderer.drawString(i18n("waterpower.rotor") + ":", 44, 30, 0x404040)
        fontRenderer.drawString(
                i18n("waterpower.watermill.output") + ": " + DEFAULT_DECIMAL_FORMAT.format(te.getFromEU(te.latestOutput))
                        + te.getEnergyUnit().name + "/t", 8, 45, 0x404040)
        fontRenderer
                .drawString(StringBuilder().append(i18n("waterpower.watermill.check_water")).append(',')
                        .append(i18n("waterpower.watermill.check_lava")).append(':')
                        .append(if (te.isRangeSupported()) ("" + te.waterBlocks + "," + te.lavaBlocks)
                        else i18n("waterpower.watermill.cannot_check")).toString(), 8, 55, 0x404040)
        val a = te.getRange()
        val b = a * a * a - 1
        fontRenderer.drawString(i18n("waterpower.watermill.need") + ":" + b + "=" + a + "^3-1", 8, 65, 0x404040)
    }

    @Throws(IOException::class)
    override fun actionPerformed(p_146284_1_: GuiButton?) {
        super.actionPerformed(p_146284_1_)

        when (p_146284_1_!!.id) {
            1 -> {
                val newType = Energy.values()[(te.energyType + 1) % Energy.values().size]
                btnEnergyType.displayString = newType.name
                te.onUnitChanged(newType)
            }
        }
    }

}