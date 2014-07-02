/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.network;

import io.netty.buffer.Unpooled;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.jackhuang.watercraft.Reference;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCPacket {
	protected boolean isChunkDataPacket = false;
	protected String channel = Reference.ModChannel;

	public int id;
	public String[] dataString;
	public int[] dataInt;

	public WCPacket() {

	}

	public WCPacket(int id, String[] data, int[] dataInt) {
		this.id = id;
		this.dataString = data;
		this.dataInt = dataInt;
	}

	public FMLProxyPacket getPacket() {

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);
		try {
			data.writeByte(getID());
			writeData(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new FMLProxyPacket(Unpooled.wrappedBuffer(bytes.toByteArray()), Reference.ModChannel);
	}

	@SideOnly(Side.CLIENT)
	public void readData(DataInputStream data) throws IOException {
		id = data.readInt();
		byte len;
		int i;
		len = data.readByte();
		if (len == 0)
			dataString = null;
		else {
			dataString = new String[len];
			for (i = 0; i < len; i++)
				dataString[i] = data.readUTF();
		}
		len = data.readByte();
		if (len == 0)
			dataInt = null;
		else {
			dataInt = new int[len];
			for (i = 0; i < len; i++)
				dataInt[i] = data.readInt();
		}
	}

	public void writeData(DataOutputStream data) throws IOException {
		data.writeInt(id);
		if (this.dataString != null) {
			data.writeByte(this.dataString.length);
			for (String string : dataString)
				data.writeUTF(string);
		} else
			data.writeByte(0);
		if (this.dataInt != null) {
			data.writeByte(this.dataInt.length);
			for (int string : dataInt)
				data.writeInt(string);
		} else
			data.writeByte(0);
	}

	public static WCPacket parse(byte[] bytes)
			throws IOException, InstantiationException, IllegalAccessException {
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(
				bytes));
		int id = data.readByte();
		WCPacket pkt = PacketType.createPacket(id);
		pkt.readData(data);
		return pkt;
	}
	
	public int getID() {
		return 0;
	}
	
	public enum PacketType {
		DEFAULT(WCPacket.class),
		TILE_ENTITY(WCPacket.class);
		
		Class<? extends WCPacket> clazz;
		
		private PacketType(Class<? extends WCPacket> cls) {
			clazz = cls;
		}
		
		public static WCPacket createPacket(int id) throws InstantiationException, IllegalAccessException {
			return PacketType.values()[id].clazz.newInstance();
		}
	}
}
