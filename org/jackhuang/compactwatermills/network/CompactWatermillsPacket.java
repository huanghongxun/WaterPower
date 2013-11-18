package org.jackhuang.compactwatermills.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.jackhuang.compactwatermills.Reference;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

public class CompactWatermillsPacket {
	protected boolean isChunkDataPacket = false;
	protected String channel = Reference.ModChannel;
	
	public int id;
	public String[] dataString;
	public int[] dataInt;
	
	public CompactWatermillsPacket() {
		
	}
	
	public CompactWatermillsPacket(int id, String[] data, int[] dataInt) {
		this.id = id;
		this.dataString = data;
		this.dataInt = dataInt;
	}
	

	public Packet getPacket() {

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);
		try {
			writeData(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = channel;
		packet.data = bytes.toByteArray();
		packet.length = packet.data.length;
		//packet.isChunkDataPacket = this.isChunkDataPacket;
		return packet;
	}

	public void readData(DataInputStream data) throws IOException {
		id = data.readInt();
		byte len; int i;
		len = data.readByte();
		dataString = new String[len];
		for(i = 0; i < len; i++)
			dataString[i] = data.readUTF(); 
		len = data.readByte();
		dataInt = new int[len];
		for(i = 0; i < len; i++)
			dataInt[i] = data.readInt(); 
	}

	public void writeData(DataOutputStream data) throws IOException {
		data.writeInt(id);
		if(this.dataString != null)
			data.writeByte(this.dataString.length);
		else
			data.writeByte(0);
		for(String string : dataString)
			data.writeUTF(string);
		if(this.dataInt != null)
			data.writeByte(this.dataInt.length);
		else
			data.writeByte(0);
		for(int string : dataInt)
			data.writeInt(string);
	}
	
	public static CompactWatermillsPacket parse(byte[] bytes) throws IOException {
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(bytes));
		CompactWatermillsPacket pkt = new CompactWatermillsPacket();
		pkt.readData(data);
		return pkt;
	}
}
