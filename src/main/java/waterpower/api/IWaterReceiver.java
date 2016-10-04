/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public interface IWaterReceiver {
    public int canProvideWater(int canProvide, EnumFacing side, TileEntity provider);

    public void provideWater(int provide);
}
