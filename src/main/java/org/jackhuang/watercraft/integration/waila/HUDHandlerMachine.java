/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.integration.waila;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jackhuang.watercraft.common.block.machines.TileEntityStandardWaterMachine;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

@InterfaceList({
    @Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = Mods.IDs.Waila)
})
public class HUDHandlerMachine implements IWailaDataProvider {

    @Override
    @Method(modid = Mods.IDs.Waila)
    public List<String> getWailaBody(ItemStack arg0, List<String> arg1,
            IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
        TileEntity te = arg2.getTileEntity();
        if(!(te instanceof TileEntityStandardWaterMachine)) return arg1;
        TileEntityStandardWaterMachine tile = (TileEntityStandardWaterMachine) te;
        
        arg1.add(StatCollector.translateToLocal("cptwtrml.gui.using") + ": " + tile.energyConsume + "mb/t");
        arg1.add(StatCollector.translateToLocal("cptwtrml.gui.stored") + ": " + tile.getFluidAmount() + "mb");
        arg1.add(StatCollector.translateToLocal("cptwtrml.gui.capacity") + ": " + tile.getFluidTankCapacity() + "mb");
        
        return arg1;
    }

    @Override
    @Method(modid = Mods.IDs.Waila)
    public List<String> getWailaHead(ItemStack arg0, List<String> arg1,
            IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
        // TODO Auto-generated method stub
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
    @Method(modid = Mods.IDs.Waila)
    public NBTTagCompound getNBTData(EntityPlayerMP arg0, TileEntity arg1,
            NBTTagCompound arg2, World arg3, int arg4, int arg5, int arg6) {
        // TODO Auto-generated method stub
        return null;
    }

}
