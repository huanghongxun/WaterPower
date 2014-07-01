package org.jackhuang.compactwatermills.common.tileentity;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;

public abstract class TileEntityMetaMultiBlock extends TileEntityMultiBlock implements ITileEntityMetaBlock {

	public TileEntityMetaMultiBlock(int tankSize) {
		super(tankSize);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		initNBT(nbt, -1);
	}

}
