/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.attachment

import cofh.redstoneflux.api.IEnergyReceiver
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.fml.common.Optional
import waterpower.common.Energy
import waterpower.common.block.tile.TileEntityBase
import waterpower.integration.IDs
import waterpower.integration.Mod

class AttachmentRF(te: TileEntityBase, val energyStroage: EnergyStorage) : TileEntityAttachment(te),  IEnergyStorage {

    override fun getName() = "rf"

    override fun onTick() {
        super.onTick()

        for (facing in EnumFacing.VALUES) {
            if (energyStored == 0) break
            val opposite = facing.opposite
            val pos = master.pos.offset(facing)
            val neighbor = master.world.getTileEntity(pos)
            if (neighbor != null) {
                var received = 0
                var flag = false
                if (Mod.RedstoneFlux.isAvailable) {
                    try {
                        if (neighbor is IEnergyReceiver) {
                            received += neighbor.receiveEnergy(opposite, energyStored, false)
                            flag = true
                        }
                    } catch(t: Throwable) {
                        // may not happen when cofhapi loaded, but if api changed, we should ignore to keep mod working and update then.
                    }
                }
                if (!flag && neighbor.hasCapability(CapabilityEnergy.ENERGY, opposite)) {
                    received = 0
                    val cap = neighbor.getCapability(CapabilityEnergy.ENERGY, opposite) as IEnergyStorage
                    if (cap.canReceive())
                        received += cap.receiveEnergy(energyStored, false)
                    extractEnergy(received, false)
                }
            }
        }
    }

    @Optional.Method(modid = IDs.RedstoneFlux)
    fun processTileEntity(te: TileEntity, facing: EnumFacing): Int {
        if (te is IEnergyReceiver)
            return te.receiveEnergy(facing, energyStored, false)
        return -1
    }

    override fun getCapabilities(side: EnumFacing?) = setOf(CapabilityEnergy.ENERGY)
    override fun <T> getCapability(capability: Capability<T>, side: EnumFacing?): T? {
        if (capability == CapabilityEnergy.ENERGY)
            return this as T
        else
            return super.getCapability(capability, side)
    }

    override fun canExtract() = energyStroage.canExtract()
    override fun canReceive() = false
    override fun extractEnergy(maxExtract: Int, simulate: Boolean) = Energy.EU2RF(energyStroage.extractEnergy(Energy.RF2EU(maxExtract.toDouble()), simulate)).toInt()
    override fun receiveEnergy(maxReceive: Int, simulate: Boolean) = 0
    override fun getEnergyStored() = energyStroage.getEnergyStoredRF()
    override fun getMaxEnergyStored() = Energy.EU2RF(energyStroage.maxEnergyStored.toDouble()).toInt()
}