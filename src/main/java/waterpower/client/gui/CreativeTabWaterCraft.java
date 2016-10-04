/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.client.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import waterpower.common.block.GlobalBlocks;

public class CreativeTabWaterCraft extends CreativeTabs {

    public CreativeTabWaterCraft(String name) {
        super(name);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(GlobalBlocks.waterMill);
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(GlobalBlocks.waterMill);
    }

}
