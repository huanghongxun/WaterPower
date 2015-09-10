/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityMetaMultiBlock extends TileEntityMultiBlock implements ITileEntityMeta {

    public TileEntityMetaMultiBlock(int tankSize) {
        super(tankSize);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        initNBT(nbt, -1);
    }

}
