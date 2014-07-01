package org.jackhuang.compactwatermills.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityElectricMetaBlock extends TileEntityBaseGenerator implements ITileEntityMetaBlock {

	private short meta;
	
	public TileEntityElectricMetaBlock(int i, int j) {
		super(i, j);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		initNBT(nbt, -1);
		//worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}
}
