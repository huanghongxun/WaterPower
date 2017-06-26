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

@Integration(IDs.AppliedEnergistics2)
object AppliedEnergistics2Module : IModule() {

    @Optional.Method(modid = IDs.AppliedEnergistics2)
    fun crusher(input: ItemStack, output: ItemStack): Boolean {
        val tag = NBTTagCompound()

        val inTag = NBTTagCompound()
        input.writeToNBT(inTag)
        tag.setTag("in", inTag)

        val outTag = NBTTagCompound()
        output.writeToNBT(outTag)
        tag.setTag("out", outTag)

        tag.setInteger("turns", 7)
        return FMLInterModComms.sendMessage(IDs.AppliedEnergistics2, "add-grindable", tag)
    }

    override fun onPreInit() {
        Recipes.crushers += this::crusher
    }
}