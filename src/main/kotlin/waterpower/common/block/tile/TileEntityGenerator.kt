/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.tile

import ic2.api.energy.tile.IHeatSource
import ic2.api.energy.tile.IKineticSource
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.common.Optional.Interface
import net.minecraftforge.fml.common.Optional.InterfaceList
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.annotations.SaveNBT
import waterpower.annotations.Sync
import waterpower.api.IUnitChangeable
import waterpower.common.Energy
import waterpower.common.block.attachment.*
import waterpower.common.network.NetworkHandler
import waterpower.common.network.PacketUnitChanged
import waterpower.integration.IDs
import waterpower.integration.Mod
import waterpower.util.pushFluidAround

@InterfaceList(
        Interface(iface = "ic2.api.energy.tile.IKineticSource", modid = IDs.IndustrialCraft2, striprefs = true),
        Interface(iface = "ic2.api.energy.tile.IHeatSource", modid = IDs.IndustrialCraft2, striprefs = true))
abstract class TileEntityGenerator(production: Int, maxStorage: Double, tier: Int)
    : TileEntityInventory(), IKineticSource, IUnitChangeable, IHeatSource {

    protected val fluid = addAttachment(AttachmentFluid(this))

    @SaveNBT
    protected val fluidTank = fluid.addTankInsert("fluid", maxStorage.toInt())

    protected val energyAttachments = mutableMapOf<Energy, TileEntityAttachment>()

    @SaveNBT @Sync val energyStorage = EnergyStorage(maxStorage, Double.MAX_VALUE, production.toDouble())
    @SaveNBT @Sync var energyType = Energy.EU.ordinal
    @Sync var latestOutput = 0.0

    init {
        if (Mod.IndustrialCraft2.isAvailable) {
            val eu = AttachmentEU.createEnergySource(this, energyStorage)
            eu.sourceTier = tier
            energyAttachments[Energy.EU] = eu
        }
        if (Mod.RedstoneFlux.isAvailable)
            energyAttachments[Energy.RF] = AttachmentRF(this, energyStorage)
        if (Mod.BuildCraftPower.isAvailable)
            energyAttachments[Energy.MJ] = AttachmentMJ(this, energyStorage)
        energyAttachments[Energy.STEAM] = AttachmentSteam(this, energyStorage, fluidTank)
        energyAttachments[Energy.WATER] = AttachmentWater(this, energyStorage, fluidTank)
    }

    override fun onLoad() {
        super.onLoad()

        val energyUnit = getEnergyUnit()
        if (energyAttachments.containsKey(energyUnit))
            energyAttachments[energyUnit]!!.onLoaded()
    }

    override fun onUnloaded() {
        super.onUnloaded()

        val energyUnit = getEnergyUnit()
        if (energyAttachments.containsKey(energyUnit))
            energyAttachments[energyUnit]!!.onUnloaded()
    }

    fun enableUpdateEntity(): Boolean {
        return isServerSide()
    }

    override fun onNeighborTileChanged(pos: BlockPos) {
        super.onNeighborTileChanged(pos)

        if (energyAttachments.containsKey(getEnergyUnit()))
            energyAttachments[getEnergyUnit()]!!.onNeighborTileChange(pos)
    }

    protected abstract fun computeOutput(world: World, pos: BlockPos): Double

    override fun update() {
        super.update()

        if (!isServerSide())
            return

        pushFluidAround(getWorld(), getPos(), fluidTank)

        if (!isRedstonePowered()) {
            latestOutput = computeOutput(world, pos)
            energyStorage.receiveEnergy(latestOutput, false)

            if (energyAttachments.containsKey(getEnergyUnit()))
                energyAttachments[getEnergyUnit()]!!.onTick()
        }
    }

    override fun onUpdate() {
        super.onUpdate()

        if (!isRedstonePowered())
            if (energyAttachments.containsKey(getEnergyUnit()))
                energyAttachments[getEnergyUnit()]!!.onUpdate()
    }

    /**
     * ------------------------------------------------------

     * HU, KU(INDUSTRIAL CRAFT 2 EXPERIMENTAL) INTEGRATION BEGINS

     * ------------------------------------------------------
     */

    @Optional.Method(modid = IDs.IndustrialCraft2)
    override fun maxrequestHeatTick(directionFrom: EnumFacing): Int {
        if (getEnergyUnit() == Energy.HU)
            return Energy.EU2HU(energyStorage.getMaxEnergyExtracted()).toInt()
        return 0
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    override fun requestHeat(directionFrom: EnumFacing, requestheat: Int): Int {
        if (getEnergyUnit() == Energy.HU)
            return Energy.EU2HU(energyStorage.extractEnergy(Energy.HU2EU(requestheat.toDouble()), true)).toInt()
        return 0
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    override fun maxrequestkineticenergyTick(directionFrom: EnumFacing): Int {
        if (getEnergyUnit() == Energy.KU)
            return Energy.EU2KU(energyStorage.getMaxEnergyExtracted()).toInt()
        return 0
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    override fun requestkineticenergy(directionFrom: EnumFacing, requestkineticenergy: Int): Int {
        if (getEnergyUnit() == Energy.KU)
            return Energy.EU2KU(energyStorage.extractEnergy(Energy.KU2EU(requestkineticenergy.toDouble()), true)).toInt()
        return 0
    }

    /**
     * ------------------------------------------------------

     * HU, KU(INDUSTRIAL CRAFT 2 EXPERIMENTAL) INTEGRATION ENDS

     * ------------------------------------------------------
     */

    override fun setUnit(energyUnit: Energy) {
        val oldEnergy = getEnergyUnit()
        if (oldEnergy != energyUnit) {
            if (energyAttachments.containsKey(oldEnergy))
                energyAttachments[oldEnergy]!!.onUnloaded()
            if (energyAttachments.containsKey(energyUnit))
                energyAttachments[energyUnit]!!.onLoaded()
        }
        energyType = energyUnit.ordinal
    }

    fun getFromEU(eu: Double): Double {
        return getEnergyUnit().getFromEU(eu)
    }

    fun getEnergyUnit(): Energy {
        if (energyType < 0 || energyType >= Energy.values().size)
            energyType = 0
        return Energy.values()[energyType]
    }

    @SideOnly(Side.CLIENT)
    fun onUnitChanged(t: Energy) {
        energyType = t.ordinal
        NetworkHandler.instance.sendToServer(PacketUnitChanged(Minecraft.getMinecraft().player.world.provider.getDimension(), pos, getEnergyUnit()))
    }

    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        var cap: T? = null
        if (energyAttachments.containsKey(getEnergyUnit()))
            cap = energyAttachments[getEnergyUnit()]!!.getCapability(capability, facing)
        if (cap == null)
            cap = super.getCapability(capability, facing)
        return cap
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        if (super.hasCapability(capability, facing)) return true
        if (energyAttachments.containsKey(getEnergyUnit()))
            return energyAttachments[getEnergyUnit()]!!.getCapabilities(facing).contains(capability)
        else
            return false
    }
}