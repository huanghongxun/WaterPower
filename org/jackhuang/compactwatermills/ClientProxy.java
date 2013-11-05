package org.jackhuang.compactwatermills;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer thePlayer, World world, int X, int Y,
		int Z) {
		TileEntity tileEntity = world.getBlockTileEntity(X, Y, Z);
		if (tileEntity == null) return null;
		/*if(ID == DefaultGuiIds.get("tileEntityTurbine")) {
			if(tileEntity instanceof TileEntityTurbine) {
				TileEntityTurbine tileEntityT = (TileEntityTurbine) tileEntity;
				return ClientGUIRotor.makeGUI(thePlayer.inventory, tileEntityT);
			}
		} else if(ID == DefaultGuiIds.get("tileEntityWatermill")) {
			TileEntityWatermill tileEntityCW = (TileEntityWatermill) tileEntity;
			return ClientGUIRotor.makeGUI(thePlayer.inventory,
				tileEntityCW);
		}*/
		TileEntityBaseGenerator tileEntityCW = (TileEntityBaseGenerator) tileEntity;
		return ClientGUIRotor.makeGUI(thePlayer.inventory,
			tileEntityCW);
	}

}
