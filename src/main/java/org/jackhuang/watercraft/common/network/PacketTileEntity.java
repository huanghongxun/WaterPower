package org.jackhuang.watercraft.common.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.common.tileentity.TileEntityBase;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketTileEntity
	implements IMessage, IMessageHandler<PacketTileEntity, IMessage> {

    public int x, y, z;
    public TileEntityBase base;
    public NBTTagCompound tag;

    public PacketTileEntity() {
    }

    public PacketTileEntity(TileEntityBase entity) {
	this();
	x = entity.xCoord;
	y = entity.yCoord;
	z = entity.zCoord;
	base = entity;
    }

    @Override
    public IMessage onMessage(PacketTileEntity message, MessageContext ctx) {
	World world = WaterPower.getWorld();
	TileEntity te = world.getTileEntity(message.x, message.y, message.z);
	if (te instanceof TileEntityBase) {
	    message.base = (TileEntityBase) te;
	} else {
	    message.base = null;
	}
	if (message.base == null) {
	    return null;
	}

	message.base.readPacketData(message.tag);

	return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
	x = buf.readInt();
	y = buf.readInt();
	z = buf.readInt();

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
	buf.writeInt(x);
	buf.writeInt(y);
	buf.writeInt(z);
	tag = new NBTTagCompound();
	base.writePacketData(tag);
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
