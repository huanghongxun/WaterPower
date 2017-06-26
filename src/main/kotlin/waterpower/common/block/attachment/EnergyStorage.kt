/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.attachment

import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.energy.IEnergyStorage
import waterpower.common.Energy
import waterpower.util.INBTSerializable

class EnergyStorage(var capacity: Double = 0.0, var maxReceive: Double = 0.0, var maxExtract: Double = maxReceive) : IEnergyStorage, INBTSerializable<EnergyStorage> {

    protected var energy: Double = 0.0
    var active = true

    fun receiveEnergy(maxReceive: Double, simulate: Boolean): Double {
        if (!canReceive())
            return 0.0

        val energyReceived = minOf(capacity - energy, minOf(this.maxReceive, maxReceive))
        if (!simulate)
            energy += energyReceived
        return energyReceived
    }

    override fun receiveEnergy(maxReceive: Int, simulate: Boolean) =
            receiveEnergy(maxReceive.toDouble(), simulate).toInt()

    fun extractEnergy(maxExtract: Double, simulate: Boolean): Double {
        if (!canExtract())
            return 0.0

        val energyExtracted = minOf(getMaxEnergyExtracted(), maxExtract)
        if (!simulate)
            energy -= energyExtracted
        return energyExtracted
    }

    override fun extractEnergy(maxExtract: Int, simulate: Boolean) =
            extractEnergy(maxExtract.toDouble(), simulate).toInt()

    fun getMaxEnergyExtracted(): Double
            = minOf(energy, maxExtract)

    override fun getEnergyStored(): Int {
        return energy.toInt()
    }

    override fun getMaxEnergyStored(): Int {
        return capacity.toInt()
    }

    override fun canExtract(): Boolean {
        return active && this.maxExtract > 0
    }

    override fun canReceive(): Boolean {
        return active && this.maxReceive > 0
    }

    fun getEnergyStoredEU(): Double = energy
    fun getEnergyStoredMJ(): Int = Energy.EU2MJ(energy).toInt()
    fun getEnergyStoredRF(): Int = Energy.EU2RF(energy).toInt()
    fun getEnergyStoredWater(): Int = Energy.EU2Water(energy).toInt()
    fun getEnergyStoredSteam(): Int = Energy.EU2Steam(energy).toInt()

    override fun serializeNBT(): NBTTagCompound {
        val tag = NBTTagCompound()
        tag.setDouble("energy", energy)
        tag.setDouble("capacity", capacity)
        return tag
    }

    override fun deserializeNBT(tag: NBTTagCompound) {
        if (tag.hasKey("energy")) energy = tag.getDouble("energy")
        if (tag.hasKey("capacity")) capacity = tag.getDouble("capacity")
    }

    override fun clone(): Any {
        val v = EnergyStorage()
        v.deserializeNBT(serializeNBT())
        return v
    }
}