package org.jackhuang.compactwatermills.common.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityBase;

import thaumcraft.api.ThaumcraftApi.EntityTags;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class CompactWatermillsTileEntityPacket extends CompactWatermillsPacket {
	protected String channel = Reference.ModChannel;
	
	public int x, y, z;
	public TileEntityBase base;
	public NBTTagCompound tag;
	
	public CompactWatermillsTileEntityPacket() {}
	
	public CompactWatermillsTileEntityPacket(TileEntityBase entity) {
		this();
		x = entity.xCoord;
		y = entity.yCoord;
		z = entity.zCoord;
		base = entity;
	}
	
	@Override
	public void writeData(DataOutputStream data) throws IOException {
		data.writeInt(x);
		data.writeInt(y);
		data.writeInt(z);
		tag = new NBTTagCompound();
		base.writePacketData(tag);
		byte[] b = CompressedStreamTools.compress(tag);
		data.writeInt(b.length);
		data.write(b);
	}
	
	@Override
	public void readData(DataInputStream data) throws IOException {
		x = data.readInt();
		y = data.readInt();
		z = data.readInt();
		
		World world = CompactWatermills.getWorld();
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileEntityBase)
			base = (TileEntityBase) te;
		else
			base = null;
		if(base == null) return;
		
		int size = data.readInt();
		
		byte[] b = new byte[size];
		data.read(b);
		tag = CompressedStreamTools.decompress(b);
		
		base.readPacketData(tag);
	}
	
	@Override
	public int getID() {
		return PacketType.TILE_ENTITY.ordinal();
	}
}
