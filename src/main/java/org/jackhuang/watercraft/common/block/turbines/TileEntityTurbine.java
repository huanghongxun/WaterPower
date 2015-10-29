package org.jackhuang.watercraft.common.block.turbines;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.block.reservoir.Reservoir;
import org.jackhuang.watercraft.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.watercraft.common.block.tileentity.TileEntityRotor;
import org.jackhuang.watercraft.util.WPLog;

public class TileEntityTurbine extends TileEntityRotor {

    public static int maxOutput = 32767;
    // public int speed;
    private TurbineType type;

    public TileEntityTurbine() {
        super(0, 10000000);
    }

    public TileEntityTurbine(TurbineType type) {
        super(type.percent, 10000000);
    }

    @Override
    public void initNBT(NBTTagCompound tag, int meta) {
        type = TurbineType.values()[meta == -1 ? tag.getInteger("type") : meta];
        this.production = type.percent;
    }

    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("type", type == null ? 0 : type.ordinal());
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);

        tag.setInteger("type", type == null ? 0 : type.ordinal());
    }

    @Override
    public void readPacketData(NBTTagCompound tag) {
        super.readPacketData(tag);

        TurbineType tt = type;
        type = TurbineType.values()[tag.getInteger("type")];

        if (tt == null || !tt.equals(type))
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public TurbineType getType() {
        return type;
    }

    private TileEntityReservoir getWater(World world, int x, int y, int z) {
        TileEntityReservoir reservoir = null;
        switch (getDirection()) {
        case 2:
            // z+1
            if (Reservoir.isRes(world, x, y, z + 1)) {
                reservoir = (TileEntityReservoir) world.getTileEntity(x, y, z + 1);
            }
            break;
        case 5:
            // x-1
            if (Reservoir.isRes(world, x - 1, y, z))
                reservoir = (TileEntityReservoir) world.getTileEntity(x - 1, y, z);
            break;
        case 3:
            // z-1
            if (Reservoir.isRes(world, x, y, z - 1))
                reservoir = (TileEntityReservoir) world.getTileEntity(x, y, z - 1);
            break;
        case 4:
            // x+1
            if (Reservoir.isRes(world, x + 1, y, z))
                reservoir = (TileEntityReservoir) world.getTileEntity(x + 1, y, z);
            break;
        }
        return reservoir;
    }

    public double getWater() {
        TileEntityReservoir pair = getWater(worldObj, xCoord, yCoord, zCoord);
        return pair.getFluidAmount();
    }

    protected double computeOutput(World world, int x, int y, int z) {
        // calSpeed();
        /*
         * double use = Math.min(water,
         * ReservoirType.values()[size.blockType].maxUse); double baseEnergy =
         * use * 5000; LogHelper.log("Can output?" + canOutput); if(!canOutput)
         * return 0; double per = tickRotor(); LogHelper.log("per?" + per);
         * if(per > 0) { double energy = baseEnergy * per * (speed / 1000);
         * LogHelper.log("energy?" + energy); LogHelper.log("use?" + use); water
         * -= use; return energy; } return 0;
         */
        TileEntityReservoir pair = getWater(world, x, y, z);
        if (pair == null || pair.getFluidTank() == null || pair.getFluidStackfromTank() == null || pair.getFluidfromTank() == null)
            return 0;
        else {
            WPLog.debugLog("fluidAmount=" + pair.getFluidAmount());
            WPLog.debugLog("maxUse=" + pair.type.maxUse);
            double use = Math.min(pair.getFluidAmount(), pair.type.maxUse);
            WPLog.debugLog("use=" + use);
            int multiple = 0;
            if (pair.getFluidfromTank() == FluidRegistry.WATER)
                multiple = 1;
            else if (FluidRegistry.isFluidRegistered("steam") && pair.getFluidfromTank() == FluidRegistry.getFluid("steam")
                    || FluidRegistry.isFluidRegistered("ic2steam") && pair.getFluidfromTank() == FluidRegistry.getFluid("ic2steam"))
                multiple = 5;
            if (multiple == 0)
                return 0;
            double baseEnergy = use * type.percent / 2048 * multiple;
            WPLog.debugLog("baseEnergy" + baseEnergy);
            // WPLog.debugLog("speed" + speed);
            double per = tickRotor();
            WPLog.debugLog("per=" + per);
            if (per > 0) {
                double energy = baseEnergy * per; // * ((double) speed / 50);
                WPLog.debugLog("energy = " + energy);
                if (energy > 0) {
                    pair.useLiquid((int) use);
                    damageRotor(1);
                }
                WPLog.debugLog("use=" + use);
                return energy;
            }
        }
        return 0;
    }

    @Override
    public String getInventoryName() {
        return type == null ? "NULL" : type.getShowedName();
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityTurbine");
    }

    @Override
    public ItemStack getDroppedItemStack() {
        return new ItemStack(GlobalBlocks.turbine, 1, type == null ? 0 : type.ordinal());
    }

    @Override
    public boolean isActive() {
        return super.isActive() && (storage < maxStorage);
    }

    @Override
    protected boolean allowedSendPacketTank() {
        return false;
    }

}