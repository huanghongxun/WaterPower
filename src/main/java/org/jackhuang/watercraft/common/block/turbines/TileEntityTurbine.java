package org.jackhuang.watercraft.common.block.turbines;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.block.reservoir.Reservoir;
import org.jackhuang.watercraft.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.watercraft.common.block.watermills.WaterType;
import org.jackhuang.watercraft.common.item.rotors.ItemRotor;
import org.jackhuang.watercraft.common.item.rotors.RotorInventorySlot;
import org.jackhuang.watercraft.common.tileentity.TileEntityBaseGenerator;
import org.jackhuang.watercraft.common.tileentity.TileEntityElectricMetaBlock;
import org.jackhuang.watercraft.util.WPLog;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;

public class TileEntityTurbine extends TileEntityElectricMetaBlock {

    public static int maxOutput = 32767;
    // public int speed;
    private TurbineType type;

    RotorInventorySlot slotRotor;

    boolean sendInitData;

    public TileEntityTurbine() {
        super(0, 10000000);
        addInvSlot(slotRotor = new RotorInventorySlot(this, 1));
    }

    public TileEntityTurbine(TurbineType type) {
        super(type.percent, 10000000);
        addInvSlot(slotRotor = new RotorInventorySlot(this, 1));
    }

    @Override
    public void initNBT(NBTTagCompound tag, int meta) {
        if (meta == -1) {
            type = TurbineType.values()[tag.getInteger("type")];
        } else {
            type = TurbineType.values()[meta];
        }
        this.production = type.percent;
        sendInitData = true;
    }

    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        // tag.setInteger("speed", this.speed);

        if (type == null)
            tag.setInteger("type", 0);
        else
            tag.setInteger("type", type.ordinal());
    }

    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        // this.speed = tag.getInteger("speed");
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);

        // tag.setInteger("speed", speed);

        if (sendInitData) {
            sendInitData = false;
            tag.setBoolean("sendInitData", true);
            if (type == null)
                tag.setInteger("type", 0);
            else
                tag.setInteger("type", type.ordinal());
        }
    }

    @Override
    public void readPacketData(NBTTagCompound tag) {
        super.readPacketData(tag);
        // speed = tag.getInteger("speed");

        if (tag.hasKey("sendInitData")) {
            type = TurbineType.values()[tag.getInteger("type")];

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public TurbineType getType() {
        return type;
    }

    private TileEntityReservoir getWater(World world, int x, int y, int z) {
        TileEntityReservoir reservoir = null;
        switch (getFacing()) {
        case 2:
            // z+1
            if (Reservoir.isRes(world, x, y, z + 1)) {
                reservoir = (TileEntityReservoir) world.getTileEntity(x, y,
                        z + 1);
            }
            break;
        case 5:
            // x-1
            if (Reservoir.isRes(world, x - 1, y, z))
                reservoir = (TileEntityReservoir) world.getTileEntity(x - 1, y,
                        z);
            break;
        case 3:
            // z-1
            if (Reservoir.isRes(world, x, y, z - 1))
                reservoir = (TileEntityReservoir) world.getTileEntity(x, y,
                        z - 1);
            break;
        case 4:
            // x+1
            if (Reservoir.isRes(world, x + 1, y, z))
                reservoir = (TileEntityReservoir) world.getTileEntity(x + 1, y,
                        z);
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
        if (pair == null)
            return 0;
        else {
            WPLog.debugLog("fluidAmount=" + pair.getFluidAmount());
            WPLog.debugLog("maxUse=" + pair.type.maxUse);
            double use = Math.min(pair.getFluidAmount(), pair.type.maxUse);
            WPLog.debugLog("use=" + use);
            int multiple = 0;
            if (pair.getFluidfromTank() == FluidRegistry.WATER)
                multiple = 1;
            else if (FluidRegistry.isFluidRegistered("steam")
                    && pair.getFluidfromTank() == FluidRegistry
                            .getFluid("steam")
                    || FluidRegistry.isFluidRegistered("ic2steam")
                    && pair.getFluidfromTank() == FluidRegistry
                            .getFluid("ic2steam"))
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

    public boolean hasRotor() {
        return slotRotor != null && !slotRotor.isEmpty()
                && slotRotor.get(0).getItem() instanceof ItemRotor;
    }

    public ItemRotor getRotor() {
        return (ItemRotor) slotRotor.get(0).getItem();
    }

    private void damageRotor(int tick) {
        ItemRotor rotor = getRotor();
        rotor.tickRotor(slotRotor.get(0), this, worldObj);
        if (!rotor.type.isInfinite()) {
            if (slotRotor.get(0).getItemDamage() + tick > slotRotor.get(0)
                    .getMaxDamage()) {
                slotRotor.put(0, null);
            } else {
                int damage = slotRotor.get(0).getItemDamage() + tick;
                slotRotor.get(0).setItemDamage(damage);
            }
            markDirty();
        }
    }

    private double tickRotor() {
        if (!Reference.General.watermillNeedsRotor)
            return 1;
        return hasRotor() ? getRotor().type.getEfficiency() : 0;
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
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(this.getBlockType(), 1, type.ordinal());
    }

    @Override
    public boolean isActive() {
        return super.isActive() && (storage < maxStorage);
    }
    /*
     * private void calSpeed() { TileEntityReservoir r = getWater(worldObj,
     * xCoord, yCoord, zCoord); if (r != null && r.getWater() > 0 && hasRotor())
     * { // canOutput = true; speed++; if (speed > 50) speed = 50; } else {
     * speed--; if (speed < 0) speed = 0; } if (speed > 0) damageRotor(1); }
     */

}