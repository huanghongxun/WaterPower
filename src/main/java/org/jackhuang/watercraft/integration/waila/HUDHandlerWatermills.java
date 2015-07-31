package org.jackhuang.watercraft.integration.waila;

import java.util.List;

import org.jackhuang.watercraft.common.block.watermills.TileEntityWatermill;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

@InterfaceList({
	@Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = Mods.IDs.Waila)
})
public class HUDHandlerWatermills implements IWailaDataProvider {

	@Override
	@Method(modid = Mods.IDs.Waila)
	public List<String> getWailaBody(ItemStack arg0, List<String> arg1,
			IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
		TileEntity te = arg2.getTileEntity();
		if(!(te instanceof TileEntityWatermill)) return arg1;
		TileEntityWatermill tile = (TileEntityWatermill) te;
		arg1.add("Stored/EU: " + tile.storage);
		arg1.add("Lastest Output/EU: " + tile.lastestOutput);
		arg1.add("Facing: " + tile.getFacing());
		if(tile.getFluidTank() == null) return arg1;
		FluidStack f = tile.getFluidTank().getFluid();
		arg1.add("Stored Fluid: " + (f == null ? "Empty" : f.getLocalizedName()));
		arg1.add("Fluid amount/mb: " + tile.getFluidTank().getFluidAmount());
		return arg1;
	}

	@Override
	@Method(modid = Mods.IDs.Waila)
	public List<String> getWailaHead(ItemStack arg0, List<String> arg1,
			IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
		TileEntity te = arg2.getTileEntity();
		if(!(te instanceof TileEntityWatermill)) return arg1;
		TileEntityWatermill tile = (TileEntityWatermill) te;
		arg1.set(0, StatCollector.translateToLocal("cptwtrml.watermill.WATERMILL") + " " + tile.getType().name());
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

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP arg0, TileEntity arg1,
			NBTTagCompound arg2, World arg3, int arg4, int arg5, int arg6) {
		// TODO Auto-generated method stub
		return null;
	}

}
