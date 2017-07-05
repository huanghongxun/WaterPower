/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.turbine

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
import waterpower.common.block.container.ContainerRotor
import waterpower.util.DEFAULT_DECIMAL_FORMAT
import java.io.IOException

@SideOnly(Side.CLIENT)
class GuiTurbine(player: EntityPlayer, private val gen: TileEntityTurbine) : GuiBase(ContainerRotor(player, gen)) {

    private var btnEnergyType: GuiButton? = null

    init {
        allowUserInput = false
    }

    override fun initGui() {
        super.initGui()

        val l = (width - xSize) / 2
        val i1 = (height - ySize) / 2
        btnEnergyType = GuiButton(1, l + 100, i1 + 24, 30, 20, gen.getEnergyUnit().name)
        this.buttonList.add(btnEnergyType)
    }

    override fun drawGuiContainerBackgroundLayer(f: Float, i: Int, j: Int) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        mc.renderEngine.bindTexture(ResourceLocation("${WaterPower.MOD_ID}:textures/gui/turbine.png"))
        val l = (width - xSize) / 2
        val i1 = (height - ySize) / 2
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize)
    }

    override fun drawGuiContainerForegroundLayer(par1: Int, par2: Int) {
        fontRenderer.drawString(gen.getName(), 8, 6, 0x404040)
        fontRenderer.drawString(i18n("waterpower.watermill.rotor") + ":", 44, 30, 0x404040)
        fontRenderer.drawString(i18n("container.inventory"), 8, ySize - 96 + 2, 0x404040)
        fontRenderer.drawString(
                i18n("waterpower.watermill.output") + ": " + DEFAULT_DECIMAL_FORMAT.format(gen.getFromEU(gen.latestOutput))
                        + gen.getEnergyUnit().name + "/t", 8, 50, 0x404040)
    }

    @Throws(IOException::class)
    override fun actionPerformed(p_146284_1_: GuiButton?) {
        super.actionPerformed(p_146284_1_)

        when (p_146284_1_!!.id) {
            1 -> {
                val newType = Energy.values()[(gen.getEnergyUnit().ordinal + 1) % Energy.values().size]
                btnEnergyType!!.displayString = newType.name
                gen.onUnitChanged(newType)
            }
        }
    }
}