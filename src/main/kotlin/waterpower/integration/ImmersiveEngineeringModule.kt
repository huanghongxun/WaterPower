/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.Optional
import waterpower.annotations.Integration
import waterpower.annotations.forMethod
import waterpower.common.recipe.Recipes
import waterpower.util.emptyStack

@Integration(IDs.ImmersiveEngineering)
object ImmersiveEngineeringModule : IModule() {
    private var method: java.lang.reflect.Method? = null

    @Optional.Method(modid = IDs.ImmersiveEngineering)
    fun blastFurnace(input: Any, cookTime: Int, output: ItemStack, slag: ItemStack?): Boolean {
        try {
            if (method == null) {
                method = forMethod(Class.forName("blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe"), "addRecipe",
                        emptyStack, Any(), 0, emptyStack)
            }
            method!!.invoke(null, output, input, cookTime, slag)
            return true
        } catch (e: Throwable) {
            return false
        }
    }

    fun blastFurnace(input: ItemStack, output: ItemStack, time: Int) =
            blastFurnace(input, time, output, emptyStack)

    fun blastFurnace(input: String, output: ItemStack, time: Int) =
            blastFurnace(input, time, output, emptyStack)

    override fun onInit() {
        super.onInit()

        Recipes.blastFurnaces += this::blastFurnace
        Recipes.blastFurnacesOreDict += this::blastFurnace
    }
}