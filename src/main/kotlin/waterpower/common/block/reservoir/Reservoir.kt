/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.reservoir

import net.minecraft.nbt.NBTTagCompound
import waterpower.util.INBTSerializable
import waterpower.util.getUUID
import waterpower.util.setUUID
import java.util.*

data class Reservoir(var length: Int = 0, var width: Int = 0, var height: Int = 0, var nonAirBlock: Int = 0, var uuid: UUID? = null) : INBTSerializable<Reservoir> {

    val capacity: Int
        get() = (length - 2) * (width - 2) * (height - 1) - nonAirBlock

    override fun serializeNBT(): NBTTagCompound {
        val tag = NBTTagCompound()
        tag.setInteger("length", length)
        tag.setInteger("width", width)
        tag.setInteger("height", height)
        tag.setInteger("nonAirBlock", nonAirBlock)
        tag.setUUID("uuid", uuid)
        return tag
    }

    override fun deserializeNBT(tag: NBTTagCompound) {
        length = tag.getInteger("length")
        width = tag.getInteger("width")
        height = tag.getInteger("height")
        nonAirBlock = tag.getInteger("nonAirBlock")
        uuid = tag.getUUID("uuid")
    }

    override fun clone(): Any {
        val r = Reservoir()
        r.deserializeNBT(serializeNBT())
        return r
    }

    fun setInvalid() {
        uuid = null
        length = -1
        width = -1
        height = -1
    }
}