/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.machine

import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.energy.IEnergyStorage
import waterpower.annotations.SaveNBT
import waterpower.annotations.Sync
import waterpower.api.IUpgrade
import waterpower.api.IWaterReceiver
import waterpower.api.WaterAPI
import waterpower.common.block.attachment.EnergyStorage
import waterpower.common.block.inventory.InventorySlotOutput
import waterpower.common.block.inventory.InventorySlotProcessable
import waterpower.common.block.inventory.InventorySlotUpgrade
import waterpower.common.block.tile.TileEntityInventory
import waterpower.common.recipe.RecipeOutput


abstract class TileEntityBaseMachine(val defaultEnergyConsume: Int, val defaultOperationLength: Int, val outputSlotCount: Int = 2)
    : TileEntityInventory() {

    @Sync var guiProgress: Float = 0f
    @SaveNBT @Sync var progress: Short = 0
    @Sync var energyConsume: Int = defaultEnergyConsume

    var defaultEnergyStorage = defaultEnergyConsume * defaultOperationLength
    var operationLength = defaultOperationLength
    var operationsPerTick = 1

    @Sync @SaveNBT val energy = EnergyStorage(defaultEnergyStorage.toDouble(), Double.MAX_VALUE)

    var waterReceiver = object : IWaterReceiver {
        override fun receiveWater(limit: Int, side: EnumFacing, simulate: Boolean)
                = energy.receiveEnergy(limit, simulate)
    }

    lateinit var inputSlot: InventorySlotProcessable
    val outputSlot = InventorySlotOutput(this, "output", outputSlotCount)
    val upgradeSlot = InventorySlotUpgrade(this, "upgrade", 4)

    protected var isLastFailed = false
    protected var lastFailedContents: Array<ItemStack>? = null

    override fun onLoaded() {
        if (isServerSide())
            setOverclockRates();

        super.onLoaded()
    }


    fun setOverclockRates() {
        val extraProcessTime = 0
        var processTimeMultiplier = 1.0
        val extraEnergyDemand = 0
        var energyDemandMultiplier = 1.0
        var extraEnergyStorage = 0
        var energyStorageMultiplier = 1.0

        for (i in 0..this.upgradeSlot.size() - 1) {
            val stack = this.upgradeSlot.get(i)

            if (stack.isEmpty || stack.item !is IUpgrade)
                continue
            val upgrade = stack.item as IUpgrade
            val count = stack.count.toDouble()

            processTimeMultiplier *= Math.pow(upgrade.getSpeedAdditionalValue(stack), count)
            energyDemandMultiplier *= Math.pow(upgrade.getEnergyDemandMultiplier(stack), count)
            extraEnergyStorage += upgrade.getStorageAdditionalValue(stack) * stack.count
            energyStorageMultiplier *= Math.pow(1.0, count)
        }

        val previousProgress = this.progress / this.operationLength

        val stackOpLen = (this.defaultOperationLength + extraProcessTime) * 64.0 * processTimeMultiplier
        this.operationsPerTick = Math.min(Math.ceil(64.0 / stackOpLen), 2147483647.0).toInt()
        this.operationLength = Math.round(stackOpLen * this.operationsPerTick / 64.0).toInt()

        this.energyConsume = applyModifier(this.defaultEnergyConsume, extraEnergyDemand, energyDemandMultiplier).toInt()
        this.energy.capacity = applyModifier(this.defaultEnergyStorage, extraEnergyStorage + this.operationLength * this.energyConsume,
                energyStorageMultiplier)

        if (this.operationLength < 1)
            this.operationLength = 1

        this.progress = Math.floor(previousProgress * this.operationLength + 0.1).toInt().toShort()
    }

    override fun markDirty() {
        super.markDirty()

        if (isServerSide())
            setOverclockRates()
    }

    fun getProgress(): Float {
        return this.guiProgress
    }

    override fun update() {
        super.update()

        if (!isServerSide())
            return

        var needsInvUpdate = false

        if (isLastFailed && inputSlot.isEquals(lastFailedContents!!))
        else {
            val output = getOutput()

            if (output != null && energy.energyStored >= this.energyConsume) {

                if (isLastFailed) {
                    beginProcess(output)
                    isLastFailed = false
                }

                this.progress = (this.progress + 1).toShort()
                energy.extractEnergy(energyConsume, false)

                if (this.progress >= this.operationLength) {
                    operate(output)
                    needsInvUpdate = true
                    this.progress = 0
                }
            } else {
                if (output == null) {
                    this.progress = 0
                    isLastFailed = true
                    lastFailedContents = inputSlot.copiedContent
                }

                // setActive(false);
            }

            val tmp = guiProgress
            if (isServerSide())
                this.guiProgress = this.progress.toFloat() / this.operationLength
            if (Math.abs(tmp - guiProgress) > 0.001 && isServerSide())
                sendUpdateToClient()
        }

        if (needsInvUpdate)
            super.markDirty()
    }

    protected open fun beginProcess(output: RecipeOutput) {}

    protected open fun failedProcess() {}

    fun operate(output: RecipeOutput) {
        var o = output
        for (i in 0..this.operationsPerTick - 1) {
            val processResult = o.items

            operateOnce(o, processResult)

            val newOutput = getOutput() ?: break
            o = newOutput
        }
    }

    fun operateOnce(output: RecipeOutput, processResult: Collection<ItemStack>) {
        this.inputSlot.consume()
        this.outputSlot.add(processResult)
    }

    private fun applyModifier(base: Int, extra: Int, multiplier: Double): Double {
        val ret = Math.round((base + extra) * multiplier).toDouble()

        return if (ret > 2147483647.0) 2147483647.0 else ret
    }

    fun getOutput(): RecipeOutput? {
        if (this.inputSlot.isEmpty())
            return null

        val output = this.inputSlot.process() ?: return null

        if (this.outputSlot.canAdd(output.items)) {
            return output
        }
        return null
    }

    fun getEnergy(): IEnergyStorage = energy

    fun getOutputSize(): Int {
        return this.outputSlot.size()
    }

    fun getOutput(index: Int): ItemStack {
        return this.outputSlot[index]
    }

    fun setOutput(index: Int, stack: ItemStack) {
        this.outputSlot.put(index, stack)
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?) =
            capability == WaterAPI.CAPABILITY_WATER_RECEIVER || super.hasCapability(capability, facing)

    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (capability == WaterAPI.CAPABILITY_WATER_RECEIVER)
            return waterReceiver as T
        else
            return super.getCapability(capability, facing)
    }
}