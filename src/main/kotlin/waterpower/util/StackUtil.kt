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
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.oredict.OreDictionary
import java.util.*

val emptyStack: ItemStack = ItemStack.EMPTY

fun ItemStack.generalize()
        = ItemStack(this.item, getCount(this), OreDictionary.WILDCARD_VALUE)

fun isStackEmpty(stack: ItemStack?) =
        stack == null || getCount(stack) <= 0 || stack.item == null || stack.isEmpty

fun ItemStack.set(newCount: Int): ItemStack {
    count = newCount
    return this
}

fun ItemStack.withNBT(map: Map<String, Any>): ItemStack {
    if (tagCompound == null)
        tagCompound = NBTTagCompound()
    for ((key, value) in map.entries) {
        when (value) {
            is Int -> tagCompound?.setInteger(key, value)
            is Boolean -> tagCompound?.setBoolean(key, value)
            is Double -> tagCompound?.setDouble(key, value)
            is Float -> tagCompound?.setFloat(key, value)
            is Byte -> tagCompound?.setByte(key, value)
            is Long -> tagCompound?.setLong(key, value)
            is Short -> tagCompound?.setShort(key, value)
            is String -> tagCompound?.setString(key, value)
            is NBTBase -> tagCompound?.setTag(key, value)
            is ByteArray -> tagCompound?.setByteArray(key, value)
            is IntArray -> tagCompound?.setIntArray(key, value)
        }
    }
    return this
}

fun shrink(stack: ItemStack, count: Int = 1): ItemStack {
    stack.shrink(count)
    if (isStackEmpty(stack)) return emptyStack
    else return stack
}

fun grow(stack: ItemStack, count: Int = 1) = shrink(stack, -count)

fun getCount(stack: ItemStack) = stack.count

fun ItemStack.dropAsEntity(world: World, x: Int, y: Int, z: Int) {
    val f = 0.7
    val dx = world.rand.nextFloat() * f + (1.0 - f) * 0.5
    val dy = world.rand.nextFloat() * f + (1.0 - f) * 0.5
    val dz = world.rand.nextFloat() * f + (1.0 - f) * 0.5
    val entityItem = EntityItem(world, x + dx, y + dy, z + dz, this.copy())
    world.spawnEntity(entityItem)
}

fun ItemStack.copyWithNewCount(newCount: Int)
        = this.copy().set(newCount)

fun ItemStack.damage(amount: Int): Boolean {
    if (attemptDamageItem(amount, rand, null)) {
        shrink(1)
        itemDamage = 0
        return isEmpty
    }
    return false
}

fun ItemStack.consume(): ItemStack {
    if (getCount(this) == 1) {
        if (item.hasContainerItem(this))
            return item.getContainerItem(this)
        else
            return emptyStack
    }
    splitStack(1)
    return this
}

fun getCopiedStacks(stack: Array<ItemStack>): Array<ItemStack> {
    return Array<ItemStack>(stack.size, { stack[it].copy() })
}

fun isStackEqual(a: ItemStack, b: ItemStack) =
        isStackEmpty(a) && isStackEmpty(b) || (!isStackEmpty(a) && !isStackEmpty(b) && a.isItemEqual(b) && Objects.equals(a.tagCompound, b.tagCompound))

fun isStacksEqual(stacks1: Array<ItemStack>, stacks2: Array<ItemStack>): Boolean {
    if (stacks1.size != stacks2.size)
        return false
    for (i in stacks1.indices)
        if (!isStackEqual(stacks1[i], stacks2[i]))
            return false
    return true
}

private val rand = Random()