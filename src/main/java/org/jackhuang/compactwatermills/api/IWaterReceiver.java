package org.jackhuang.compactwatermills.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public interface IWaterReceiver {
	public int canProvideWater(int canProvide, ForgeDirection side, TileEntity provider);
	public void provideWater(int provide);
}
