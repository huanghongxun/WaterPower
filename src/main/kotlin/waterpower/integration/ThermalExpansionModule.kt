/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.event.FMLInterModComms
import waterpower.annotations.Integration
import waterpower.common.recipe.Recipes
import waterpower.util.emptyStack
import waterpower.util.isStackEmpty

@Integration(IDs.ThermalExpansion)
object ThermalExpansionModule : IModule() {

    fun addFurnaceRecipe(energy: Int, input: ItemStack, output: ItemStack): Boolean {
        val toSend = NBTTagCompound()
        toSend.setInteger("energy", energy)
        toSend.setTag("input", NBTTagCompound())
        toSend.setTag("output", NBTTagCompound())
        input.writeToNBT(toSend.getCompoundTag("input"))
        output.writeToNBT(toSend.getCompoundTag("output"))
        return FMLInterModComms.sendMessage(IDs.ThermalExpansion, "AddFurnaceRecipe", toSend)
    }

    fun furnace(input: ItemStack, output: ItemStack): Boolean {
        return addFurnaceRecipe(3200, input, output)
    }

    fun addCompactorPressRecipe(energy: Int, input: ItemStack, output: ItemStack): Boolean {
        val toSend = NBTTagCompound()
        toSend.setInteger("energy", energy)
        toSend.setTag("input", NBTTagCompound())
        toSend.setTag("output", NBTTagCompound())
        input.writeToNBT(toSend.getCompoundTag("input"))
        output.writeToNBT(toSend.getCompoundTag("output"))
        return FMLInterModComms.sendMessage(IDs.ThermalExpansion, "AddCompactorPressRecipe", toSend)
    }

    fun bender(input: ItemStack, output: ItemStack): Boolean {
        return addCompactorPressRecipe(3200, input, output)
    }

    fun addCompactorStorageRecipe(energy: Int, input: ItemStack, output: ItemStack): Boolean {
        val toSend = NBTTagCompound()
        toSend.setInteger("energy", energy)
        toSend.setTag("input", NBTTagCompound())
        toSend.setTag("output", NBTTagCompound())
        input.writeToNBT(toSend.getCompoundTag("input"))
        output.writeToNBT(toSend.getCompoundTag("output"))
        return FMLInterModComms.sendMessage(IDs.ThermalExpansion, "AddCompactorStorageRecipe", toSend)
    }

    fun compressor(input: ItemStack, output: ItemStack): Boolean {
        return addCompactorStorageRecipe(3200, input, output)
    }

    fun addPulverizerRecipe(energy: Int, input: ItemStack, primaryOutput: ItemStack) =
            addPulverizerRecipe(energy, input, primaryOutput, emptyStack, 0)

    fun addPulverizerRecipe(energy: Int, input: ItemStack, primaryOutput: ItemStack, secondaryOutput: ItemStack, secondaryChance: Int = 100): Boolean {
        if (isStackEmpty(input) || isStackEmpty(primaryOutput))
            return false
        val toSend = NBTTagCompound()
        toSend.setInteger("energy", energy)
        toSend.setTag("input", NBTTagCompound())
        toSend.setTag("primaryOutput", NBTTagCompound())
        toSend.setTag("secondaryOutput", NBTTagCompound())
        input.writeToNBT(toSend.getCompoundTag("input"))
        primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"))
        secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"))
        toSend.setInteger("secondaryChance", secondaryChance)
        return FMLInterModComms.sendMessage(IDs.ThermalExpansion, "AddPulverizerRecipe", toSend)
    }

    fun crusher(input: ItemStack, output: ItemStack) =
            addPulverizerRecipe(3200, input, output)

    fun addSawmillRecipe(energy: Int, input: ItemStack, primaryOutput: ItemStack) =
            addSawmillRecipe(energy, input, primaryOutput, emptyStack, 0)

    fun addSawmillRecipe(energy: Int, input: ItemStack, primaryOutput: ItemStack, secondaryOutput: ItemStack, secondaryChance: Int = 100): Boolean {
        if (isStackEmpty(input) || isStackEmpty(primaryOutput))
            return false
        val toSend = NBTTagCompound()
        toSend.setInteger("energy", energy)
        toSend.setTag("input", NBTTagCompound())
        toSend.setTag("primaryOutput", NBTTagCompound())
        toSend.setTag("secondaryOutput", NBTTagCompound())
        input.writeToNBT(toSend.getCompoundTag("input"))
        primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"))
        secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"))
        toSend.setInteger("secondaryChance", secondaryChance)
        return FMLInterModComms.sendMessage(IDs.ThermalExpansion, "AddSawmillRecipe", toSend)
    }

    fun sawmill(input: ItemStack, output: ItemStack) =
            addSawmillRecipe(3200, input, output)

    fun addSmelterRecipe(energy: Int, primaryInput: ItemStack, secondaryInput: ItemStack, primaryOutput: ItemStack) =
            addSmelterRecipe(energy, primaryInput, secondaryInput, primaryOutput, emptyStack, 0)

    fun addSmelterRecipe(energy: Int, primaryInput: ItemStack, secondaryInput: ItemStack, primaryOutput: ItemStack, secondaryOutput: ItemStack,
                         secondaryChance: Int = 100): Boolean {
        if (isStackEmpty(primaryInput) || isStackEmpty(secondaryInput) || isStackEmpty(primaryOutput))
            return false
        val toSend = NBTTagCompound()
        toSend.setInteger("energy", energy)
        toSend.setTag("primaryInput", NBTTagCompound())
        toSend.setTag("secondaryInput", NBTTagCompound())
        toSend.setTag("primaryOutput", NBTTagCompound())
        toSend.setTag("secondaryOutput", NBTTagCompound())
        primaryInput.writeToNBT(toSend.getCompoundTag("primaryInput"))
        secondaryInput.writeToNBT(toSend.getCompoundTag("secondaryInput"))
        primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"))
        secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"))
        toSend.setInteger("secondaryChance", secondaryChance)
        return FMLInterModComms.sendMessage(IDs.ThermalExpansion, "AddSmelterRecipe", toSend)
    }

    fun blastFurnace(input: ItemStack, output: ItemStack, time: Int) =
            addSmelterRecipe(10000, input, emptyStack, output)

    override fun onInit() {
        super.onInit()

        //Recipes.furnaces += this::furnace
        Recipes.benders += this::bender
        Recipes.compressors += this::compressor
        Recipes.crushers += this::crusher
        Recipes.sawmills += this::sawmill
    }
}
