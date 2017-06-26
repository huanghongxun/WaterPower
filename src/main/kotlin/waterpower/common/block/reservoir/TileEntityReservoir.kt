/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.reservoir

import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.IFluidHandler
import waterpower.Preference
import waterpower.annotations.HasGui
import waterpower.annotations.Register
import waterpower.annotations.SaveNBT
import waterpower.annotations.Sync
import waterpower.api.IUpgrade
import waterpower.api.IWaterReceiver
import waterpower.api.WaterAPI
import waterpower.common.block.attachment.AttachmentFluid
import waterpower.common.block.inventory.InventorySlotConsumableLiquid
import waterpower.common.block.inventory.InventorySlotOutput
import waterpower.common.block.inventory.InventorySlotUpgrade
import waterpower.common.block.tile.TileEntityMultiBlock
import waterpower.common.init.WPBlocks
import waterpower.util.Vec2i
import waterpower.util.getWaterIncomeAndExpenseByBiome

@HasGui(guiClass = GuiReservoir::class, containerClass = ContainerReservoir::class)
open class TileEntityReservoir(val type: Reservoirs) : TileEntityMultiBlock<TileEntityReservoir>(), IFluidHandler {

    protected val fluid = addAttachment(AttachmentFluid(this))
    @SaveNBT @Sync
    protected val fluidTank = fluid.addTank("fluid", 0)

    @Sync val reservoir = Reservoir()

    var defaultStorage = 0
    var extraStorage = 0

    var rainLevel = 0.0
    var underLevel = 0.0
    var overLevel = 0.0

    @Sync private var lastAddedWater = 0

    private val ocean = Vec2i()
    private val fluidSlot = InventorySlotConsumableLiquid(this, "fluidSlot", 1)
    private val outputSlot = InventorySlotOutput(this, "output", 1)
    private val upgradeSlot = InventorySlotUpgrade(this, "upgrade", 4)

    private lateinit var helper: MultiBlockHelper

    fun getFluidTank(): FluidTank {
        if (!isNormalBlock())
            return fluidTank
        else
            return getMasterBlock()!!.fluidTank
    }

    fun getFluidSlot(): InventorySlotConsumableLiquid {
        if (!isNormalBlock())
            return fluidSlot
        else
            return getMasterBlock()!!.fluidSlot
    }

    fun getOutputSlot(): InventorySlotOutput {
        if (!isNormalBlock())
            return outputSlot
        else
            return getMasterBlock()!!.outputSlot
    }

    fun getUpgradeSlot(): InventorySlotUpgrade {
        if (!isNormalBlock())
            return upgradeSlot
        else
            return getMasterBlock()!!.upgradeSlot
    }

    override fun getName() = type.getLocalizedName()

    override fun getBlockState() = super.getBlockState().withProperty(WPBlocks.reservoir.TYPES, type)!!

    fun useLiquid(use: Int) {
        if (getWorld().isRemote || isMaster())
            fluidTank.drain(use, true)
        else if (!isIncompleteStructure())
            (getMasterBlock() as TileEntityReservoir).fluidTank.drain(use, true)
    }

    override fun canBeMaster() = helper.canBeMaster()

    override fun onMasterChanged() {
        super.onMasterChanged()

        if (getMasterBlock() != this) {
            fluidTank.capacity = 0
            fluidTank.fluid = null
        }
    }

    override fun test(): List<TileEntityReservoir> {
        helper.reservoir(reservoir)

        if (reservoir.uuid != null) {
            defaultStorage = reservoir.capacity * type.capacity
            fluidTank.capacity = defaultStorage + extraStorage
        }

        return helper.list
    }

    override fun onUpdate() {
        super.onUpdate()

        if (isMaster() && !getWorld().isRemote && reservoir.uuid != null) {

            val (weather, biomeGet) = getWaterIncomeAndExpenseByBiome(getWorld(), pos)

            val length = reservoir.length - 2
            val width = reservoir.width - 2
            val area = length * width
            val cover = helper.coveredBlocks(reservoir)

            var addWater = 0.0
            val add = type.capacity / 10000.0
            // Rain Receiving
            addWater += rainLevel * weather * add * (area - cover) * biomeGet
            // Tide Receiving
            addWater += Math.min(this.ocean.x, this.ocean.y) * type.capacity / 100
            // Underground water receiving
            addWater += underLevel * (1.0 - this.pos.y / 256) * add * (area - cover) * biomeGet
            // Surface water receiving
            addWater += overLevel * (1.0 - Math.abs(64 - this.pos.y) / 64) * (area - cover) * biomeGet * add

            val biomeID = getWorld().getBiomeForCoordsBody(pos).biomeName
            if (("ocean" == biomeID || "river" == biomeID) && pos.y < 64) {
                addWater += length.toDouble() * width.toDouble() * 0.5
            }

            lastAddedWater = (addWater * Preference.General.updateTicks).toInt()

            if (addWater.toInt() * Preference.General.updateTicks >= 1)
                fluidTank.fill(FluidStack(FluidRegistry.WATER, addWater.toInt() * Preference.General.updateTicks), true)
        }
    }

    override fun update() {
        super.update()

        if (!getWorld().isRemote && !isIncompleteStructure()) {

            if (isMaster()) {
                if (fluidSlot.processIntoTank(fluidTank, outputSlot))
                    markDirty()
            }

            for (f in EnumFacing.values()) {
                val te = getWorld().getTileEntity(pos.offset(f))
                if (te != null && te.hasCapability(WaterAPI.CAPABILITY_WATER_RECEIVER!!, f.opposite)) {
                    val waterReceiver = te.getCapability(WaterAPI.CAPABILITY_WATER_RECEIVER!!, f.opposite) as IWaterReceiver
                    val delta = waterReceiver.receiveWater(getFluidTank().fluidAmount, f.opposite, false)
                    useLiquid(delta)
                }
            }
        }
    }

    override fun onTestFailed() {
        fluidTank.capacity = 0
    }

    fun getLastAddedWater(): Int {
        if (!isNormalBlock())
            return lastAddedWater
        else
            return (getMasterBlock() as TileEntityReservoir).getLastAddedWater()
    }

    override fun markDirty() {
        super.markDirty()

        if (isServerSide())
            refreshPlugins()
    }

    override fun onFirstTick() {
        super.onFirstTick()

        if (isServerSide()) {
            helper = MultiBlockHelper(this)
            refreshPlugins()
        }
    }

    fun refreshPlugins() {
        if (isMaster()) {
            extraStorage = 0
            underLevel = 0.0
            overLevel = 0.0
            rainLevel = 0.0

            for (i in 0..this.upgradeSlot.size() - 1) {
                val stack = this.upgradeSlot[i]

                if (stack.isEmpty || stack.item !is IUpgrade)
                    continue
                val upgrade = stack.item as IUpgrade
                rainLevel += upgrade.getRainAdditionalValue(stack)
                overLevel += upgrade.getOverworldAdditionalValue(stack)
                underLevel += upgrade.getUnderworldAdditionalValue(stack)
                extraStorage += upgrade.getStorageAdditionalValue(stack)
            }

            fluidTank.capacity = defaultStorage + extraStorage
        }
    }

    override fun drain(resource: FluidStack?, doDrain: Boolean) = getFluidTank().drain(resource, doDrain)

    override fun drain(maxDrain: Int, doDrain: Boolean) = getFluidTank().drain(maxDrain, doDrain)

    override fun fill(resource: FluidStack?, doFill: Boolean) = getFluidTank().fill(resource, doFill)

    override fun getTankProperties() = getFluidTank().tankProperties!!
}

@Register("waterpower.reservoir.wood")
class TileEntityReservoirWood : TileEntityReservoir(Reservoirs.wood)

@Register("waterpower.reservoir.stone")
class TileEntityReservoirStone : TileEntityReservoir(Reservoirs.stone)

@Register("waterpower.reservoir.lapis")
class TileEntityReservoirLapis : TileEntityReservoir(Reservoirs.lapis)

@Register("waterpower.reservoir.tin")
class TileEntityReservoirTin : TileEntityReservoir(Reservoirs.tin)

@Register("waterpower.reservoir.copper")
class TileEntityReservoirCopper : TileEntityReservoir(Reservoirs.copper)

@Register("waterpower.reservoir.quartz")
class TileEntityReservoirQuartz : TileEntityReservoir(Reservoirs.quartz)

@Register("waterpower.reservoir.zinc_alloy")
class TileEntityReservoirZincAlloy : TileEntityReservoir(Reservoirs.zinc_alloy)

@Register("waterpower.reservoir.bronze")
class TileEntityReservoirBronze : TileEntityReservoir(Reservoirs.bronze)

@Register("waterpower.reservoir.iron")
class TileEntityReservoirIron : TileEntityReservoir(Reservoirs.iron)

@Register("waterpower.reservoir.obsidian")
class TileEntityReservoirObsidian : TileEntityReservoir(Reservoirs.obsidian)

@Register("waterpower.reservoir.steel")
class TileEntityReservoirSteel : TileEntityReservoir(Reservoirs.steel)

@Register("waterpower.reservoir.gold")
class TileEntityReservoirGold : TileEntityReservoir(Reservoirs.gold)

@Register("waterpower.reservoir.manganese_steel")
class TileEntityReservoirManganeseSteel : TileEntityReservoir(Reservoirs.manganese_steel)

@Register("waterpower.reservoir.diamond")
class TileEntityReservoirDiamond : TileEntityReservoir(Reservoirs.diamond)

@Register("waterpower.reservoir.vanadium_steel")
class TileEntityReservoirVanadiumSteel : TileEntityReservoir(Reservoirs.vanadium_steel)
