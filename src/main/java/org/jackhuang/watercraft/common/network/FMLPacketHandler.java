package org.jackhuang.watercraft.common.network;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.EnergyType;
import org.jackhuang.watercraft.common.tileentity.TileEntityBaseGenerator;
import org.jackhuang.watercraft.util.WCLog;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FMLPacketHandler {
	
	public static final FMLPacketHandler INSTANCE = new FMLPacketHandler();
	final FMLEventChannel channel;
	
	private FMLPacketHandler() {
	    this.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(Reference.ModChannel);
	    this.channel.register(this);
	}

	@SubscribeEvent
	public void onPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
		byte[] data = new byte[event.packet.payload().readableBytes()];
	    event.packet.payload().readBytes(data);
		try {
			WCPacket packet = WCPacket.parse(data);
			onPacketData(packet, ((NetHandlerPlayServer)event.handler).playerEntity);
		} catch (Exception e) {
		}
	}
	
	@SubscribeEvent
	public void onPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
		byte[] data = new byte[event.packet.payload().readableBytes()];
	    event.packet.payload().readBytes(data);
		try {
			WCPacket packet = WCPacket.parse(data);
			onPacketData(packet, null);
		} catch (Exception e) {
			WCLog.err(e.toString());
		}
	}
	
	public void onPacketData(WCPacket packet,
			EntityPlayerMP playerMP) {
		switch(packet.id) {
		case 1: // EnergyType changed event.
			TileEntity te = playerMP.worldObj.getTileEntity(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]);
			if(te instanceof TileEntityBaseGenerator) {
				TileEntityBaseGenerator te2 = (TileEntityBaseGenerator) te;
				te2.energyType = EnergyType.values()[packet.dataInt[3]];
			}
			break;
		}
	}

}
