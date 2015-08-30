package org.jackhuang.watercraft.integration;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.recipe.IMyRecipeInput;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.item.ItemStack;

public class EnderIOModule {
    public static void alloySmelter(String name, ItemStack o,
            IMyRecipeInput... i) {
        String list = "";
        for (IMyRecipeInput x : i)
            list += x.getEnderIOXML();
        String value = String
                .format("<recipeGroup name=\"WaterPower\">"
                        + "<recipe name=\"%s\" energyCost=\"10000\">"
                        + "<input>"
                        + list
                        + "</input>"
                        + "<output>"
                        + "<itemStack modID=\"%s\" itemName=\"%s\" itemMeta=\"%s\" exp=\"1\" number=\"%d\" />"
                        + "</output>" + "</recipe>" + "</recipeGroup>", name,
                        Reference.ModID, o.getItem().delegate.name().split(":")[1],
                        o.getItemDamage(), o.stackSize);
        FMLInterModComms.sendMessage(Mods.EnderIO.id,
                "recipe:alloysmelter", value);

    }
}
