/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.block.watermills;

import java.text.DecimalFormat;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.client.gui.ContainerRotor;
import org.jackhuang.watercraft.common.EnergyType;
import org.jackhuang.watercraft.common.network.MessagePacketHandler;
import org.jackhuang.watercraft.common.network.PacketUnitChanged;
import org.jackhuang.watercraft.common.tileentity.TileEntityGenerator;
import org.jackhuang.watercraft.util.Utils;
import org.jackhuang.watercraft.util.WPLog;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.DimensionManager;

@SideOnly(Side.CLIENT)
public class GuiWatermill extends GuiContainer {
    private TileEntityWatermill gen;

    private ContainerWatermill container;
    private GuiButton btnEnergyType;

    public GuiWatermill(EntityPlayer player, TileEntityWatermill gen) {
        super(new ContainerWatermill(player, gen));
        this.gen = gen;
        allowUserInput = false;
        container = new ContainerWatermill(player, gen);
    }

    @Override
    public void initGui() {
        super.initGui();

        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        btnEnergyType = new GuiButton(1, l + 100, i1 + 24, 30, 20,
                gen.energyType.name());
        this.buttonList.add(btnEnergyType);
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

        fontRendererObj.drawString(gen.getInventoryName(), 8, 6, 0x404040);
        fontRendererObj.drawString(
                StatCollector.translateToLocal("cptwtrml.rotor.ROTOR") + ":",
                44, 30, 0x404040);
        fontRendererObj.drawString(
                StatCollector.translateToLocal("cptwtrml.watermill.OUTPUT")
                        + ": " + Utils.DEFAULT_DECIMAL_FORMAT.format(gen.getFromEU(gen.latestOutput))
                        + gen.energyType.name() + "/t", 8, 45, 0x404040);
        fontRendererObj
                .drawString(
                        StatCollector
                                .translateToLocal("cptwtrml.watermill.CHECK_WATER")
                                + ","
                                + StatCollector
                                        .translateToLocal("cptwtrml.watermill.CHECK_LAVA")
                                + ":"
                                + (gen.isRangeSupported() ? gen.waterBlocks
                                        + "," + gen.lavaBlocks
                                        : StatCollector
                                                .translateToLocal("cptwtrml.watermill.CANNOT_CHECK")),
                        8, 55, 0x404040);
        int a = gen.getRange();
        int b = a * a * a - 1;
        fontRendererObj.drawString(
                StatCollector.translateToLocal("cptwtrml.watermill.NEED") + ":"
                        + b + "=" + a + "^3-1", 8, 65, 0x404040);
        fontRendererObj.drawString(
                StatCollector.translateToLocal("cptwtrml.watermill.PRODUCTION")
                        + ":" + Utils.DEFAULT_DECIMAL_FORMAT.format(gen.getFromEU(gen.production))
                        + gen.energyType.name(), 8, 75, 0x404040);
    }

    @Override
    protected void actionPerformed(GuiButton p_146284_1_) {
        super.actionPerformed(p_146284_1_);

        switch (p_146284_1_.id) {
        case 1:
            gen.energyType = EnergyType.values()[(gen.energyType.ordinal() + 1)
                    % EnergyType.values().length];
            btnEnergyType.displayString = gen.energyType.name();
            MessagePacketHandler.INSTANCE
                    .sendToServer(new PacketUnitChanged(
                            Minecraft.getMinecraft().thePlayer.worldObj.provider.dimensionId,
                            gen.xCoord, gen.yCoord, gen.zCoord, gen.energyType
                                    .ordinal()));
            break;
        }
    }

}