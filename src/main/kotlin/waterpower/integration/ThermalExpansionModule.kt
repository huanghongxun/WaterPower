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

@Integration(IDs.ThermalExpansion)
object ThermalExpansionModule : IModule() {
    fun addFurnaceRecipe(energy: Int, input: ItemStack, output: ItemStack) {
        val toSend = NBTTagCompound()
        toSend.setInteger("energy", energy)
        toSend.setTag("input", NBTTagCompound())
        toSend.setTag("output", NBTTagCompound())
        input.writeToNBT(toSend.getCompoundTag("input"))
        output.writeToNBT(toSend.getCompoundTag("output"))
        FMLInterModComms.sendMessage("ThermalExpansion", "FurnaceRecipe", toSend)
    }

    fun addPulverizerRecipe(energy: Int, input: ItemStack, primaryOutput: ItemStack) =
            addPulverizerRecipe(energy, input, primaryOutput, ItemStack.EMPTY, 0)

    fun addPulverizerRecipe(energy: Int, input: ItemStack, primaryOutput: ItemStack, secondaryOutput: ItemStack, secondaryChance: Int = 100): Boolean {
        if (input.isEmpty || primaryOutput.isEmpty)
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
        return FMLInterModComms.sendMessage("ThermalExpansion", "PulverizerRecipe", toSend)
    }

    fun crusher(input: ItemStack, output: ItemStack) =
            addPulverizerRecipe(3200, input, output)

    fun addSawmillRecipe(energy: Int, input: ItemStack, primaryOutput: ItemStack) =
            addSawmillRecipe(energy, input, primaryOutput, ItemStack.EMPTY, 0)

    fun addSawmillRecipe(energy: Int, input: ItemStack, primaryOutput: ItemStack, secondaryOutput: ItemStack, secondaryChance: Int = 100): Boolean {
        if (input.isEmpty || primaryOutput.isEmpty)
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
        return FMLInterModComms.sendMessage("ThermalExpansion", "SawmillRecipe", toSend)
    }

    fun sawmill(input: ItemStack, output: ItemStack) =
            addSawmillRecipe(3200, input, output)

    fun addSmelterRecipe(energy: Int, primaryInput: ItemStack, secondaryInput: ItemStack, primaryOutput: ItemStack) =
            addSmelterRecipe(energy, primaryInput, secondaryInput, primaryOutput, ItemStack.EMPTY, 0)

    fun addSmelterRecipe(energy: Int, primaryInput: ItemStack, secondaryInput: ItemStack, primaryOutput: ItemStack, secondaryOutput: ItemStack,
                         secondaryChance: Int = 100): Boolean {
        if (primaryInput.isEmpty || secondaryInput.isEmpty || primaryOutput.isEmpty)
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
        return FMLInterModComms.sendMessage("ThermalExpansion", "SmelterRecipe", toSend)
    }

    fun blastFurnace(input: ItemStack, output: ItemStack, time: Int) =
            addSmelterRecipe(10000, input, ItemStack.EMPTY, output)

    override fun onInit() {
        super.onInit()

        Recipes.crushers += this::crusher
        Recipes.sawmills += this::sawmill
    }
}
