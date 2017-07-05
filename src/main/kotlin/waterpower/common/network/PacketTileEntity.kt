/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.network

import io.netty.buffer.ByteBuf
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import waterpower.annotations.Message
import waterpower.common.block.tile.TileEntityBase
import waterpower.util.getWorld
import java.io.*

@Message(Side.CLIENT)
class PacketTileEntity() : Packet<PacketTileEntity>() {

    var pos: Long = 0
    var base: TileEntityBase? = null
    var tag: NBTTagCompound? = null

    constructor(entity: TileEntityBase) : this() {
        pos = entity.pos.toLong()
        base = entity
    }

    override fun onMessage(message: PacketTileEntity, ctx: MessageContext): IMessage? {
        val world = getWorld()
        val te = world!!.getTileEntity(BlockPos.fromLong(message.pos))
        if (te is TileEntityBase)
            message.base = te
        else
            message.base = null
        if (message.base == null || message.tag == null)
            return null

        message.base!!.readPacketData(message.tag as NBTTagCompound)

        return null
    }

    override fun fromBytes(buf: ByteBuf) {
        pos = buf.readLong()

        val len = buf.readInt()
        val data = ByteArray(len)
        buf.readBytes(data)
        try {
            tag = CompressedStreamTools.read(DataInputStream(ByteArrayInputStream(data)))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeLong(pos)
        if (tag == null) {
            tag = NBTTagCompound()
            base!!.writePacketData(tag as NBTTagCompound)
        }
        val baos = ByteArrayOutputStream()
        val dos = DataOutputStream(baos)
        try {
            CompressedStreamTools.write(tag, dos)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        val data = baos.toByteArray()
        buf.writeInt(data.size)
        buf.writeBytes(data)
    }

}