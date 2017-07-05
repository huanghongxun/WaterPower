/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.inventory

import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import waterpower.common.block.tile.TileEntityInventory
import waterpower.util.*
import java.util.*

abstract class InventorySlotConsumable : InventorySlot {

    constructor(base: TileEntityInventory, name: String, count: Int) : super(base, name, InventorySlot.Access.I, count, InventorySlot.InvSide.TOP) {}

    constructor(base: TileEntityInventory, name: String, access: InventorySlot.Access, count: Int, preferredSide: InventorySlot.InvSide) : super(base, name, access, count, preferredSide) {}

    abstract override fun accepts(stack: ItemStack?): Boolean

    override fun canOutput(): Boolean {
        return super.canOutput() || this.access !== InventorySlot.Access.NONE && !isStackEmpty(get()) && !accepts(get()!!)
    }

    fun consume(count: Int, simulate: Boolean = false, consumeContainers: Boolean = false): ItemStack? {
        var amount = count
        var ret = emptyStack

        for (i in 0..size() - 1) {
            var itemStack = get(i)

            if (isStackEmpty(itemStack) || !accepts(itemStack!!) || !isStackEmpty(ret) && !isStackEqual(itemStack, ret)
                    || getCount(itemStack) != 1 && !consumeContainers && itemStack.item.hasContainerItem()) {
                continue
            }

            val currentAmount = minOf(amount, getCount(itemStack))

            amount -= currentAmount

            if (!simulate)
                if (getCount(itemStack) == currentAmount)
                    if (!consumeContainers && itemStack.item.hasContainerItem())
                        put(i, itemStack.item.getContainerItem(itemStack))
                    else
                        clear(i)
                else
                    itemStack = shrink(itemStack)

            if (isStackEmpty(ret))
                ret = itemStack!!.copyWithNewCount(currentAmount)
            else
                ret = grow(ret!!)

            if (amount == 0)
                break
        }
        return ret
    }

    fun damage(count: Int, src: EntityLivingBase? = null): ItemStack {
        var amount = count
        var ret = emptyStack
        var damageApplied = 0

        var i = 0
        while (i < size()) {
            var itemStack = get(i)

            if (isStackEmpty(itemStack) || !accepts(itemStack!!) || !itemStack.item.isDamageable
                    || !isStackEmpty(ret) && (itemStack.item !== ret!!.item || !ItemStack.areItemStackTagsEqual(itemStack, ret))) {
                ++i
                continue
            }
            val currentAmount = minOf(amount, itemStack.maxDamage - itemStack.itemDamage)

            damageApplied += currentAmount
            amount -= currentAmount

            if (src != null)
                itemStack.damageItem(currentAmount, src)
            else {
                itemStack.attemptDamageItem(currentAmount, random)
            }

            if (itemStack.itemDamage >= itemStack.maxDamage) {
                itemStack = shrink(itemStack)
                itemStack!!.itemDamage = 0
            }

            if (isStackEmpty(itemStack))
                put(i, emptyStack)
            else {
                i--
            }

            if (isStackEmpty(ret))
                ret = itemStack.copy()

            if (amount == 0) {
                break
            }
            ++i
        }
        ret!!.set(damageApplied / ret.maxDamage)
        ret.itemDamage = damageApplied % ret.maxDamage

        return ret
    }

    companion object {
        private val random = Random()
    }
}