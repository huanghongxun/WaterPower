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
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.common.event.FMLInterModComms
import waterpower.annotations.Integration
import waterpower.common.recipe.Recipes

@Integration(IDs.Mekanism)
object MekanismModule : IModule() {

    @Optional.Method(modid = IDs.Mekanism)
    fun blastFurnace(input: ItemStack, output: ItemStack, time: Int)
            = metallurgicInfuser("CARBON", Math.round(time / 100.0f), input, output)

    @Optional.Method(modid = IDs.Mekanism)
    fun metallurgicInfuser(infuse: String, amount: Int, input: ItemStack, output: ItemStack): Boolean {
        val sendTag = convertToSimpleRecipe(input, output)
        sendTag.setString("infuseType", infuse)
        sendTag.setInteger("infuseAmount", amount)

        return FMLInterModComms.sendMessage(IDs.Mekanism, "MetallurgicInfuserRecipe", sendTag)
    }

    @Optional.Method(modid = IDs.Mekanism)
    fun crusher(stack: ItemStack, output: ItemStack): Boolean {
        val sendTag = convertToSimpleRecipe(stack, output)

        return FMLInterModComms.sendMessage(IDs.Mekanism, "CrusherRecipe", sendTag)
    }

    private fun convertToSimpleRecipe(input: ItemStack, out: ItemStack): NBTTagCompound {
        val sendTag = NBTTagCompound()
        val inputTagDummy = NBTTagCompound()
        val outputTagDummy = NBTTagCompound()

        val inputTag = input.writeToNBT(inputTagDummy)
        val outputTag = out.writeToNBT(outputTagDummy)

        sendTag.setTag("input", inputTag)
        sendTag.setTag("output", outputTag)

        return sendTag
    }

    override fun onPreInit() {
        super.onPreInit()

        Recipes.crushers += this::crusher
        Recipes.blastFurnaces += this::blastFurnace
    }
}