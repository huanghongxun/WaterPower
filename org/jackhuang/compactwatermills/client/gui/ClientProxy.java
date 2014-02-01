package org.jackhuang.compactwatermills.client.gui;

import org.jackhuang.compactwatermills.CommonProxy;
import org.jackhuang.compactwatermills.block.reservoir.TileEntityReservoir;
import org.jackhuang.compactwatermills.block.turbines.TileEntityTurbine;
import org.jackhuang.compactwatermills.block.watermills.TileEntityWatermill;
import org.jackhuang.compactwatermills.client.renderer.EntityWaterWheelRenderer;
import org.jackhuang.compactwatermills.entity.EntityWaterWheel;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import cpw.mods.fml.client.registry.RenderingRegistry;
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
		if(ID == DefaultGuiIds.get("tileEntityTurbine")) {
			if(tileEntity instanceof TileEntityTurbine) {
				TileEntityTurbine tileEntityT = (TileEntityTurbine) tileEntity;
				return ClientGUITurbine.makeGUI(thePlayer, tileEntityT);
			}
		} else if(ID == DefaultGuiIds.get("tileEntityWatermill")) {
			TileEntityWatermill tileEntityCW = (TileEntityWatermill) tileEntity;
			return ClientGUIWatermill.makeGUI(thePlayer, tileEntityCW);
		} else if(ID == DefaultGuiIds.get("tileEntityReservoir")) {
			TileEntityReservoir tileEntityR = (TileEntityReservoir) tileEntity;
			return ClientGUIReservoir.makeGUI(thePlayer, tileEntityR);
		}
		return null;
	}
	
	@Override
	public void registerRenderer() {
		super.registerRenderer();
		
		RenderingRegistry.registerEntityRenderingHandler(EntityWaterWheel.class, new EntityWaterWheelRenderer());
	}

}
