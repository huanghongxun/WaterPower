package org.jackhuang.watercraft.common.network;

import org.jackhuang.watercraft.common.EnergyType;
import org.jackhuang.watercraft.common.block.tileentity.IUnitChangeable;
import org.jackhuang.watercraft.common.block.tileentity.TileEntityGenerator;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketUnitChanged implements IMessage, IMessageHandler<PacketUnitChanged, IMessage> {
    private int dim, x, y, z, unitId;

    public PacketUnitChanged() {
    }

    public PacketUnitChanged(int dim, int x, int y, int z, int unitId) {
        super();
        this.dim = dim;
        this.x = x;
        this.y = y;
        this.z = z;
        this.unitId = unitId;
    }

    @Override
    public IMessage onMessage(PacketUnitChanged message, MessageContext ctx) {
        World world = DimensionManager.getWorld(message.dim);
        TileEntity te = world.getTileEntity(message.x, message.y, message.z);
        if (te instanceof IUnitChangeable) {
            IUnitChangeable tebg = (IUnitChangeable) te;
            tebg.setUnitId(message.unitId);
        }
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dim = buf.readInt();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        unitId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(unitId);
    }

}
