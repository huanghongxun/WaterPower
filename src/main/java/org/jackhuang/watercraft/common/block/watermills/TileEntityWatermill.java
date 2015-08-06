package org.jackhuang.watercraft.common.block.watermills;

import java.util.List;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.client.render.ModelWaterWheel;
import org.jackhuang.watercraft.common.entity.EntityWaterWheel;
import org.jackhuang.watercraft.common.item.others.ItemOthers;
import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.common.item.range.ItemRange;
import org.jackhuang.watercraft.common.item.range.RangeInventorySlot;
import org.jackhuang.watercraft.common.item.range.RangeType;
import org.jackhuang.watercraft.common.item.rotors.ItemRotor;
import org.jackhuang.watercraft.common.item.rotors.RotorInventorySlot;
import org.jackhuang.watercraft.common.tileentity.TileEntityElectricMetaBlock;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.Utils;

import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * 
 * @author jackhuang1998
 * 
 */
public class TileEntityWatermill extends TileEntityElectricMetaBlock {

    private WaterType type;
    private EntityWaterWheel wheel;
    private boolean canWheelTurn = false;
    public int waterBlocks, lavaBlocks;

    RotorInventorySlot slotRotor;
    RangeInventorySlot slotUpdater;

    boolean sendInitData;

    public TileEntityWatermill() {
        super(0, 32767);
        addInvSlot(slotRotor = new RotorInventorySlot(this));
        addInvSlot(slotUpdater = new RangeInventorySlot(this, 4));
    }

    public TileEntityWatermill(WaterType type) {
        super(type.output, 32767);
        this.type = type;
        slotRotor = new RotorInventorySlot(this);
        addInvSlot(slotRotor);
        slotUpdater = new RangeInventorySlot(this);
        addInvSlot(slotUpdater);
    }

    @Override
    public void initNBT(NBTTagCompound tag, int meta) {
        if (meta == -1) {
            type = WaterType.values()[tag.getInteger("type")];
        } else {
            type = WaterType.values()[meta];
        }
        this.production = type.output;
        sendInitData = true;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (type == null)
            tag.setInteger("type", 0);
        else
            tag.setInteger("type", type.ordinal());
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);
        tag.setInteger("waterBlocks", waterBlocks);
        tag.setInteger("lavaBlocks", lavaBlocks);

        if (sendInitData) {
            sendInitData = false;
            tag.setBoolean("sendInitData", true);
            NBTTagCompound nbt = new NBTTagCompound();
            slotRotor.writeToNBT(nbt);
            tag.setTag("rotor", nbt);
            nbt = new NBTTagCompound();
            slotUpdater.writeToNBT(nbt);
            tag.setTag("updater", nbt);
            if (type == null)
                tag.setInteger("type", 0);
            else
                tag.setInteger("type", type.ordinal());
        }
    }

    @Override
    public void readPacketData(NBTTagCompound tag) {
        super.readPacketData(tag);
        waterBlocks = tag.getInteger("waterBlocks");
        lavaBlocks = tag.getInteger("lavaBlocks");

        if (tag.hasKey("sendInitData")) {
            slotRotor.readFromNBT((NBTTagCompound) tag.getTag("rotor"));
            slotUpdater.readFromNBT((NBTTagCompound) tag.getTag("updater"));
            type = WaterType.values()[tag.getInteger("type")];

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public WaterType getType() {
        return type;
    }

    private void getWaterBlocks() {
        lavaBlocks = waterBlocks = 0;
        if (type == null)
            return;

        int range = getRange();
        if (range * range * range > 3375) {
            lavaBlocks = waterBlocks = -1;
            return;
        }
        range /= 2;

        for (int xTest = -range; xTest <= range; xTest++) {
            for (int yTest = -range; yTest <= range; yTest++) {
                for (int zTest = -range; zTest <= range; zTest++) {
                    if (Utils.isWater(worldObj, xCoord + xTest, yCoord + yTest,
                            zCoord + zTest)) {
                        waterBlocks++;
                    } else if (Utils.isLava(worldObj, xCoord + xTest, yCoord
                            + yTest, zCoord + zTest)) {
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
                if (slotUpdater.get(i) != null
                        && slotUpdater.get(i).getItem() instanceof ItemRange)
                    return true;
        }
        return false;
    }

    public int getRange() {
        if (type == null)
            return 0;
        int range = type.length;
        if (slotUpdater != null && !slotUpdater.isEmpty()) {
            for (int i = 0; i < slotUpdater.size(); i++) {
                ItemStack is = slotUpdater.get(i);
                if (is == null)
                    continue;
                if (is != null
                        && slotUpdater.get(i).getItem() instanceof ItemRange)
                    if (is.getItemDamage() >= RangeType.values().length)
                        return type.length;
                range -= is.stackSize
                        * RangeType.values()[is.getItemDamage()].range;
            }
        }
        if (range < 3)
            range = 3;
        return range;
    }

    public boolean hasRotor() {
        return slotRotor != null && !slotRotor.isEmpty()
                && slotRotor.get(0).getItem() instanceof ItemRotor;
    }

    public ItemRotor getRotor() {
        return (ItemRotor) slotRotor.get(0).getItem();
    }

    private void damageRotor(int tick) {
        if (!hasRotor())
            return;
        ItemRotor rotor = getRotor();
        rotor.tickRotor(slotRotor.get(0), this, worldObj);
        if (!rotor.type.isInfinite()) {
            if (slotRotor.get(0).getItemDamage() + tick > slotRotor
                    .get(0).getMaxDamage()) {
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

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return getDroppedItemStack();
    }

    public void spawnWheel() {
        wheel = new EntityWaterWheel(worldObj, this);
        wheel.parent = this;
        wheel.parentFacing = getFacing();
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
    @Method(modid = Mods.IDs.Factorization)
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getInfo());
        if (getFluidTank() == null)
            return sb.toString();
        FluidStack f = getFluidTank().getFluid();
        sb.append("Stored Fluid: "
                + (f == null ? "Empty" : f.getLocalizedName()) + "\n");
        sb.append("Fluid Amount: " + getFluidTank().getFluidAmount() + "mb\n");
        return sb.toString();
    }
    
    @Override
    public ItemStack getDroppedItemStack() {
        return new ItemStack(this.getBlockType(), 1, type == null ? 0 : type.ordinal());
    }
}
