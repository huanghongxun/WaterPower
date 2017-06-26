/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.util

import net.minecraft.nbt.NBTTagCompound

interface INBTSerializable<T : INBTSerializable<T>> : Cloneable {
    fun serializeNBT(): NBTTagCompound
    fun deserializeNBT(tag: NBTTagCompound)
    public override fun clone(): Any
}