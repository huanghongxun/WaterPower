/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.util

import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.oredict.OreDictionary
import java.util.*

val emptyStack: ItemStack? = null

fun ItemStack.generalize()
        = ItemStack(this.item, getCount(this), OreDictionary.WILDCARD_VALUE)

fun isStackEmpty(stack: ItemStack?) =
        stack == null || getCount(stack) <= 0 || stack.item == null

fun ItemStack.set(newCount: Int): ItemStack {
    stackSize = newCount
    return this
}

fun shrink(stack: ItemStack, count: Int = 1): ItemStack? {
    stack.stackSize -= count
    if (isStackEmpty(stack)) return emptyStack
    else return stack
}

fun grow(stack: ItemStack, count: Int = 1) = shrink(stack, -count)

fun getCount(stack: ItemStack) = stack.stackSize

fun ItemStack.dropAsEntity(world: World, x: Int, y: Int, z: Int) {
    val f = 0.7
    val dx = world.rand.nextFloat() * f + (1.0 - f) * 0.5
    val dy = world.rand.nextFloat() * f + (1.0 - f) * 0.5
    val dz = world.rand.nextFloat() * f + (1.0 - f) * 0.5
    val entityItem = EntityItem(world, x + dx, y + dy, z + dz, this.copy())
    world.spawnEntityInWorld(entityItem)
}

fun ItemStack.copyWithNewCount(newCount: Int)
        = this.copy().set(newCount)

fun ItemStack.damag(amount: Int): Boolean {
    if (attemptDamageItem(amount, rand)) {
        shrink(this, 1)
        itemDamage = 0
        return isStackEmpty(this)
    }
    return false
}

fun ItemStack.consume(): ItemStack? {
    if (getCount(this) == 1) {
        if (item.hasContainerItem(this))
            return item.getContainerItem(this)
        else
            return emptyStack
    }
    splitStack(1)
    return this
}

fun getCopiedStacks(stack: Array<ItemStack?>): Array<ItemStack?> {
    return Array<ItemStack?>(stack.size, { stack[it]?.copy() })
}

fun isStackEqual(a: ItemStack?, b: ItemStack?) =
        isStackEmpty(a) && isStackEmpty(b) || (!isStackEmpty(a) && !isStackEmpty(b) && a!!.isItemEqual(b!!) && Objects.equals(a.tagCompound, b.tagCompound))

fun isStacksEqual(stacks1: Array<ItemStack?>, stacks2: Array<ItemStack?>): Boolean {
    if (stacks1.size != stacks2.size)
        return false
    for (i in stacks1.indices)
        if (!isStackEqual(stacks1[i], stacks2[i]))
            return false
    return true
}

private val rand = Random()