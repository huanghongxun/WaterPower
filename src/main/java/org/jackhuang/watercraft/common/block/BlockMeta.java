/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public abstract class BlockMeta extends BlockWaterPower {

	public BlockMeta(String id,
			Material material, Class<? extends ItemBlock> c) {
		super(id, material, c);
	}

	@Override
	protected int maxMetaData() {
		return 1;
	}

}
