package org.jackhuang.compactwatermills.common.network;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityMultiBlock;
import org.jackhuang.compactwatermills.helpers.LogHelper;

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

public class PacketHandler {
	
	public static final PacketHandler INSTANCE = new PacketHandler();
	final FMLEventChannel channel;
	
	private PacketHandler() {
	    this.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(Reference.ModChannel);
	    this.channel.register(this);
	}

	@SubscribeEvent
	public void onPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
		byte[] data = new byte[event.packet.payload().readableBytes()];
	    event.packet.payload().readBytes(data);
		try {
			CompactWatermillsPacket packet = CompactWatermillsPacket.parse(data);
			onPacketData(packet, ((NetHandlerPlayServer)event.handler).playerEntity);
		} catch (Exception e) {
		}
	}
	
	@SubscribeEvent
	public void onPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
		byte[] data = new byte[event.packet.payload().readableBytes()];
	    event.packet.payload().readBytes(data);
		try {
			CompactWatermillsPacket packet = CompactWatermillsPacket.parse(data);
			onPacketData(packet, null);
		} catch (Exception e) {
			LogHelper.err(e.toString());
		}
	}
	
	public void onPacketData(CompactWatermillsPacket packet,
			EntityPlayerMP playerMP) {
	}

}
