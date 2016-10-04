/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.common.block;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class GlobalBlocks {
	public static final List<BlockWaterPower> blocks = new LinkedList<>();
    public static BlockWaterPower waterMill, turbine, reservoir, ore, machine, material;

    public static ItemStack macerator, compressor, centrifuge, advancedCompressor, sawmill, lathe, cutter, waterPump, advancedWaterPump;

    public static ItemStack monaziteOre, vanadiumOre, manganeseOre, magnetOre, zincOre;
}
