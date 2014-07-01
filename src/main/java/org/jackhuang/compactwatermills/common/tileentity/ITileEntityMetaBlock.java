package org.jackhuang.compactwatermills.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public interface ITileEntityMetaBlock {
	void initNBT(NBTTagCompound nbt, int meta);
}
