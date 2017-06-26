/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.crafttweaker

import minetweaker.MineTweakerAPI
import minetweaker.api.item.IItemStack
import minetweaker.api.minecraft.MineTweakerMC
import stanhebben.zenscript.annotations.NotNull
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod
import waterpower.common.recipe.IRecipeManager
import waterpower.common.recipe.RecipeManagers


@ZenClass("mods.waterpower.Machines")
object Machines {

    @ZenMethod
    fun addCrusherRecipe(@NotNull input: IItemStack, @NotNull output: IItemStack) {
        MineTweakerAPI.apply(AddRecipeAction("WaterPower Crusher", RecipeManagers.crusher, MineTweakerMC.getItemStack(input), MineTweakerMC
                .getItemStack(output)))
    }

    @ZenMethod
    fun addLatheRecipe(@NotNull input: IItemStack, @NotNull output: IItemStack) {
        MineTweakerAPI.apply(AddRecipeAction("WaterPower Lathe", RecipeManagers.lathe, MineTweakerMC.getItemStack(input), MineTweakerMC.getItemStack(output)))
    }

    @ZenMethod
    fun addCutterRecipe(@NotNull input: IItemStack, @NotNull output: IItemStack) {
        MineTweakerAPI.apply(AddRecipeAction("WaterPower Cutter", RecipeManagers.cutter, MineTweakerMC.getItemStack(input), MineTweakerMC.getItemStack(output)))
    }

    @ZenMethod
    fun addCompressorRecipe(@NotNull input: IItemStack, @NotNull output: IItemStack) {
        MineTweakerAPI.apply(AddRecipeAction("WaterPower Compressor", RecipeManagers.compressor, MineTweakerMC.getItemStack(input), MineTweakerMC
                .getItemStack(output)))
    }

    @ZenMethod
    fun addCentrifugeRecipe(@NotNull input: IItemStack, @NotNull output: IItemStack) {
        MineTweakerAPI.apply(AddRecipeAction("WaterPower Centrifuge", RecipeManagers.centrifuge, MineTweakerMC.getItemStack(input), MineTweakerMC
                .getItemStack(output)))
    }

    @ZenMethod
    fun addAdvancedCompressorRecipe(@NotNull input: IItemStack, @NotNull output: IItemStack) {
        MineTweakerAPI.apply(AddRecipeAction("WaterPower Advanced Compressor", RecipeManagers.advCompressor, MineTweakerMC.getItemStack(input), MineTweakerMC
                .getItemStack(output)))
    }

    @ZenMethod
    fun addSawmillRecipe(@NotNull input: IItemStack, @NotNull output: IItemStack) {
        MineTweakerAPI
                .apply(AddRecipeAction("WaterPower Sawmill", RecipeManagers.sawmill, MineTweakerMC.getItemStack(input), MineTweakerMC.getItemStack(output)))
    }

    @JvmStatic
    @ZenMethod
    fun removeCrusherRecipe(@NotNull input: IItemStack) {
        MineTweakerAPI.apply(RemoveRecipeAction("WaterPower Crusher", RecipeManagers.crusher, MineTweakerMC.getItemStack(input)))
    }

    @JvmStatic
    @ZenMethod
    fun removeLatheRecipe(@NotNull input: IItemStack) {
        MineTweakerAPI.apply(RemoveRecipeAction("WaterPower Lathe", RecipeManagers.lathe, MineTweakerMC.getItemStack(input)))
    }

    @ZenMethod
    fun removeCutterRecipe(@NotNull input: IItemStack) {
        MineTweakerAPI.apply(RemoveRecipeAction("WaterPower Cutter", RecipeManagers.cutter, MineTweakerMC.getItemStack(input)))
    }

    @ZenMethod
    fun removeCompressorRecipe(@NotNull input: IItemStack) {
        MineTweakerAPI.apply(RemoveRecipeAction("WaterPower Compressor", RecipeManagers.compressor, MineTweakerMC.getItemStack(input)))
    }

    @ZenMethod
    fun removeCentrifugeRecipe(@NotNull input: IItemStack) {
        MineTweakerAPI.apply(RemoveRecipeAction("WaterPower Centrifuge", RecipeManagers.centrifuge, MineTweakerMC.getItemStack(input)))
    }

    @ZenMethod
    fun removeAdvancedCompressorRecipe(@NotNull input: IItemStack) {
        MineTweakerAPI.apply(RemoveRecipeAction("WaterPower Advanced Compressor", RecipeManagers.advCompressor, MineTweakerMC.getItemStack(input)))
    }

    @ZenMethod
    fun removeSawmillRecipe(@NotNull input: IItemStack, @NotNull output: IItemStack) {
        MineTweakerAPI.apply(RemoveRecipeAction("WaterPower Sawmill", RecipeManagers.sawmill, MineTweakerMC.getItemStack(input)))
    }

    private fun getMachineOutput(input: IItemStack, rm: IRecipeManager): IItemStack? {
        val output = rm.getOutput(MineTweakerMC.getItemStack(input), false)
        if (output == null || output.items.isEmpty())
            return null
        return MineTweakerMC.getIItemStack(output.items.first())
    }

    @JvmStatic
    @ZenMethod
    fun getCrusherOutput(input: IItemStack): IItemStack? {
        return getMachineOutput(input, RecipeManagers.crusher)
    }

    @JvmStatic
    @ZenMethod
    fun getAdvancedCompressorOutput(input: IItemStack): IItemStack? {
        return getMachineOutput(input, RecipeManagers.advCompressor)
    }

    @JvmStatic
    @ZenMethod
    fun getCentrifugeOutput(input: IItemStack): IItemStack? {
        return getMachineOutput(input, RecipeManagers.centrifuge)
    }

    @JvmStatic
    @ZenMethod
    fun getCompressorOutput(input: IItemStack): IItemStack? {
        return getMachineOutput(input, RecipeManagers.compressor)
    }

    @JvmStatic
    @ZenMethod
    fun getCutterOutput(input: IItemStack): IItemStack? {
        return getMachineOutput(input, RecipeManagers.cutter)
    }

    @JvmStatic
    @ZenMethod
    fun getLatheOutput(input: IItemStack): IItemStack? {
        return getMachineOutput(input, RecipeManagers.lathe)
    }

    @JvmStatic
    @ZenMethod
    fun getSawmillOutput(input: IItemStack): IItemStack? {
        return getMachineOutput(input, RecipeManagers.sawmill)
    }

}