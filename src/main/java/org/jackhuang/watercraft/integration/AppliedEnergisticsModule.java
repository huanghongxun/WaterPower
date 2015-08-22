package org.jackhuang.watercraft.integration;

import net.minecraft.item.ItemStack;

public class AppliedEnergisticsModule extends BaseModule {

    public static void crusher(ItemStack in, ItemStack out) {
        appeng.api.AEApi.instance().registries().grinder().addRecipe(in, out, 7);
    }
}
