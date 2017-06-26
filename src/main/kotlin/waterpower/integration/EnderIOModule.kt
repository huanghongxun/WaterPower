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
import net.minecraftforge.fml.common.event.FMLInterModComms
import waterpower.WaterPower
import waterpower.annotations.Integration
import waterpower.common.recipe.IRecipeInput
import waterpower.common.recipe.RecipeInputItemStack
import waterpower.common.recipe.RecipeInputOreDictionary

@Integration(IDs.EnderIO)
object EnderIOModule : IModule() {

    @Optional.Method(modid = IDs.EnderIO)
    fun alloySmelter(name: String, o: ItemStack, vararg i: IRecipeInput): Boolean {
        var list = ""
        for (x in i)
            if (x is RecipeInputItemStack)
                list += getItemStackXML(x)
            else if (x is RecipeInputOreDictionary)
                list += getOreDictXML(x)
            else WaterPower.logger.warn("Unknown recipe input: ${x}, please contact with the mod author")
        val value = String.format("<recipeGroup name=\"WaterPower\">" + "<recipe name=\"%s\" energyCost=\"10000\">" + "<input>" + list + "</input>"
                + "<output>" + "<itemStack modID=\"%s\" itemName=\"%s\" itemMeta=\"%s\" exp=\"1\" number=\"%d\" />" + "</output>" + "</recipe>"
                + "</recipeGroup>", name, WaterPower.MOD_ID, o.item.delegate.name().resourcePath, o.itemDamage, o.count)
        return FMLInterModComms.sendMessage(IDs.EnderIO, "recipe:alloysmelter", value)
    }

    fun getItemStackXML(recipe: RecipeInputItemStack) =
            "<itemStack modID=\"${WaterPower.MOD_ID}\" itemName=\"${recipe.input.item.delegate.name()}\" itemMeta=\"${recipe.input.itemDamage}\""

    fun getOreDictXML(recipe: RecipeInputOreDictionary) =
            "<itemStack oreDictionary=\"${recipe.ore}\" />"
}