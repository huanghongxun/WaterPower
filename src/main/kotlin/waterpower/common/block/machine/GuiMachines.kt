/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.machine

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.WaterPower
import waterpower.client.i18n

@SideOnly(Side.CLIENT)
class GuiCrusher(player: EntityPlayer, te: TileEntityCrusher)
    : GuiMachineBase(ContainerBaseMachine(player, te),
        i18n("waterpower.machine.crusher"),
        ResourceLocation("${WaterPower.MOD_ID}:textures/gui/crusher.png"))

@SideOnly(Side.CLIENT)
class GuiCutter(player: EntityPlayer, te: TileEntityCutter)
    : GuiMachineBase(ContainerBaseMachine(player, te),
        i18n("waterpower.machine.cutter"),
        ResourceLocation("${WaterPower.MOD_ID}:textures/gui/cutter.png"))

@SideOnly(Side.CLIENT)
class GuiLathe(player: EntityPlayer, te: TileEntityLathe)
    : GuiMachineBase(ContainerBaseMachine(player, te),
        i18n("waterpower.machine.lathe"),
        ResourceLocation("${WaterPower.MOD_ID}:textures/gui/lathe.png"))

@SideOnly(Side.CLIENT)
class GuiCompressor(player: EntityPlayer, te: TileEntityCompressor)
    : GuiMachineBase(ContainerBaseMachine(player, te),
        i18n("waterpower.machine.compressor"),
        ResourceLocation("${WaterPower.MOD_ID}:textures/gui/compressor.png"))

@SideOnly(Side.CLIENT)
class GuiAdvCompressor(player: EntityPlayer, te: TileEntityAdvCompressor)
    : GuiMachineBase(ContainerBaseMachine(player, te),
        i18n("waterpower.machine.advanced_compressor"),
        ResourceLocation("${WaterPower.MOD_ID}:textures/gui/compressor.png"))

@SideOnly(Side.CLIENT)
class GuiSawmill(player: EntityPlayer, te: TileEntitySawmill)
    : GuiMachineBase(ContainerBaseMachine(player, te),
        i18n("waterpower.machine.sawmill"),
        ResourceLocation("${WaterPower.MOD_ID}:textures/gui/sawmill.png"))

@SideOnly(Side.CLIENT)
class GuiCentrifuge(player: EntityPlayer, te: TileEntityCentrifuge)
    : GuiMachineBase(ContainerCentrifuge(player, te),
        i18n("waterpower.machine.centrifuge"),
        ResourceLocation("${WaterPower.MOD_ID}:textures/gui/centrifuge.png"))