/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.attachment

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability

abstract class TileEntityAttachment(val master: waterpower.common.block.tile.TileEntityBase) {
    abstract fun getName(): String
    open fun readFromNBT(tag: NBTTagCompound) {}
    open fun writeToNBT(): NBTTagCompound? = null
    open fun onLoaded() {}
    open fun onUnloaded() {}
    open fun onTick() {}
    open fun onUpdate() {}
    open fun <T> getCapability(capability: Capability<T>, side: EnumFacing?): T? = null
    open fun getCapabilities(side: EnumFacing?): Collection<Capability<*>> = java.util.Collections.emptySet()
}