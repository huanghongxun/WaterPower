/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.network

import io.netty.buffer.ByteBuf
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import waterpower.annotations.Message
import waterpower.api.IUnitChangeable
import waterpower.common.Energy

@Message(Side.SERVER)
class PacketUnitChanged() : Packet<PacketUnitChanged>() {
    private var dim: Int = 0
    private var energy: Energy = Energy.EU
    private var pos: Long = 0

    constructor(dim: Int, pos: BlockPos, energy: Energy) : this() {
        this.dim = dim
        this.pos = pos.toLong()
        this.energy = energy
    }

    override fun onMessage(message: PacketUnitChanged, ctx: MessageContext): IMessage? {
        val world = DimensionManager.getWorld(message.dim)
        val te = world.getTileEntity(BlockPos.fromLong(message.pos))
        if (te is IUnitChangeable)
            te.setUnit(message.energy)
        return null
    }

    override fun fromBytes(buf: ByteBuf) {
        dim = buf.readInt()
        pos = buf.readLong()
        energy = Energy.values()[buf.readInt()]
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeInt(dim)
        buf.writeLong(pos)
        buf.writeInt(energy.ordinal)
    }

}
