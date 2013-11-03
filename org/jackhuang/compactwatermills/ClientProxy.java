package org.jackhuang.compactwatermills;

import org.jackhuang.compactwatermills.watermills.ClientGUIWatermill;
import org.jackhuang.compactwatermills.watermills.TileEntityWatermill;

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
		if (tileEntity != null && tileEntity instanceof TileEntityWatermill) {
			TileEntityWatermill tileEntityCW = (TileEntityWatermill) tileEntity;
			return ClientGUIWatermill.makeGUI(tileEntityCW.getType(), thePlayer.inventory,
				tileEntityCW);
		}
		else {
			return null;
		}
	}

}
