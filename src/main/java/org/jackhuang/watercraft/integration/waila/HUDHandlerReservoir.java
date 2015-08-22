package org.jackhuang.watercraft.integration.waila;

import java.util.List;

import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.watercraft.util.Mods;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class HUDHandlerReservoir implements IWailaDataProvider {

    @Override
    public List<String> getWailaBody(ItemStack arg0, List<String> arg1,
            IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
        TileEntity te = arg2.getTileEntity();
        if (!(te instanceof TileEntityReservoir))
            return arg1;
        TileEntityReservoir tile = (TileEntityReservoir) te;
        arg1.add(StatCollector.translateToLocal("cptwtrml.gui.reservoir.add")
                + ": " + tile.getLastAddedWater());
        if (tile.getFluidTank() == null)
            return arg1;
        FluidStack f = tile.getFluidTank().getFluid();
        arg1.add((f == null ? StatCollector
                .translateToLocal("cptwtrml.gui.empty") : f.getFluid().getLocalizedName())
                + ": "
                + tile.getFluidAmount()
                + "/"
                + tile.getFluidTankCapacity()
                + "mb");
        return arg1;
    }

    @Override
    public List<String> getWailaHead(ItemStack arg0, List<String> arg1,
            IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
        TileEntity te = arg2.getTileEntity();
        if (!(te instanceof TileEntityReservoir))
            return arg1;
        TileEntityReservoir tile = (TileEntityReservoir) te;
        arg1.add(0, tile.type.getShowedName());
        return arg1;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor arg0,
            IWailaConfigHandler arg1) {
        TileEntity te = arg0.getTileEntity();
        if (!(te instanceof TileEntityReservoir))
            return null;
        TileEntityReservoir tile = (TileEntityReservoir) te;
        return new ItemStack(GlobalBlocks.reservoir, 1, tile.type.ordinal());
    }

    @Override
    public List<String> getWailaTail(ItemStack arg0, List<String> arg1,
            IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
        return arg1;
    }

}