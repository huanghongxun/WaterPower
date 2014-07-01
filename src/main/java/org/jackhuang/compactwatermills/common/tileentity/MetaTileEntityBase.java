package org.jackhuang.compactwatermills.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public abstract class MetaTileEntityBase {
	
	public TileEntityElectricMetaBlock baseTileEntity;
	
	/**
	 * call per tick
	 */
	public void tick(){}
	
	/**
	 * call per second
	 */
	public void second(){}
	
	/**
	 * call per tick on the server side.
	 */
	public void tickServer(){}
	
	public void readFromNBT(NBTTagCompound nbt){}
	
	public void writeToNBT(NBTTagCompound nbt){}

}
