/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.integration.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import net.minecraftforge.fml.common.Optional.Method;
import waterpower.client.Local;
import waterpower.common.block.machines.TileEntityStandardWaterMachine;
import waterpower.util.Mods;

@InterfaceList({ @Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = Mods.IDs.Waila) })
public class HUDHandlerMachine implements IWailaDataProvider {

    @Override
    @Method(modid = Mods.IDs.Waila)
    public List<String> getWailaBody(ItemStack arg0, List<String> arg1, IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
        NBTTagCompound tag = arg2.getNBTData();

        if (tag.hasKey("using")) {
            arg1.add(Local.get("cptwtrml.gui.using") + ": " + tag.getInteger("using") + "mb/t");
            arg1.add(Local.get("cptwtrml.gui.stored") + ": " + tag.getInteger("stored") + "mb");
            arg1.add(Local.get("cptwtrml.gui.capacity") + ": " + tag.getInteger("capacity") + "mb");
        }
        return arg1;
    }

    @Override
    @Method(modid = Mods.IDs.Waila)
    public List<String> getWailaHead(ItemStack arg0, List<String> arg1, IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
        return arg1;
    }

    @Override
    @Method(modid = Mods.IDs.Waila)
    public ItemStack getWailaStack(IWailaDataAccessor arg0, IWailaConfigHandler arg1) {
        return null;
    }

    @Override
    @Method(modid = Mods.IDs.Waila)
    public List<String> getWailaTail(ItemStack arg0, List<String> arg1, IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
        return arg1;
    }

    @Override
    @Method(modid = Mods.IDs.Waila)
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (te instanceof TileEntityStandardWaterMachine) {
            TileEntityStandardWaterMachine tile = (TileEntityStandardWaterMachine) te;
            tag.setInteger("using", tile.energyConsume);
            tag.setInteger("stored", tile.getFluidAmount());
            tag.setInteger("capacity", tile.getFluidTankCapacity());
        }
        return tag;
    }

}
