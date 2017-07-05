/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.inventory

import net.minecraft.item.ItemStack
import waterpower.common.block.tile.TileEntityInventory
import waterpower.common.recipe.IRecipeManager
import waterpower.common.recipe.RecipeOutput
import waterpower.util.isStackEmpty
import waterpower.util.set
import java.util.*

class InventorySlotProcessableGeneric(base: TileEntityInventory, name: String, count: Int, var recipeManager: IRecipeManager) : InventorySlotProcessable(base, name, count) {

    override fun accepts(stack: ItemStack?): Boolean {
        if (isStackEmpty(stack)) return false
        val tmp = stack!!.copy().set(2147483647)

        return getOutput(tmp, false, true) != null
    }

    override fun process(): RecipeOutput? {
        val input = get()
        if (isStackEmpty(input) && !allowEmptyInput())
            return null

        val output = getOutput(input!!, false, false) ?: return null

        val itemsCopy = ArrayList<ItemStack>(output.items.size)

        for (itemStack in output.items) {
            itemsCopy.add(itemStack.copy())
        }

        return RecipeOutput(itemsCopy)
    }

    override fun consume() {
        val input = get()
        if (isStackEmpty(input) && !allowEmptyInput())
            throw IllegalStateException("consume from empty slot")

        val output = getOutput(input!!, true, false) ?: throw IllegalStateException("consume without a processing result")

        if (isStackEmpty(input))
            clear()
    }

    protected fun getOutput(input: ItemStack, adjustInput: Boolean, forAccept: Boolean): RecipeOutput? {
        return this.recipeManager.getOutput(input, adjustInput)
    }

    fun getRecipeOutput(): RecipeOutput? {
        val input = get()
        if (isStackEmpty(input)) return null
        return getOutput(input!!, false, false)
    }

    protected fun allowEmptyInput(): Boolean {
        return false
    }
}