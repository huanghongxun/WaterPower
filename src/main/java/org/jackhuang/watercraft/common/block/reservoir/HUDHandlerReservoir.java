package org.jackhuang.watercraft.common.block.reservoir;

import java.util.List;

import org.jackhuang.watercraft.util.mods.Mods;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

@InterfaceList({
	@Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = Mods.IDs.Waila)
})
public class HUDHandlerReservoir implements IWailaDataProvider {

	@Override
	@Method(modid = Mods.IDs.Waila)
	public List<String> getWailaBody(ItemStack arg0, List<String> arg1,
			IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
		TileEntity te = arg2.getTileEntity();
		if(!(te instanceof TileEntityReservoir)) return arg1;
		TileEntityReservoir tile = (TileEntityReservoir) te;
		arg1.add("Capacity: " + tile.getMaxWater());
		arg1.add("Add: " + tile.getLastAddedWater());
		arg1.add("Fluid: " + tile.getFluidTank().getFluid().getLocalizedName());
		arg1.add("Stored: " + tile.getWater());
		arg1.add("Stored HP: " + tile.getHPWater());
		return arg1;
	}

	@Override
	@Method(modid = Mods.IDs.Waila)
	public List<String> getWailaHead(ItemStack arg0, List<String> arg1,
			IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
		TileEntity te = arg2.getTileEntity();
		if(!(te instanceof TileEntityReservoir)) return arg1;
		TileEntityReservoir tile = (TileEntityReservoir) te;
		arg1.set(0, tile.type.getShowedName() + " " + StatCollector.translateToLocal("cptwtrml.reservoir.RESERVOIR"));
		return arg1;
	}

	@Override
	@Method(modid = Mods.IDs.Waila)
	public ItemStack getWailaStack(IWailaDataAccessor arg0,
			IWailaConfigHandler arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(modid = Mods.IDs.Waila)
	public List<String> getWailaTail(ItemStack arg0, List<String> arg1,
			IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
		// TODO Auto-generated method stub
		return arg1;
	}

}