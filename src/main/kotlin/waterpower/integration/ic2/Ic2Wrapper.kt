/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.ic2

import com.google.common.collect.Lists
import ic2.api.recipe.IBasicMachineRecipeManager
import ic2.api.recipe.Recipes
import net.minecraft.item.ItemStack
import waterpower.annotations.callImpl
import waterpower.common.recipe.*
import java.util.*

class Ic2Wrapper(internal var containsIC2Recipe: Any) : IRecipeManager {

    override fun addRecipe(input: ItemStack, vararg outputs: ItemStack): Boolean {
        try {
            (containsIC2Recipe as IBasicMachineRecipeManager).addRecipe(Recipes.inputFactory.forStack(input), Lists.newArrayList(*outputs), null, true)
            return true
        } catch (t: Throwable) {
            t.printStackTrace()
            return false
        }

    }

    override fun getOutput(input: ItemStack, adjustInput: Boolean): RecipeOutput? {
        try {
            val r = (containsIC2Recipe as IBasicMachineRecipeManager).getOutputFor(input, adjustInput) ?: return null
            return RecipeOutput(r.items)
        } catch (t: Throwable) {
            t.printStackTrace()
            return null
        }

    }

    override fun getAllRecipes(): Map<IRecipeInput, RecipeOutput> {
        val map = HashMap<IRecipeInput, RecipeOutput>()
        try {
            for (entry in (containsIC2Recipe as IBasicMachineRecipeManager).recipes) {
                var input: IRecipeInput? = null
                val ic2Input = entry.input
                if (ic2Input is ic2.api.recipe.IRecipeInput) {
                    if (ic2Input.inputs.size == 1)
                        input = RecipeInputItemStack(ic2Input.inputs.single())
                    else if (ic2Input.javaClass.name.contains("Ore")) { // RecipeInputOreDict
                        try {
                            val x = callImpl(Class.forName("ic2.core.recipe.RecipeInputOreDict"), "input", ic2Input) as String?
                            if (x != null) input = RecipeInputOreDictionary(x)
                        } catch(tt: Throwable) {
                            tt.printStackTrace()
                        }
                    }
                }
                if (input != null)
                    map.put(input, RecipeOutput(entry.output))
            }
            return map
        } catch (t: Throwable) {
            t.printStackTrace()
            return map
        }
    }

    override fun removeRecipe(input: ItemStack): Boolean {
        return false
    }

}
