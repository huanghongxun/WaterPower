/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.recipe

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import java.util.*

typealias RecipeAddPredicate = (ItemStack, ItemStack) -> Boolean
typealias BlastFurnaceRecipeAddPredicate = (ItemStack, ItemStack, Int) -> Boolean
typealias BlastFurnaceOreDictRecipeAddPredicate = (String, ItemStack, Int) -> Boolean

object Recipes {
    val lathes = LinkedList<RecipeAddPredicate>()
    val crushers = LinkedList<RecipeAddPredicate>()
    val sawmills = LinkedList<RecipeAddPredicate>()
    val cutters = LinkedList<RecipeAddPredicate>()
    val compressors = LinkedList<RecipeAddPredicate>()
    val benders = LinkedList<RecipeAddPredicate>()
    val blastFurnaces = LinkedList<BlastFurnaceRecipeAddPredicate>()
    val blastFurnacesOreDict = LinkedList<BlastFurnaceOreDictRecipeAddPredicate>()

    fun bender(input: ItemStack, output: ItemStack): Boolean {
        var flag = false
        for (predicate in benders)
            flag = flag or predicate(input, output)
        return flag
    }

    fun blastFurnace(input: ItemStack, output: ItemStack, time: Int): Boolean {
        var flag = false
        for (predicate in blastFurnaces)
            flag = flag or predicate(input, output, time)
        return flag
    }

    fun compressor(input: ItemStack, output: ItemStack): Boolean {
        var flag = false
        for (predicate in compressors)
            flag = flag or predicate(input, output)
        return flag
    }

    fun crusher(input: ItemStack, output: ItemStack): Boolean {
        var flag = false
        for (predicate in crushers)
            flag = flag or predicate(input, output)
        if (!flag)
            flag = flag or RecipeManagers.crusher.addRecipe(input, output)
        return flag
    }

    fun cutter(input: ItemStack, output: ItemStack): Boolean {
        var flag = false
        for (predicate in cutters)
            flag = flag or predicate(input, output)
        return flag
    }

    fun lathe(input: ItemStack, output: ItemStack): Boolean {
        var flag = false
        for (predicate in lathes)
            flag = flag or predicate(input, output)
        return flag
    }

    fun furnace(input: ItemStack, output: ItemStack) {
        GameRegistry.addSmelting(input, output, 0.0f)
    }

    fun doesOreNameExist(name: String)
            = OreDictionary.getOres(name).isNotEmpty()

    fun craft(output: ItemStack, vararg pars: Any?) {
        GameRegistry.addRecipe(ShapedOreRecipe(output, *pars))
    }

    fun craftShapeless(output: ItemStack, vararg pars: Any?) {
        GameRegistry.addRecipe(ShapelessOreRecipe(output, *pars))
    }

}