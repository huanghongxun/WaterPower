/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.watermill

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import waterpower.annotations.HasGui
import waterpower.annotations.Register
import waterpower.annotations.Sync
import waterpower.common.block.inventory.InventorySlotRange
import waterpower.common.block.tile.TileEntityRotorGenerator
import waterpower.common.init.WPBlocks
import waterpower.common.item.ItemRange
import waterpower.common.item.RangePlugins
import waterpower.util.isLava
import waterpower.util.isWater

@HasGui(guiClass = GuiWatermill::class, containerClass = ContainerWatermill::class)
open class TileEntityWatermill(val type: EnumWatermill) : TileEntityRotorGenerator(type.getOutput(), 2097152.0, type.getTier()) {
    val slotUpdater = InventorySlotRange(this)

    @Sync var waterBlocks: Int = 0
    @Sync var lavaBlocks: Int = 0
    @Sync var _range = 0

    override fun getName() = type.getLocalizedName()

    override fun getBlockState() = super.getBlockState().withProperty(WPBlocks.watermill.TYPES, type)!!

    private fun getWaterBlocks() {
        waterBlocks = 0
        lavaBlocks = waterBlocks

        var range = getRange()
        if (range * range * range > 729) {
            waterBlocks = -1
            lavaBlocks = waterBlocks
            return
        }
        range /= 2

        for (xTest in -range..range) {
            for (yTest in -range..range) {
                for (zTest in -range..range) {
                    val newPos = pos.add(xTest, yTest, zTest)
                    if (isWater(getWorld(), newPos)) {
                        waterBlocks++
                    } else if (isLava(getWorld(), newPos)) {
                        lavaBlocks++
                    }
                }
            }
        }

        if (type.ordinal < 2)
            lavaBlocks = 0
    }

    override fun computeOutput(world: World, pos: BlockPos): Double {
        if (waterBlocks == -1) {
            return 0.0
        }

        var vol = getRange()
        vol *= vol * vol
        val waterPercent = waterBlocks * 1.0 / (vol - 1)
        val lavaPercent = lavaBlocks * 1.0 / (vol - 1)
        val percent = waterPercent + lavaPercent * 4
        var energy = type.getOutput() * percent
        energy *= getEfficiency()
        if (energy > 0)
            damageRotor(1)
        return energy
    }

    override fun onUpdate() {
        super.onUpdate()

        getWaterBlocks()
    }

    fun isRangeSupported() = waterBlocks != -1 && lavaBlocks != -1

    fun hasRangeUpdater(): Boolean {
        if (!slotUpdater.isEmpty()) {
            for (i in 0..slotUpdater.size() - 1)
                if (!slotUpdater.get(i).isEmpty && slotUpdater.get(i).getItem() is ItemRange)
                    return true
        }
        return false
    }

    fun getRange(): Int {
        if (getWorld().isRemote && _range != 0)
            return _range
        _range = type.checkSize
        if (hasRangeUpdater()) {
            for (i in 0..slotUpdater.size() - 1) {
                val stack = slotUpdater.get(i)
                if (stack.isEmpty) continue
                if (slotUpdater.get(i).getItem() is ItemRange && stack.getItemDamage() >= RangePlugins.values().size)
                    return type.checkSize
                _range -= stack.count * RangePlugins.values()[stack.getItemDamage()].range
            }
        }
        if (_range < 3)
            _range = 3
        return _range
    }
}

@Register("waterpower.watermill.mk1")
class TileEntityWatermillMK1 : TileEntityWatermill(EnumWatermill.MK1)

@Register("waterpower.watermill.mk2")
class TileEntityWatermillMK2 : TileEntityWatermill(EnumWatermill.MK2)

@Register("waterpower.watermill.mk3")
class TileEntityWatermillMK3 : TileEntityWatermill(EnumWatermill.MK3)

@Register("waterpower.watermill.mk4")
class TileEntityWatermillMK4 : TileEntityWatermill(EnumWatermill.MK4)

@Register("waterpower.watermill.mk5")
class TileEntityWatermillMK5 : TileEntityWatermill(EnumWatermill.MK5)

@Register("waterpower.watermill.mk6")
class TileEntityWatermillMK6 : TileEntityWatermill(EnumWatermill.MK6)

@Register("waterpower.watermill.mk7")
class TileEntityWatermillMK7 : TileEntityWatermill(EnumWatermill.MK7)

@Register("waterpower.watermill.mk8")
class TileEntityWatermillMK8 : TileEntityWatermill(EnumWatermill.MK8)

@Register("waterpower.watermill.mk9")
class TileEntityWatermillMK9 : TileEntityWatermill(EnumWatermill.MK9)

@Register("waterpower.watermill.mk10")
class TileEntityWatermillMK10 : TileEntityWatermill(EnumWatermill.MK10)

@Register("waterpower.watermill.mk11")
class TileEntityWatermillMK11 : TileEntityWatermill(EnumWatermill.MK11)

@Register("waterpower.watermill.mk12")
class TileEntityWatermillMK12 : TileEntityWatermill(EnumWatermill.MK12)

@Register("waterpower.watermill.mk13")
class TileEntityWatermillMK13 : TileEntityWatermill(EnumWatermill.MK13)

@Register("waterpower.watermill.mk14")
class TileEntityWatermillMK14 : TileEntityWatermill(EnumWatermill.MK14)

@Register("waterpower.watermill.mk15")
class TileEntityWatermillMK15 : TileEntityWatermill(EnumWatermill.MK15)

@Register("waterpower.watermill.mk16")
class TileEntityWatermillMK16 : TileEntityWatermill(EnumWatermill.MK16)