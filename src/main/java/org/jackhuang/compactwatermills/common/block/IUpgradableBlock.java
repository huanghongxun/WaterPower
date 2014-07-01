package org.jackhuang.compactwatermills.common.block;

import net.minecraft.item.ItemStack;

public interface IUpgradableBlock {
	double getEnergy();

	boolean useEnergy(double paramDouble);

	int getOutputSize();

	ItemStack getOutput(int paramInt);

	void setOutput(int paramInt, ItemStack paramItemStack);
}
