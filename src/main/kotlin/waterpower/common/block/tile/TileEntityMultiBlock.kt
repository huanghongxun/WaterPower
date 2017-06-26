/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.tile

import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import waterpower.annotations.SaveNBT
import waterpower.annotations.Sync


abstract class TileEntityMultiBlock<T : TileEntityMultiBlock<T>> : TileEntityInventory() {

    protected var tested: Boolean = false

    @Sync @SaveNBT private var masterPos: BlockPos = BlockPos(0, 0, 0)
    @Sync @SaveNBT private var hasMaster: Boolean = false
    protected var blockList: List<T> = emptyList()

    protected fun setMaster(pos: BlockPos?) {
        if (pos == null && hasMaster) {
            hasMaster = false
        } else if (pos != null && !hasMaster) {
            hasMaster = true
            masterPos = pos
        } else if (pos != null && hasMaster && masterPos != pos) {
            masterPos = pos
        }
    }

    /**
     * The process:
     * 1 -> the master block construct the structure
     * 2 -> set the master block of other blocks in the structure to this
     * 3 -> sync masterPos to client
     * 4 -> the functionality of other blocks are redirected to the master block,
     *      which means something like invslot and fluidtanks and other capabilities are redirected to the master block's.
     *
     * problem:
     * as we know, minecraft will process chunks in more than 2 chunks at the same time, so we have thread syncing problem.
     * when a block broken.
     */
    fun getMasterBlock(): T? {
        if (!hasMaster || getWorld() == null) return null
        val te = getWorld().getTileEntity(masterPos)
        if (te == null || te !is TileEntityMultiBlock<*>) {
            setMaster(null)
            return null
        } else
            return te as T?
    }

    override fun onSyncDataChanged(name: String) {
        super.onSyncDataChanged(name)

        if ("masterPos" == name || "hasMaster" == name)
            onMasterChanged()
    }

    open fun onMasterChanged() {}

    protected abstract fun test(): List<T>

    protected abstract fun canBeMaster(): Boolean

    @Synchronized
    open fun checkStructure() {
        if (canBeMaster()) {

            tested = true
            val newBlockList = test()
            val oldBlockList = blockList
            val changed = oldBlockList.isEmpty() || newBlockList.isEmpty() || newBlockList != oldBlockList
            if (changed && !oldBlockList.isEmpty()) {
                for (block in oldBlockList) {
                    block.tested = false
                    block.setMaster(null)
                }
            }
            blockList = newBlockList
            if (newBlockList.isEmpty()) {
                tested = false
                onTestFailed()
            } else if (changed) {
                for (block in newBlockList) {
                    block.tested = true
                    block.setMaster(this.pos)
                }
            }
        }
    }

    override fun onFirstTick() {
        super.onFirstTick()

        onMasterChanged()
    }

    override fun onUpdate() {
        super.onUpdate()

        checkStructure()
    }

    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (!isIncompleteStructure() && !isMaster())
            return getMasterBlock()!!.getCapability(capability, facing)
        else
            return super.getCapability(capability, facing)
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        if (!isIncompleteStructure() && !isMaster())
            return getMasterBlock()!!.hasCapability(capability, facing)
        else
            return super.hasCapability(capability, facing)
    }

    fun isMaster(): Boolean {
        return getMasterBlock() == this
    }

    fun isIncompleteStructure(): Boolean {
        return getMasterBlock() == null
    }

    fun isNormalBlock(): Boolean {
        return !isMaster() && !isIncompleteStructure()
    }

    protected abstract fun onTestFailed()
}