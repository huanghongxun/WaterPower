package org.jackhuang.watercraft.common.block.watermills;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.block.tileentity.TileEntityRotor;
import org.jackhuang.watercraft.common.entity.EntityWaterWheel;
import org.jackhuang.watercraft.common.item.range.ItemRange;
import org.jackhuang.watercraft.common.item.range.RangeInventorySlot;
import org.jackhuang.watercraft.common.item.range.RangeType;
import org.jackhuang.watercraft.util.Utils;

/**
 * 
 * @author jackhuang1998
 * 
 */
public class TileEntityWatermill extends TileEntityRotor {

    private WaterType type;
    private EntityWaterWheel wheel;
    private boolean canWheelTurn = false;
    public int waterBlocks, lavaBlocks;
    public int preWaterBlocks = -999, preLavaBlocks = -999;
    public int range = 0, rotor = -1;

    RangeInventorySlot slotUpdater;

    public TileEntityWatermill() {
        super(0, 2097152);
        addInvSlot(slotUpdater = new RangeInventorySlot(this, 4));
    }

    public TileEntityWatermill(WaterType type) {
        super(type.output, 2097152);
        this.type = type;
        slotUpdater = new RangeInventorySlot(this);
        addInvSlot(slotUpdater);
    }

    @Override
    public void initNBT(NBTTagCompound tag, int meta) {
        type = WaterType.values()[meta == -1 ? tag.getInteger("type") : meta];
        production = type.output;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("type", type == null ? 0 : type.ordinal());
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);
        tag.setInteger("waterBlocks", waterBlocks);
        tag.setInteger("lavaBlocks", lavaBlocks);

        rotor = hasRotor() ? getRotor().type.ordinal() : -1;
        tag.setInteger("rotor", rotor);
        tag.setInteger("range", range);
        tag.setInteger("type", type == null ? 0 : type.ordinal());
    }

    @Override
    public void readPacketData(NBTTagCompound tag) {
        super.readPacketData(tag);
        waterBlocks = tag.getInteger("waterBlocks");
        lavaBlocks = tag.getInteger("lavaBlocks");

        rotor = tag.getInteger("rotor");
        range = tag.getInteger("range");
        WaterType wt = type;
        type = WaterType.values()[tag.getInteger("type")];

        if (wt == null || !wt.equals(type))
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public WaterType getType() {
        return type;
    }

    private void getWaterBlocks() {
        preLavaBlocks = lavaBlocks;
        preWaterBlocks = waterBlocks;
        lavaBlocks = waterBlocks = 0;
        if (type == null)
            return;

        int range = getRange();
        if (range * range * range > 729) {
            lavaBlocks = waterBlocks = -1;
            return;
        }
        range /= 2;

        for (int xTest = -range; xTest <= range; xTest++) {
            for (int yTest = -range; yTest <= range; yTest++) {
                for (int zTest = -range; zTest <= range; zTest++) {
                    if (Utils.isWater(worldObj, xCoord + xTest, yCoord + yTest, zCoord + zTest)) {
                        waterBlocks++;
                    } else if (Utils.isLava(worldObj, xCoord + xTest, yCoord + yTest, zCoord + zTest)) {
                        lavaBlocks++;
                    }
                }
            }
        }

        if (type.ordinal() < 2)
            lavaBlocks = 0;
    }

    public boolean isRangeSupported() {
        return waterBlocks != -1 && lavaBlocks != -1;
    }

    protected double computeOutput(World world, int x, int y, int z) {
        if (type == null || waterBlocks == -1) {
            return 0;
        }

        double vol = getRange();
        vol *= vol * vol;
        double waterPercent = (double) waterBlocks / (vol - 1);
        double lavaPercent = (double) lavaBlocks / (vol - 1);
        double percent = waterPercent + lavaPercent * 4;
        double energy = type.output * percent;
        energy *= tickRotor();
        if (energy > 0)
            damageRotor(1);
        return energy;
    }

    @Override
    protected int getProduction() {
        if (lavaBlocks > 0)
            return super.getProduction() * 4;
        else
            return super.getProduction();
    }

    public boolean hasRangeUpdater() {
        if (slotUpdater != null && !slotUpdater.isEmpty()) {
            for (int i = 0; i < slotUpdater.size(); i++)
                if (slotUpdater.get(i) != null && slotUpdater.get(i).getItem() instanceof ItemRange)
                    return true;
        }
        return false;
    }

    public int getRange() {
        if (WaterPower.isClientSide() && range != 0)
            return range;
        if (type == null)
            return 0;
        int range = type.length;
        if (slotUpdater != null && !slotUpdater.isEmpty()) {
            for (int i = 0; i < slotUpdater.size(); i++) {
                ItemStack is = slotUpdater.get(i);
                if (is == null)
                    continue;
                if (is != null && slotUpdater.get(i).getItem() instanceof ItemRange && is.getItemDamage() >= RangeType.values().length)
                    return type.length;
                range -= is.stackSize * RangeType.values()[is.getItemDamage()].range;
            }
        }
        if (range < 3)
            range = 3;
        return range;
    }

    @Override
    public String getInventoryName() {
        return type == null ? "NULL" : type.getShowedName();
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityWatermill");
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        getWaterBlocks();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (isServerSide())
            return;
        if (wheel == null && hasRotor() && isRangeSupported())
            spawnWheel();
        else if (wheel != null && (!hasRotor() || !isRangeSupported()))
            destroyWheel();
        if (wheel != null) {
            updateWheel();
        }

    }

    public void spawnWheel() {
        wheel = new EntityWaterWheel(worldObj, this);
        wheel.parent = this;
        wheel.parentFacing = getDirection();
        this.worldObj.spawnEntityInWorld(wheel);
    }

    public void destroyWheel() {
        this.wheel.destroy();
        this.wheel = null;
    }

    public boolean canWheelTurn() {
        return storage < maxStorage;
    }

    public void updateWheel() {
        canWheelTurn = canWheelTurn();
        wheel.parent = this;
    }

    public float getRotationSpeed() {
        int vol = getRange();
        if (vol == 0)
            return 0;
        return (float) waterBlocks / (vol * vol * vol);
    }

    float angle = 0;

    public float getWheelAngle() {
        float rotationSpeed = getRotationSpeed() / 400;
        if (!Minecraft.getMinecraft().isGamePaused())
            angle += rotationSpeed;
        if (angle >= 1)
            angle -= 1;
        return angle * 360;
    }

    @Override
    public boolean isActive() {
        return super.isActive() && canWheelTurn();
    }

    @Override
    public ItemStack getDroppedItemStack() {
        return new ItemStack(GlobalBlocks.waterMill, 1, type == null ? 0 : type.ordinal());
    }

    @Override
    protected boolean allowedSendPacketTank() {
        return false;
    }
}
