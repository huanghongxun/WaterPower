package org.jackhuang.watercraft.common.network;

import java.lang.reflect.Method;

import org.jackhuang.watercraft.util.WCLog;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.world.WorldServer;

public class PacketDispatcher {
	private static final Class playerInstanceClass;
	private static final Method getOrCreateChunkWatcher;
	private static final Method sendToAllPlayersWatchingChunk;

	public static void sendToServer(WCPacket packet) {
		PacketHandler.INSTANCE.channel.sendToServer(packet.getPacket());
	}

	public static void sendToPlayer(WCPacket packet,
			EntityPlayerMP player) {
		PacketHandler.INSTANCE.channel.sendTo(packet.getPacket(), player);
	}

	public static void sendToAll(WCPacket packet) {
		PacketHandler.INSTANCE.channel.sendToAll(packet.getPacket());
	}

	public static void sendToAllAround(WCPacket packet,
			NetworkRegistry.TargetPoint zone) {
		PacketHandler.INSTANCE.channel
				.sendToAllAround(packet.getPacket(), zone);
	}

	public static void sendToDimension(WCPacket packet,
			int dimensionId) {
		PacketHandler.INSTANCE.channel.sendToDimension(packet.getPacket(),
				dimensionId);
	}
	
	public static void mcSendToServer(Packet packet) {
		Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
	}
	
	public static void mcSendToPlayer(Packet packet, int dim) {
		MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayersInDimension(packet, dim);
	}

	public static void sendToWatchers(WCPacket packet,
			WorldServer world, int worldX, int worldZ) {
		try {
			Object playerInstance = getOrCreateChunkWatcher.invoke(
					world.getPlayerManager(),
					new Object[] { Integer.valueOf(worldX >> 4),
							Integer.valueOf(worldZ >> 4),
							Boolean.valueOf(false) });
			if (playerInstance != null)
				sendToAllPlayersWatchingChunk.invoke(playerInstance,
						new Object[] { packet.getPacket() });
		} catch (Exception ex) {
			WCLog
					.err("Reflection Failure in PacketDispatcher.sendToWatchers() 20 "
							+ new Object[] { getOrCreateChunkWatcher.getName(),
									sendToAllPlayersWatchingChunk.getName() });
			throw new RuntimeException(ex);
		}
	}

	static {
		try {
			playerInstanceClass = PlayerManager.class.getDeclaredClasses()[0];
			getOrCreateChunkWatcher = ReflectionHelper.findMethod(
					PlayerManager.class, null, new String[] { "func_72690_a",
							"getOrCreateChunkWatcher" }, new Class[] {
							Integer.TYPE, Integer.TYPE, Boolean.TYPE });
			sendToAllPlayersWatchingChunk = ReflectionHelper.findMethod(
					playerInstanceClass, null, new String[] { "func_151251_a",
							"sendToAllPlayersWatchingChunk" },
					new Class[] { Packet.class });
			getOrCreateChunkWatcher.setAccessible(true);
			sendToAllPlayersWatchingChunk.setAccessible(true);
		} catch (Exception ex) {
			WCLog
					.err("Reflection Failure in PacketDispatcher initalization");
			throw new RuntimeException(ex);
		}
	}
}