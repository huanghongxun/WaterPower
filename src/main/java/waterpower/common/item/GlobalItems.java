/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.common.item;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.Item;
import waterpower.common.block.ore.ItemOreDust;
import waterpower.common.item.crafting.ItemCrafting;
import waterpower.common.item.crafting.ItemMaterial;
import waterpower.common.item.other.ItemOthers;
import waterpower.common.item.other.ItemTrouser;
import waterpower.common.item.range.ItemPlugins;
import waterpower.common.item.range.ItemRange;

/**
 * @author hyh
 */
public class GlobalItems {
	public static final List<Item> items = new LinkedList<>();
    public static ItemOthers updater;
    public static ItemRange range;
    public static ItemOreDust oreDust;
    public static ItemMaterial meterial;
    public static ItemCrafting crafting;
    public static ItemTrouser trousers;
    public static ItemPlugins plugins;
}
