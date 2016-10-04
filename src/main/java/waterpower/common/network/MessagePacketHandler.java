package waterpower.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import waterpower.Reference;

public class MessagePacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ModName.toLowerCase());

    private MessagePacketHandler() {
    }

    public static void init() {
        int idx = 0;

        INSTANCE.registerMessage(PacketUnitChanged.class, PacketUnitChanged.class, idx++, Side.SERVER);
        INSTANCE.registerMessage(PacketTileEntity.class, PacketTileEntity.class, idx++, Side.CLIENT);
    }

}
