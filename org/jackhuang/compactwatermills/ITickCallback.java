package org.jackhuang.compactwatermills;

import net.minecraft.world.World;

public abstract interface ITickCallback {
	public abstract void tickCallback(World paramWorld);
}