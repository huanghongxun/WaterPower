package waterpower.common.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import waterpower.WaterPower;
import waterpower.common.block.tileentity.TileEntityBase;

public class PacketTileEntity implements IMessage, IMessageHandler<PacketTileEntity, IMessage> {

    public long pos;
    public TileEntityBase base;
    public NBTTagCompound tag;

    public PacketTileEntity() {
    }

    public PacketTileEntity(TileEntityBase entity) {
        this();
        pos = entity.getPos().toLong();
        base = entity;
    }

    @Override
    public IMessage onMessage(PacketTileEntity message, MessageContext ctx) {
        World world = WaterPower.getWorld();
        TileEntity te = world.getTileEntity(BlockPos.fromLong(message.pos));
        if (te instanceof TileEntityBase)
            message.base = (TileEntityBase) te;
        else
            message.base = null;
        if (message.base == null)
            return null;

        message.base.readPacketData(message.tag);

        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = buf.readLong();

        int len = buf.readInt();
        byte[] data = new byte[len];
        buf.readBytes(data);
        try {
            tag = CompressedStreamTools.read(new DataInputStream(new ByteArrayInputStream(data)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos);
        if (tag == null) {
            tag = new NBTTagCompound();
            base.writePacketData(tag);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            CompressedStreamTools.write(tag, dos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] data = baos.toByteArray();
        buf.writeInt(data.length);
        buf.writeBytes(data);
    }

}
