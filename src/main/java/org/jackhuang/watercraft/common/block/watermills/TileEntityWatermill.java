package org.jackhuang.watercraft.common.block.watermills;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.entity.EntityWaterWheel;
import org.jackhuang.watercraft.common.item.range.ItemRange;
import org.jackhuang.watercraft.common.item.range.RangeInventorySlot;
import org.jackhuang.watercraft.common.item.range.RangeType;
import org.jackhuang.watercraft.common.item.rotors.ItemRotor;
import org.jackhuang.watercraft.common.item.rotors.RotorInventorySlot;
import org.jackhuang.watercraft.common.tileentity.TileEntityElectricMetaBlock;
import org.jackhuang.watercraft.util.Utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 *
 * @author jackhuang1998
 *
 */
public class TileEntityWatermill extends TileEntityElectricMetaBlock {

    RotorInventorySlot slotRotor;
    RangeInventorySlot slotUpdater;

    public TileEntityWatermill() {
	super(0, 32767);
	slotRotor = new RotorInventorySlot(this);
	addInvSlot(slotRotor);
	slotUpdater = new RangeInventorySlot(this, 4);
	addInvSlot(slotUpdater);
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
	sendUpdateToClient();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
	super.writeToNBT(nbttagcompound);

	if (type == null) {
	    nbttagcompound.setInteger("type", 0);
	} else {
	    nbttagcompound.setInteger("type", type.ordinal());
	}
    }

    private void getWaterBlocks() {
	lavaBlocks = waterBlocks = 0;
	if (type == null) {
	    return;
	}

	int range = getRange();
	if (range * range * range > 274625) {
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

	if (type.ordinal() < 2) {
	    lavaBlocks = 0;
	}
    }

    private WaterType type;
    private EntityWaterWheel wheel;
    private boolean canWheelTurn = false;
    public int waterBlocks, lavaBlocks;

    public WaterType getType() {
	return type;
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
	if (energy > 0) {
	    damageRotor(1);
	}
	return energy;
    }

    @Override
    protected int getProduction() {
	if (lavaBlocks > 0) {
	    return super.getProduction() * 4;
	} else {
	    return super.getProduction();
	}
    }

    public boolean hasRangeUpdater() {
	if (slotUpdater != null && !slotUpdater.isEmpty()) {
	    for (int i = 0; i < slotUpdater.size(); i++) {
		if (slotUpdater.get(i) != null
			&& slotUpdater.get(i).getItem() instanceof ItemRange) {
		    return true;
		}
	    }
	}
	return false;
    }

    public int getRange() {
	if (type == null) {
	    return 0;
	}
	int range = type.length;
	if (slotUpdater != null && !slotUpdater.isEmpty()) {
	    for (int i = 0; i < slotUpdater.size(); i++) {
		ItemStack is = slotUpdater.get(i);
		if (is == null) {
		    continue;
		}
		if (is != null
			&& slotUpdater.get(i).getItem() instanceof ItemRange) {
		    if (is.getItemDamage() >= RangeType.values().length) {
			return type.length;
		    }
		}
		range -= is.stackSize
			* RangeType.values()[is.getItemDamage()].range;
	    }
	}
	if (range < 3) {
	    range = 3;
	}
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
	if (!hasRotor()) {
	    return;
	}
	ItemRotor rotor = getRotor();
	rotor.tickRotor(invSlots.get(0).get(0), this, worldObj);
	if (!rotor.type.isInfinite()) {
	    if (invSlots.get(0).get(0).getItemDamage() + tick > invSlots.get(0)
		    .get(0).getMaxDamage()) {
		invSlots.get(0).put(0, null);
	    } else {
		int damage = invSlots.get(0).get(0).getItemDamage() + tick;
		invSlots.get(0).get(0).setItemDamage(damage);
	    }
	    markDirty();
	}
    }

    private double tickRotor() {
	if (hasRotor()) {
	    ItemRotor rotor = getRotor();
	    if (worldObj.isRemote) {
		return rotor.type.getEfficiency();
	    }

	    return rotor.type.getEfficiency();
	}
	if (Reference.General.watermillNeedsRotor) {
	    return 0;
	} else {
	    return 1;
	}
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
	super.writePacketData(tag);
	if (type == null) {
	    tag.setInteger("type", 0);
	} else {
	    tag.setInteger("type", type.ordinal());
	}
	tag.setInteger("waterBlocks", waterBlocks);
	tag.setInteger("lavaBlocks", lavaBlocks);
    }

    boolean markedBlockForUpdate = false;

    @Override
    public void readPacketData(NBTTagCompound tag) {
	super.readPacketData(tag);
	waterBlocks = tag.getInteger("waterBlocks");
	lavaBlocks = tag.getInteger("lavaBlocks");
	type = WaterType.values()[tag.getInteger("type")];
	if (!markedBlockForUpdate) {
	    markedBlockForUpdate = true;
	    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
    }

    @Override
    public String getInventoryName() {
	if (type == null) {
	    return "NULL";
	}
	return type.getShowedName();
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
    protected void onUpdateClientAndServer() {
	if (wheel == null && hasRotor()) {
	    spawnWheel();
	} else if (wheel != null && !hasRotor()) {
	    destroyWheel();
	}
	if (wheel != null) {
	    updateWheel();
	}

    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
	return new ItemStack(this.getBlockType(), 1, type.ordinal());
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
	if (vol == 0) {
	    return 0;
	}
	return (float) waterBlocks / (vol * vol * vol);
    }

    float angle = 0;

    public float getWheelAngle() {
	float rotationSpeed = getRotationSpeed() / 400;
	angle += rotationSpeed;
	if (angle >= 1) {
	    angle -= 1;
	}
	return angle * 360;
    }

    @Override
    public boolean isActive() {
	return super.isActive() && canWheelTurn();
    }
}
