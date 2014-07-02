package org.jackhuang.watercraft.client;

import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds.Data;
import org.jackhuang.watercraft.client.render.EntityWaterWheelRenderer;
import org.jackhuang.watercraft.client.render.RecolorableItemRenderer;
import org.jackhuang.watercraft.common.CommonProxy;
import org.jackhuang.watercraft.common.block.machines.GuiCentrifuge;
import org.jackhuang.watercraft.common.block.machines.GuiCompressor;
import org.jackhuang.watercraft.common.block.machines.GuiCutter;
import org.jackhuang.watercraft.common.block.machines.GuiLathe;
import org.jackhuang.watercraft.common.block.machines.GuiMacerator;
import org.jackhuang.watercraft.common.block.machines.GuiSawmill;
import org.jackhuang.watercraft.common.block.machines.TileEntityAdvancedCompressor;
import org.jackhuang.watercraft.common.block.machines.TileEntityCentrifuge;
import org.jackhuang.watercraft.common.block.machines.TileEntityCompressor;
import org.jackhuang.watercraft.common.block.machines.TileEntityCutter;
import org.jackhuang.watercraft.common.block.machines.TileEntityLathe;
import org.jackhuang.watercraft.common.block.machines.TileEntityMacerator;
import org.jackhuang.watercraft.common.block.machines.TileEntitySawmill;
import org.jackhuang.watercraft.common.block.reservoir.GuiReservoir;
import org.jackhuang.watercraft.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.watercraft.common.block.turbines.GuiTurbine;
import org.jackhuang.watercraft.common.block.turbines.TileEntityTurbine;
import org.jackhuang.watercraft.common.block.watermills.GuiWatermill;
import org.jackhuang.watercraft.common.block.watermills.TileEntityWatermill;
import org.jackhuang.watercraft.common.entity.EntityWaterWheel;
import org.jackhuang.watercraft.common.item.GlobalItems;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer thePlayer, World world, int X, int Y,
		int Z) {
		TileEntity tileEntity = world.getTileEntity(X, Y, Z);
		Data data;
		if (tileEntity == null) return null;
		if(ID == (data = DefaultGuiIds.get("tileEntityTurbine")).id) {
			TileEntityTurbine tileEntityT = (TileEntityTurbine) tileEntity;
			return new GuiTurbine(thePlayer, tileEntityT);
		} else if(ID == (data = DefaultGuiIds.get("tileEntityWatermill")).id) {
			TileEntityWatermill tileEntityCW = (TileEntityWatermill) tileEntity;
			return new GuiWatermill(thePlayer, tileEntityCW);
		} else if(ID == (data = DefaultGuiIds.get("tileEntityReservoir")).id) {
			TileEntityReservoir tileEntityR = (TileEntityReservoir) tileEntity;
			return new GuiReservoir(thePlayer, tileEntityR);
		} else if(ID == (data = DefaultGuiIds.get("tileEntityMacerator")).id) {
			TileEntityMacerator tileEntityR = (TileEntityMacerator) tileEntity;
			return new GuiMacerator(thePlayer, tileEntityR);
		} else if(ID == (data = DefaultGuiIds.get("tileEntityCompressor")).id) {
			TileEntityCompressor tileEntityR = (TileEntityCompressor) tileEntity;
			return new GuiCompressor(thePlayer, tileEntityR);
		} else if(ID == (data = DefaultGuiIds.get("tileEntitySawmill")).id) {
			TileEntitySawmill tileEntityR = (TileEntitySawmill) tileEntity;
			return new GuiSawmill(thePlayer, tileEntityR);
		} else if(ID == DefaultGuiIds.get("tileEntityLathe").id) {
			TileEntityLathe tileEntityR = (TileEntityLathe) tileEntity;
			return new GuiLathe(thePlayer, tileEntityR);
		} else if(ID == DefaultGuiIds.get("tileEntityCutter").id) {
			TileEntityCutter tileEntityR = (TileEntityCutter) tileEntity;
			return new GuiCutter(thePlayer, tileEntityR);
		} else if(ID == DefaultGuiIds.get("tileEntityAdvancedCompressor").id) {
			TileEntityAdvancedCompressor tileEntityR = (TileEntityAdvancedCompressor) tileEntity;
			return new GuiCompressor(thePlayer, tileEntityR);
		} else if(ID == DefaultGuiIds.get("tileEntityCentrifuge").id) {
			TileEntityCentrifuge tileEntityR = (TileEntityCentrifuge) tileEntity;
			return new GuiCentrifuge(thePlayer, tileEntityR);
		}
		return null;
	}
	
	@Override
	public void registerRenderer() {
		super.registerRenderer();
		
		RenderingRegistry.registerEntityRenderingHandler(EntityWaterWheel.class, new EntityWaterWheelRenderer());
		
		MinecraftForgeClient.registerItemRenderer(GlobalItems.meterial, new RecolorableItemRenderer());
		MinecraftForgeClient.registerItemRenderer(GlobalItems.oreDust, new RecolorableItemRenderer());
	}

}
