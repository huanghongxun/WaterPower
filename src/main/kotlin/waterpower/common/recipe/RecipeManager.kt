/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.recipe

import net.minecraft.item.ItemStack
import java.util.*

class RecipeManager : IRecipeManager {
    private val recipes = HashMap<IRecipeInput, RecipeOutput>()
    private val singleOutputRecipes = ArrayList<HashMap<ItemStack, ItemStack>>()

    override fun addRecipe(input: ItemStack, vararg outputs: ItemStack): Boolean {
        if (input.isEmpty)
            return false

        for (i in outputs.indices) {
            if (outputs[i].isEmpty)
                throw NullPointerException("The output ItemStack #$i is empty (counting from 0)")
        }

        for (existingInput in this.recipes.keys)
            if (existingInput.matches(input))
                return false
        this.recipes.put(RecipeInputItemStack(input), RecipeOutput(*outputs))
        return true
    }

    override fun removeRecipe(input: ItemStack): Boolean {
        if (input.isEmpty)
            throw NullPointerException("The recipe input is null")

        val r = LinkedList<IRecipeInput>()

        for (existingInput in this.recipes.keys)
            if (existingInput.matches(input))
                r.add(existingInput)
        for (s in r)
            recipes.remove(s)
        return !r.isEmpty()
    }

    override fun getOutput(input: ItemStack, adjustInput: Boolean): RecipeOutput? {
        var input: ItemStack = input
        if (input.isEmpty)
            return null

        for ((recipeInput, recipeOutput) in recipes.entries) {
            if (recipeInput.matches(input)) {
                if (input.count < recipeInput.getAmount() || input.item.hasContainerItem() && input.count != recipeInput.getAmount())
                    break
                if (adjustInput) {
                    if (input.item.hasContainerItem()) {
                        val container = input.item.getContainerItem(input)

                        input = container.copy()
                    } else {
                        input.shrink(recipeInput.getAmount())
                    }
                }

                return recipeOutput
            }

        }

        return null
    }

    override fun getAllRecipes(): Map<IRecipeInput, RecipeOutput> {
        return this.recipes
    }

    fun registerSingleOutputRecipes(map: HashMap<ItemStack, ItemStack>?) {
        if (map == null)
            return
        this.singleOutputRecipes.add(map)
    }
}