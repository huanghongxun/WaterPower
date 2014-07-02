/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public interface ITileEntityMeta {
	void initNBT(NBTTagCompound nbt, int meta);
}
