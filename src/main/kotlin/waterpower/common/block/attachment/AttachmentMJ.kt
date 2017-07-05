/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.attachment

import buildcraft.api.mj.IMjConnector
import buildcraft.api.mj.IMjReceiver
import buildcraft.api.mj.MjAPI
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import waterpower.common.Energy
import waterpower.common.block.tile.TileEntityBase

class AttachmentMJ(master: TileEntityBase, val energyStorage: EnergyStorage) : TileEntityAttachment(master) {
    override fun getName() = "mj"

    lateinit var mjConnector: Any

    override fun onLoaded() {
        super.onLoaded()

        runIgnoringThrowables {
            mjConnector = IMjConnector { it is IMjReceiver && it.canReceive() }
        }
    }

    override fun onTick() {
        super.onTick()

        runIgnoringThrowables {
            for (facing in EnumFacing.VALUES)
                sendPower(facing)
        }
    }

    fun getReceiverToPower(tile: TileEntity?, side: EnumFacing): Any? {
        if (tile == null) {
            return null
        } else {
            val rec = tile.getCapability(MjAPI.CAP_RECEIVER, side.opposite)
            return if (rec != null && rec.canConnect(this.mjConnector as IMjConnector)) rec else null
        }
    }

    private fun getTileBuffer(facing: EnumFacing)
            = master.world.getTileEntity(master.pos.offset(facing))

    private fun getPowerToExtract(facing: EnumFacing, doExtract: Boolean): Long {
        val tile = this.getTileBuffer(facing)
        if (tile == null) {
            return 0L
        } else {
            val receiver = this.getReceiverToPower(tile, facing)
            return if (receiver == null) 0L
            else energyStorage.extractEnergy(Energy.MJ2EU((receiver as IMjReceiver).powerRequested.toDouble()), doExtract).toLong()
        }
    }

    private fun sendPower(facing: EnumFacing) {
        val tile = this.getTileBuffer(facing)
        if (tile != null) {
            val receiver = this.getReceiverToPower(tile, facing)
            if (receiver != null) {
                val extracted = this.getPowerToExtract(facing, true)
                if (extracted > 0L && receiver is IMjReceiver) {
                    val excess = receiver.receivePower(extracted, false)
                    energyStorage.extractEnergy(Energy.MJ2EU((extracted - excess).toDouble()), true)
                }
            }
        }
    }

    fun runIgnoringThrowables(x: () -> Unit) {
        try {
            x()
        } catch(ignore: Throwable) {
        }
    }
}