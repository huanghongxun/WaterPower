/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.api

import net.minecraft.nbt.NBTBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.CapabilityManager

object WaterAPI {
    @CapabilityInject(IWaterReceiver::class)
    var CAPABILITY_WATER_RECEIVER: Capability<IWaterReceiver>? = null

    init {
        CapabilityManager.INSTANCE.register(IWaterReceiver::class.java, VoidStorage(), { TODO("do it yourself") })
        assert(CAPABILITY_WATER_RECEIVER != null)
    }

    class VoidStorage<T> : Capability.IStorage<T> {
        override fun writeNBT(capability: Capability<T>?, instance: T, side: EnumFacing?): NBTBase? {
            TODO("not implemented")
        }

        override fun readNBT(capability: Capability<T>?, instance: T, side: EnumFacing?, nbt: NBTBase?) {
            TODO("not implemented")
        }

    }
}
