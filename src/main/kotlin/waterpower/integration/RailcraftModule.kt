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
import waterpower.util.getCount
import waterpower.util.isStackEmpty
import waterpower.util.set


@Integration(IDs.RailCraft)
object RailcraftModule : IModule() {

    @Optional.Method(modid = IDs.RailCraft)
    fun rollingMachine(output: ItemStack, vararg args: Any?): Boolean {
        for (o in args)
            if (o == null)
                return false
        if (isStackEmpty(output))
            return false
        //return RailcraftCraftingManager.rollingMachine.addRecipe(output, args)
        return false
    }

    @Optional.Method(modid = IDs.RailCraft)
    fun blastFurnace(input: ItemStack, output: ItemStack, time: Int)
            = blastFurnace(input, true, false, time, output)

    @Optional.Method(modid = IDs.RailCraft)
    fun blastFurnace(input: ItemStack, matchDamage: Boolean, matchNBT: Boolean, cookTime: Int, output: ItemStack): Boolean {
        if (isStackEmpty(input) || isStackEmpty(output))
            return false
        //RailcraftCraftingManager.blastFurnace.addRecipe(input, matchDamage, matchNBT, cookTime, output)
        return false
    }

    @Optional.Method(modid = IDs.RailCraft)
    fun crusher(input: ItemStack, output: ItemStack)
            = crusher(input, true, false, output)

    @Optional.Method(modid = IDs.RailCraft)
    fun crusher(input: ItemStack, matchDamage: Boolean, matchNBT: Boolean, vararg output: ItemStack): Boolean {
        if (isStackEmpty(input))
            return false
        val data = NBTTagCompound()
        val `in` = NBTTagCompound()
        input.writeToNBT(`in`)
        data.setTag("input", `in`)
        data.setBoolean("matchMeta", matchDamage)
        data.setBoolean("matchNBT", matchNBT)
        for (i in output.indices) {
            val out = NBTTagCompound()
            output[i].writeToNBT(out)
            out.setFloat("chance", 1f)
            data.setTag("output" + i, out)
        }

        return FMLInterModComms.sendMessage(IDs.RailCraft, "rock-crusher", data)
    }

    @Optional.Method(modid = IDs.RailCraft)
    fun bender(i: ItemStack, output: ItemStack): Boolean {
        val input = i.copy()
        input.set(1)
        return when (getCount(i)) {
            1 -> rollingMachine(output, "A", 'A', input)
            2 -> rollingMachine(output, "AA", 'A', input)
            3 -> rollingMachine(output, "AAA", 'A', input)
            4 -> rollingMachine(output, "AA", "AA", 'A', input)
            5 -> rollingMachine(output, "AAA", "AA ", 'A', input)
            6 -> rollingMachine(output, "AAA", "AAA", 'A', input)
            7 -> rollingMachine(output, "AAA", "AAA", "A  ", 'A', input)
            8 -> rollingMachine(output, "AAA", "AAA", "AA ", 'A', input)
            9 -> rollingMachine(output, "AAA", "AAA", "AAA", 'A', input)
            else -> false
        }
    }

    override fun onInit() {
        super.onInit()
        Recipes.benders += this::bender
        Recipes.blastFurnaces += this::blastFurnace
        Recipes.crushers += this::crusher
    }
}