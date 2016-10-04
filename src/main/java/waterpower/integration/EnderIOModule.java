/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import waterpower.Reference;
import waterpower.common.recipe.IMyRecipeInput;
import waterpower.util.Mods;

public class EnderIOModule {
    public static void alloySmelter(String name, ItemStack o, IMyRecipeInput... i) {
        String list = "";
        for (IMyRecipeInput x : i)
            list += x.getEnderIOXML();
        String value = String.format("<recipeGroup name=\"WaterPower\">" + "<recipe name=\"%s\" energyCost=\"10000\">" + "<input>" + list + "</input>"
                + "<output>" + "<itemStack modID=\"%s\" itemName=\"%s\" itemMeta=\"%s\" exp=\"1\" number=\"%d\" />" + "</output>" + "</recipe>"
                + "</recipeGroup>", name, Reference.ModID, o.getItem().delegate.name().getResourcePath(), o.getItemDamage(), o.stackSize);
        FMLInterModComms.sendMessage(Mods.EnderIO.id, "recipe:alloysmelter", value);

    }
}
