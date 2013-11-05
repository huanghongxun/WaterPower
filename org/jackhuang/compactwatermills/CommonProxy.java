package org.jackhuang.compactwatermills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer thePlayer, World world, int x, int y,
		int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		/*if (tileEntity != null && tileEntity instanceof TileEntityWatermill) {
			TileEntityWatermill tileEntityCW = (TileEntityWatermill) tileEntity;
			return new ContainerCompactWatermills(thePlayer.inventory,
				tileEntityCW);
		}
		else {
			return null;
		}*/
		TileEntityBaseGenerator tileEntityCW = (TileEntityBaseGenerator) tileEntity;
		return new ContainerRotor(thePlayer.inventory,
			tileEntityCW);
	}

}
