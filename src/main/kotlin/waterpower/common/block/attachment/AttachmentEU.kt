/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.attachment

import ic2.api.energy.event.EnergyTileLoadEvent
import ic2.api.energy.event.EnergyTileUnloadEvent
import ic2.api.energy.tile.*
import ic2.api.info.ILocatable
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Optional
import waterpower.common.block.tile.TileEntityBase
import waterpower.integration.IDs
import waterpower.util.isServerSide

class AttachmentEU private constructor(te: TileEntityBase, val energyStorage: EnergyStorage)
    : TileEntityAttachment(te) {

    var sourceTier = 0
    var sinkTier = 0
    var sourceDirectionPredicate = DIRECTIONS_ALL
    var sinkDirectionPredicate = DIRECTIONS_ALL
    var addedToEnergyNet = false
    lateinit var delegate: EnergyTileDelegate

    override fun getName() = "ic2.energy_source"

    override fun onLoaded() {
        loadEnergyTile()
    }

    override fun onUnloaded() {
        unloadEnergyTile()
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    fun loadEnergyTile() {
        if (isServerSide() && !addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(EnergyTileLoadEvent(delegate))

            this.addedToEnergyNet = true
        }
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    fun unloadEnergyTile() {
        if (isServerSide() && addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(EnergyTileUnloadEvent(delegate))

            this.addedToEnergyNet = false
        }
    }

    private fun asEnergySource() {
        delegate = EnergySourceDelegate()
    }

    private fun asEnergySink() {
        delegate = EnergySinkDelegate()
    }

    private fun asEnergyDual() {
        delegate = EnergyDualDelegate()
    }

    open inner class EnergyTileDelegate : ILocatable, IEnergyTile {
        override fun getWorldObj(): World = master.world
        override fun getPosition(): BlockPos = master.pos
    }

    open inner class EnergySourceDelegate : EnergyTileDelegate(), IEnergySource {
        override fun getSourceTier() = this@AttachmentEU.sourceTier
        override fun emitsEnergyTo(receiver: IEnergyAcceptor?, side: EnumFacing) = this@AttachmentEU.sourceDirectionPredicate(side)
        override fun getOfferedEnergy() = energyStorage.extractEnergy(Double.MAX_VALUE, true)
        override fun drawEnergy(amount: Double) {
            energyStorage.extractEnergy(amount, false)
        }
    }

    open inner class EnergySinkDelegate : EnergyTileDelegate(), IEnergySink {
        override fun getSinkTier() = this@AttachmentEU.sinkTier
        override fun acceptsEnergyFrom(emitter: IEnergyEmitter?, side: EnumFacing) = this@AttachmentEU.sinkDirectionPredicate(side)
        override fun getDemandedEnergy() = energyStorage.receiveEnergy(Double.MAX_VALUE, true)
        override fun injectEnergy(directionFrom: EnumFacing?, amount: Double, voltage: Double): Double {
            energyStorage.receiveEnergy(amount, false)
            return 0.0
        }
    }

    open inner class EnergyDualDelegate : EnergyTileDelegate(), IEnergySource, IEnergySink {
        override fun getSourceTier() = this@AttachmentEU.sourceTier
        override fun emitsEnergyTo(receiver: IEnergyAcceptor?, side: EnumFacing) = this@AttachmentEU.sourceDirectionPredicate(side)
        override fun getOfferedEnergy() = energyStorage.extractEnergy(Double.MAX_VALUE, true)
        override fun drawEnergy(amount: Double) {
            energyStorage.extractEnergy(amount, false)
        }

        override fun getSinkTier() = this@AttachmentEU.sinkTier
        override fun acceptsEnergyFrom(emitter: IEnergyEmitter?, side: EnumFacing) = this@AttachmentEU.sinkDirectionPredicate(side)
        override fun getDemandedEnergy() = energyStorage.receiveEnergy(Double.MAX_VALUE, true)
        override fun injectEnergy(directionFrom: EnumFacing?, amount: Double, voltage: Double): Double {
            energyStorage.receiveEnergy(amount, false)
            return 0.0
        }
    }

    companion object {
        val DIRECTIONS_ALL = { x: EnumFacing -> x in EnumFacing.VALUES }
        val DIRECTIONS_HORIZONTAL = { x: EnumFacing -> x in EnumFacing.HORIZONTALS }

        fun createEnergySource(te: TileEntityBase, energyStorage: EnergyStorage, sourceTier: Int = 0, directions: (EnumFacing) -> Boolean = DIRECTIONS_ALL): AttachmentEU {
            val eu = AttachmentEU(te, energyStorage)
            eu.sourceTier = sourceTier
            eu.sourceDirectionPredicate = directions
            eu.asEnergySource()
            return eu
        }

        fun createEnergySink(te: TileEntityBase, energyStorage: EnergyStorage, sinkTier: Int = 0, directions: (EnumFacing) -> Boolean = DIRECTIONS_ALL): AttachmentEU {
            val eu = AttachmentEU(te, energyStorage)
            eu.sinkTier = sinkTier
            eu.sinkDirectionPredicate = directions
            eu.asEnergySink()
            return eu
        }

        fun createEnergyDual(te: TileEntityBase, energyStorage: EnergyStorage, sourceTier: Int = 0, sinkTier: Int = 0, sourceDirection: (EnumFacing) -> Boolean = DIRECTIONS_ALL, sinkDirection: (EnumFacing) -> Boolean = DIRECTIONS_ALL): AttachmentEU {
            val eu = AttachmentEU(te, energyStorage)
            eu.sourceTier = sourceTier
            eu.sourceDirectionPredicate = sourceDirection
            eu.sinkTier = sinkTier
            eu.sinkDirectionPredicate = sinkDirection
            eu.asEnergyDual()
            return eu
        }
    }

}