/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import waterpower.client.gui.ContainerRotor;
import waterpower.client.gui.ContainerStandardMachine;
import waterpower.client.gui.DefaultGuiIds;
import waterpower.common.block.machines.ContainerCentrifuge;
import waterpower.common.block.machines.TileEntityAdvancedCompressor;
import waterpower.common.block.machines.TileEntityCentrifuge;
import waterpower.common.block.machines.TileEntityCompressor;
import waterpower.common.block.machines.TileEntityCutter;
import waterpower.common.block.machines.TileEntityLathe;
import waterpower.common.block.machines.TileEntityMacerator;
import waterpower.common.block.machines.TileEntitySawmill;
import waterpower.common.block.reservoir.ContainerReservoir;
import waterpower.common.block.reservoir.TileEntityReservoir;
import waterpower.common.block.turbines.TileEntityTurbine;
import waterpower.common.block.watermills.ContainerWatermill;
import waterpower.common.block.watermills.TileEntityWatermill;

public class CommonProxy implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer thePlayer, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if (tileEntity == null)
            return null;
        if (ID == DefaultGuiIds.get("tileEntityTurbine")) {
            if (tileEntity instanceof TileEntityTurbine) {
                TileEntityTurbine tileEntityT = (TileEntityTurbine) tileEntity;
                return new ContainerRotor(thePlayer, tileEntityT);
            }
        } else if (ID == DefaultGuiIds.get("tileEntityWatermill")) {
            TileEntityWatermill tileEntityCW = (TileEntityWatermill) tileEntity;
            return new ContainerWatermill(thePlayer, tileEntityCW);
        } else if (ID == DefaultGuiIds.get("tileEntityReservoir")) {
            TileEntityReservoir tileEntityR = (TileEntityReservoir) tileEntity;
            return new ContainerReservoir(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntityMacerator")) {
            TileEntityMacerator tileEntityR = (TileEntityMacerator) tileEntity;
            return new ContainerStandardMachine(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntityCompressor")) {
            TileEntityCompressor tileEntityR = (TileEntityCompressor) tileEntity;
            return new ContainerStandardMachine(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntityAdvancedCompressor")) {
            TileEntityAdvancedCompressor tileEntityR = (TileEntityAdvancedCompressor) tileEntity;
            return new ContainerStandardMachine(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntitySawmill")) {
            TileEntitySawmill tileEntityR = (TileEntitySawmill) tileEntity;
            return new ContainerStandardMachine(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntityLathe")) {
            TileEntityLathe tileEntityR = (TileEntityLathe) tileEntity;
            return new ContainerStandardMachine(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntityCutter")) {
            TileEntityCutter tileEntityR = (TileEntityCutter) tileEntity;
            return new ContainerStandardMachine(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntityCentrifuge")) {
            TileEntityCentrifuge tileEntityR = (TileEntityCentrifuge) tileEntity;
            return new ContainerCentrifuge(thePlayer, tileEntityR);
        }
        return null;
    }

    public void preInit() {

    }

    public void init() {
    }

}
