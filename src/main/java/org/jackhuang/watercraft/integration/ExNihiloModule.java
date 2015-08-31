/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.integration;

import net.minecraft.init.Blocks;

import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.block.ore.OreType;
import org.jackhuang.watercraft.common.item.GlobalItems;
import org.jackhuang.watercraft.common.item.crafting.ItemMaterial;
import org.jackhuang.watercraft.common.item.crafting.MaterialForms;

import exnihilo.registries.HammerRegistry;
import exnihilo.registries.SieveRegistry;

public class ExNihiloModule extends BaseModule {

    @Override
    public void postInit() {
        for (OreType o : OreType.values()) {
            SieveRegistry.register(Blocks.gravel, 0, GlobalItems.meterial,
                    ItemMaterial.get(o.t, MaterialForms.dustSmall)
                            .getItemDamage(), 48);
            SieveRegistry.register(Blocks.sand, 0, GlobalItems.oreDust,
                    o.ordinal(), 48);
            HammerRegistry.register(GlobalBlocks.ore, o.ordinal(),
                    GlobalItems.meterial,
                    ItemMaterial.get(o.t, MaterialForms.dust).getItemDamage(), 1, 0);
            HammerRegistry.register(GlobalBlocks.ore, o.ordinal(),
                    GlobalItems.meterial,
                    ItemMaterial.get(o.t, MaterialForms.dust).getItemDamage(), 1, 0);
        }
    }

}
