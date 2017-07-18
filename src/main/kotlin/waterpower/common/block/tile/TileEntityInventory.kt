/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.tile

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.InvWrapper
import net.minecraftforge.items.wrapper.SidedInvWrapper
import waterpower.common.block.inventory.InventorySlot
import waterpower.util.*
import java.util.*

abstract class TileEntityInventory : TileEntityBase(), ISidedInventory {
    private val itemHandler = arrayOfNulls<IItemHandler>(EnumFacing.VALUES.size + 1)
    val invSlots = ArrayList<InventorySlot>()

    override fun readFromNBT(tag: NBTTagCompound) {
        super.readFromNBT(tag)

        val inventory = tag.getCompoundTag("inventory")

        for (invSlot in this.invSlots)
            invSlot.readFromNBT(inventory.getCompoundTag(invSlot.name))
    }

    override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(tag)

        val invSlotsTag = NBTTagCompound()
        for (invSlot in invSlots) {
            val invSlotTag = NBTTagCompound()
            invSlot.writeToNBT(invSlotTag)
            invSlotsTag.setTag(invSlot.name, invSlotTag)
        }

        tag.setTag("inventory", invSlotsTag)

        return tag
    }

    override fun hasCustomName() = true

    override fun isEmpty(): Boolean {
        for (invSlot in this.invSlots) {
            if (!invSlot.isEmpty()) {
                return false
            }
        }
        return true
    }

    override fun getSizeInventory(): Int {
        var ret = 0
        for (invSlot in this.invSlots)
            ret += invSlot.size()
        return ret
    }

    override fun canInsertItem(index: Int, itemStack: ItemStack, side: EnumFacing): Boolean {
        if (isStackEmpty(itemStack)) return false
        val targetSlot = getInvSlot(index) ?: return false

        if (!targetSlot.canInput() || !targetSlot.accepts(itemStack))
            return false
        if (targetSlot.preferredSide !== InventorySlot.InvSide.ANY && targetSlot.preferredSide.matches(side))
            return true

        for (invSlot in this.invSlots) {
            if (invSlot != targetSlot && invSlot.preferredSide !== InventorySlot.InvSide.ANY && invSlot.preferredSide.matches(side)
                    && invSlot.canInput() && invSlot.accepts(itemStack)) {
                return false
            }
        }

        return true
    }

    override fun canExtractItem(index: Int, itemStack: ItemStack, side: EnumFacing): Boolean {
        val targetSlot = getInvSlot(index) ?: return false

        if (!targetSlot.canOutput())
            return false
        if (targetSlot.preferredSide !== InventorySlot.InvSide.ANY && targetSlot.preferredSide.matches(side))
            return true

        for (invSlot in this.invSlots) {
            if (invSlot != targetSlot && invSlot.preferredSide !== InventorySlot.InvSide.ANY && invSlot.preferredSide.matches(side)
                    && invSlot.canOutput()) {
                return false
            }
        }

        return true
    }

    override fun removeStackFromSlot(index: Int): ItemStack {
        val ret = getStackInSlot(index)
        if (!isStackEmpty(ret))
            setInventorySlotContents(index, emptyStack)
        return ret
    }

    override fun getStackInSlot(index: Int): ItemStack {
        var i = index
        for (invSlot in this.invSlots) {
            if (i < invSlot.size())
                return invSlot[i]
            i -= invSlot.size()
        }

        return emptyStack
    }

    override fun getDrops(): List<ItemStack> {
        val list = ArrayList(super.getDrops())
        for (slot in invSlots)
            for (stack in slot)
                if (!isStackEmpty(stack))
                    list += stack
        return list
    }

    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null) {
                if (itemHandler[itemHandler.size - 1] == null) {
                    itemHandler[itemHandler.size - 1] = InvWrapper(this)
                }
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast<T>(this.itemHandler[this.itemHandler.size - 1])
            }
            if (itemHandler[facing.ordinal] == null) {
                itemHandler[facing.ordinal] = SidedInvWrapper(this, facing)
            }
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast<T>(this.itemHandler[facing.ordinal])
        }
        return super.getCapability(capability, facing)
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?) =
            capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing)

    override fun getSlotsForFace(side: EnumFacing?)
            = IntArray(sizeInventory, { it })

    override fun isItemValidForSlot(index: Int, stack: ItemStack): Boolean {
        val invSlot = getInvSlot(index)
        return invSlot != null && invSlot.canInput() && invSlot.accepts(stack)
    }

    override fun getInventoryStackLimit(): Int {
        var max = 0
        for (slot in this.invSlots) {
            max = maxOf(max, slot.stackSizeLimit)
        }
        return max
    }

    override fun openInventory(player: EntityPlayer?) {}
    override fun closeInventory(player: EntityPlayer?) {}

    override fun isUsableByPlayer(player: EntityPlayer?) =
            !isInvalid && player!!.getDistanceSq(pos) <= 64

    override fun markDirty() {
        super.markDirty()

        for (invSlot in invSlots)
            invSlot.markDirty()
    }

    override fun setInventorySlotContents(index: Int, stack: ItemStack) {
        var ret = index
        for (invSlot in invSlots)
            if (ret < invSlot.size()) {
                invSlot.put(ret, stack)
            } else
                ret -= invSlot.size()
    }

    override fun decrStackSize(index: Int, count: Int): ItemStack {
        var itemStack = getStackInSlot(index)
        if (isStackEmpty(itemStack)) return emptyStack

        if (count >= getCount(itemStack)) {
            setInventorySlotContents(index, emptyStack)
            return itemStack
        }

        itemStack = shrink(itemStack)
        return itemStack.copyWithNewCount(count)
    }

    fun addInvSlot(invSlot: InventorySlot) =
            this.invSlots.add(invSlot)

    private fun getInvSlot(index: Int): InventorySlot? {
        var i = index
        for (invSlot in this.invSlots) {
            if (i < invSlot.size()) {
                return invSlot
            }
            i -= invSlot.size()
        }

        return null
    }

    override fun clear() {
        for (invSlot in this.invSlots)
            invSlot.clear()
    }

    override fun getDisplayName() = TextComponentString(name)

    override fun getField(id: Int) = 0
    override fun setField(id: Int, value: Int) {}
    override fun getFieldCount() = 0
}