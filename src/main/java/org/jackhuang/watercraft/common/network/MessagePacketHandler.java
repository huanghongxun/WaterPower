package org.jackhuang.watercraft.common.network;

import org.jackhuang.watercraft.Reference;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class MessagePacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ModName.toLowerCase());

    public static void init() {
        int idx = 0;

        INSTANCE.registerMessage(PacketUnitChanged.class, PacketUnitChanged.class, idx++, Side.SERVER);
        INSTANCE.registerMessage(PacketTileEntity.class, PacketTileEntity.class, idx++, Side.CLIENT);
    }

}
