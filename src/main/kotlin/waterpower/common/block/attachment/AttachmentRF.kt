/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.attachment

import cofh.api.energy.IEnergyReceiver
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.Optional
import waterpower.common.Energy
import waterpower.common.block.tile.TileEntityBase
import waterpower.integration.IDs

class AttachmentRF(te: TileEntityBase, val energyStroage: EnergyStorage) : TileEntityAttachment(te) {

    override fun getName() = "rf"

    override fun onTick() {
        reCache()
        val j = energyStroage.extractEnergy(Double.MAX_VALUE, false)
        energyStroage.receiveEnergy(Energy.RF2EU(transmitEnergy(Energy.EU2RF(j).toInt()).toDouble()), false)
    }

    private var handlerCache: Array<Any?>? = null
    private var deadCache = true

    override fun onLoaded() {
        super.onLoaded()

        // RF INTEGRATION
        deadCache = true
        handlerCache = null
    }

    /**
     * @param maxExtract how much RF do we have?
     * @return how much RF remained?
     */
    @Optional.Method(modid = IDs.CoFHAPIEnergy)
    private fun transmitEnergy(maxExtract: Int): Int {
        try {
            var e = maxExtract
            if (this.handlerCache != null) {
                var i = this.handlerCache!!.size
                while (i-- > 0) {
                    val h = this.handlerCache!![i] as IEnergyReceiver? ?: continue
                    val d = EnumFacing.VALUES[i]
                    if (h.receiveEnergy(d, e, true) > 0)
                        e -= h.receiveEnergy(d, e, false)
                    if (e <= 0)
                        return 0
                }
            }
            return e
        } catch(e: Throwable) {
            e.printStackTrace()
            return maxExtract
        }
    }

    @Optional.Method(modid = IDs.CoFHAPIEnergy)
    private fun reCache() {
        if (this.deadCache) {
            for (d in EnumFacing.VALUES)
                onNeighborTileChange(master.pos.add(d.directionVec))
            this.deadCache = false
        }
    }

    @Optional.Method(modid = IDs.CoFHAPIEnergy)
    fun onNeighborTileChange(neighbor: BlockPos) {
        val t = master.world.getTileEntity(neighbor) ?: return
        val pos = master.pos

        if (neighbor.x < pos.x)
            addCache(t, EnumFacing.EAST.ordinal)
        else if (neighbor.x > pos.x)
            addCache(t, EnumFacing.WEST.ordinal)
        else if (neighbor.y < pos.y)
            addCache(t, EnumFacing.UP.ordinal)
        else if (neighbor.y > pos.y)
            addCache(t, EnumFacing.DOWN.ordinal)
        else if (neighbor.z < pos.z)
            addCache(t, EnumFacing.NORTH.ordinal)
        else if (neighbor.z > pos.z)
            addCache(t, EnumFacing.SOUTH.ordinal)
    }

    @Optional.Method(modid = IDs.CoFHAPIEnergy)
    private fun addCache(t: TileEntity, side: Int) {
        if (handlerCache != null) {
            handlerCache!![side] = null
        }
        try {
            if (t is IEnergyReceiver && t.canConnectEnergy(EnumFacing.VALUES[side])) {
                if (handlerCache == null)
                    handlerCache = arrayOfNulls<Any?>(6)
                handlerCache!![side] = t
            }
        } catch(e: Throwable) {
            e.printStackTrace()
        }
    }

}