package org.jackhuang.watercraft.common.network;

import org.jackhuang.watercraft.common.block.tileentity.IUnitChangeable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {

		try {
			WaterPowerPacket pkt = WaterPowerPacket.parse(packet.data);
			if(player instanceof EntityPlayerMP) {
				handlePacketFromClient(pkt, (EntityPlayerMP)player);
			} else {
				handlePacketFromServer(pkt);
			}
		} catch (Exception e) {
			
		}
	}
	
	public void handlePacketFromClient(WaterPowerPacket packet,
			EntityPlayerMP playerMP) {
		switch (packet.id) {
		case 0: //unitChanged
			TileEntity tileEntity = DimensionManager.getWorld(packet.dataInt[0]).
				getBlockTileEntity(packet.dataInt[1], packet.dataInt[2], packet.dataInt[3]);
			if(tileEntity != null && tileEntity instanceof IUnitChangeable) {
				IUnitChangeable te = (IUnitChangeable) tileEntity;
				te.setUnitId(packet.dataInt[4]);
			}
			break;

		default:
			break;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void handlePacketFromServer(WaterPowerPacket packet) {
	}

}