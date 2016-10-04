package waterpower.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import waterpower.common.block.tileentity.IUnitChangeable;

public class PacketUnitChanged implements IMessage, IMessageHandler<PacketUnitChanged, IMessage> {
    private int dim, unitId;
    private long pos;

    public PacketUnitChanged() {
    }

    public PacketUnitChanged(int dim, BlockPos pos, int unitId) {
        super();
        this.dim = dim;
        this.pos = pos.toLong();
        this.unitId = unitId;
    }

    @Override
    public IMessage onMessage(PacketUnitChanged message, MessageContext ctx) {
        World world = DimensionManager.getWorld(message.dim);
        TileEntity te = world.getTileEntity(BlockPos.fromLong(message.pos));
        if (te instanceof IUnitChangeable) {
            IUnitChangeable tebg = (IUnitChangeable) te;
            tebg.setUnitId(message.unitId);
        }
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dim = buf.readInt();
        pos = buf.readLong();
        unitId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeLong(pos);
        buf.writeInt(unitId);
    }

}
