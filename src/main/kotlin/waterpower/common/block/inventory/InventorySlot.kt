/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.inventory

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.EnumFacing
import waterpower.common.block.tile.TileEntityInventory
import waterpower.util.getCopiedStacks
import waterpower.util.isStackEqual
import waterpower.util.isStacksEqual
import java.util.*
import kotlin.NoSuchElementException

open class InventorySlot(val tileEntity: TileEntityInventory, val name: String, protected val access: InventorySlot.Access, count: Int, val preferredSide: InventorySlot.InvSide = InventorySlot.InvSide.ANY)
    : Iterable<ItemStack> {
    protected val contents = Array<ItemStack>(count, { ItemStack.EMPTY })
    var stackSizeLimit = 64

    init {
        tileEntity.addInvSlot(this)
    }

    fun readFromNBT(nbtTagCompound: NBTTagCompound) {
        val contentsTag = nbtTagCompound.getTagList("Contents", 10)

        for (i in 0..contentsTag.tagCount() - 1) {
            val contentTag = contentsTag.getCompoundTagAt(i)

            val index = contentTag.getByte("Index").toInt() and 0xFF
            val itemStack = ItemStack(contentTag)
            put(index, itemStack)
        }
    }

    fun writeToNBT(nbtTagCompound: NBTTagCompound) {
        val contentsTag = NBTTagList()

        for (i in this.contents.indices) {
            if (this.contents[i].isEmpty) {
                continue
            }
            val contentTag = NBTTagCompound()

            contentTag.setByte("Index", i.toByte())
            this.contents[i].writeToNBT(contentTag)

            contentsTag.appendTag(contentTag)
        }

        nbtTagCompound.setTag("Contents", contentsTag)
    }

    fun size() = this.contents.size

    operator fun get(index: Int = 0) = this.contents[index]

    fun put(content: ItemStack) = put(0, content)

    fun put(index: Int, content: ItemStack) {
        this.contents[index] = content
    }

    fun clear() = Arrays.fill(contents, ItemStack.EMPTY)
    fun clear(index: Int) = put(index, ItemStack.EMPTY)

    open fun accepts(stack: ItemStack) = true

    fun markDirty() {}

    open fun canInput() = this.access == Access.I || this.access == Access.IO
    open fun canOutput() = this.access == Access.O || this.access == Access.IO

    fun isEmpty(): Boolean {
        for (itemStack in this.contents)
            if (!itemStack.isEmpty)
                return false

        return true
    }

    fun organize() {
        for (dstIndex in 0..this.contents.size - 1 - 1) {
            var dst: ItemStack = this.contents[dstIndex]

            if (!dst.isEmpty && dst.count >= dst.maxStackSize)
                continue
            for (srcIndex in dstIndex + 1..this.contents.size - 1) {
                val src = this.contents[srcIndex]
                if (src.isEmpty) continue
                if (dst.isEmpty) {
                    this.contents[srcIndex] = ItemStack.EMPTY
                    val tmp85_83 = src
                    dst = tmp85_83
                    this.contents[dstIndex] = tmp85_83
                } else if (isStackEqual(dst, src)) {
                    val space = dst.maxStackSize - dst.count

                    if (src.count <= space) {
                        this.contents[srcIndex] = ItemStack.EMPTY
                        dst.count += src.count
                    } else {
                        src.shrink(space)
                        dst.grow(space)
                        break
                    }
                }
            }
        }
    }

    override fun toString(): String {
        var ret = "$name[${this.contents.size}]: "

        for (i in this.contents.indices) {
            ret += this.contents[i]

            if (i >= this.contents.size - 1)
                continue
            ret += ", "
        }

        return ret
    }

    override fun iterator(): Iterator<ItemStack> {
        return object : Iterator<ItemStack> {
            var idx = 0

            override fun next(): ItemStack {
                if (idx >= this@InventorySlot.contents.size)
                    throw NoSuchElementException()
                else
                    return this@InventorySlot.contents[idx++]
            }

            override fun hasNext() = idx < this@InventorySlot.contents.size
        }
    }

    val copiedContent: Array<ItemStack>
        get() = getCopiedStacks(contents)


    fun isEquals(stack: Array<ItemStack>): Boolean {
        return isStacksEqual(stack, contents)
    }

    enum class InvSide(val acceptedSides: Set<EnumFacing>) {
        ANY(setOf(EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST)),
        TOP(setOf(EnumFacing.UP)),
        BOTTOM(setOf(EnumFacing.DOWN)),
        SIDE(setOf(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST)),
        NOTSIDE(emptySet());

        fun matches(side: EnumFacing) = acceptedSides.contains(side)
    }

    enum class Access {
        NONE, I, O, IO;

        fun isInput() = this == I || this == IO

        fun isOutput() = this == O || this == IO
    }
}