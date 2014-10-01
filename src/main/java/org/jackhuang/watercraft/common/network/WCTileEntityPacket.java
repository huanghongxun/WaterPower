/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.tileentity.TileEntityBase;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class WCTileEntityPacket extends WCPacket {
	protected String channel = Reference.ModChannel;
	
	public int x, y, z;
	public TileEntityBase base;
	public NBTTagCompound tag;
	
	public WCTileEntityPacket() {}
	
	public WCTileEntityPacket(TileEntityBase entity) {
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
		CompressedStreamTools.write(tag, data);
	}
	
	@Override
	public void readData(DataInputStream data) throws IOException {
		x = data.readInt();
		y = data.readInt();
		z = data.readInt();
		
		World world = WaterPower.getWorld();
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileEntityBase)
			base = (TileEntityBase) te;
		else
			base = null;
		if(base == null) return;
		
		tag = CompressedStreamTools.read(data);
		
		base.readPacketData(tag);
	}
	
	@Override
	public int getID() {
		return PacketType.TILE_ENTITY.ordinal();
	}
}

