package org.jackhuang.compactwatermills.common.block.turbines;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.client.gui.DefaultGuiIds;
import org.jackhuang.compactwatermills.common.block.reservoir.Reservoir;
import org.jackhuang.compactwatermills.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.compactwatermills.common.block.watermills.WaterType;
import org.jackhuang.compactwatermills.common.item.rotors.ItemRotor;
import org.jackhuang.compactwatermills.common.item.rotors.RotorInventorySlot;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityBaseGenerator;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityElectricMetaBlock;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityTurbine extends TileEntityElectricMetaBlock {

	public static int maxOutput = 32767;
	public int speed;
	private TurbineType type;

	public TurbineType getType() {
		return type;
	}

	public TileEntityTurbine() {
		super(0, 10000000);
		addInvSlot(new RotorInventorySlot(this, 1));
	}

	public TileEntityTurbine(TurbineType type) {
		super(type.percent, 10000000);
		addInvSlot(new RotorInventorySlot(this, 1));
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.speed = nbttagcompound.getInteger("speed");
	}

	@Override
	public void initNBT(NBTTagCompound tag, int meta) {
		if (meta == -1) {
			type = TurbineType.values()[tag.getInteger("type")];
		} else {
			type = TurbineType.values()[meta];
		}
		this.production = type.percent;
		sendUpdateToClient();
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("speed", this.speed);

		if(type == null) nbttagcompound.setInteger("type", 0);
		else nbttagcompound.setInteger("type", type.ordinal());
	}

	private TileEntityReservoir getWater(World world, int x, int y, int z) {
		TileEntityReservoir reservoir = null;
		switch (getFacing()) {
		case 2:
			// z+1
			if (Reservoir.isRes(world, x, y, z + 1)) {
				reservoir = (TileEntityReservoir) world.getTileEntity(x,
						y, z + 1);
			}
			break;
		case 5:
			// x-1
			if (Reservoir.isRes(world, x - 1, y, z))
				reservoir = (TileEntityReservoir) world.getTileEntity(
						x - 1, y, z);
			break;
		case 3:
			// z-1
			if (Reservoir.isRes(world, x, y, z - 1))
				reservoir = (TileEntityReservoir) world.getTileEntity(x,
						y, z - 1);
			break;
		case 4:
			// x+1
			if (Reservoir.isRes(world, x + 1, y, z))
				reservoir = (TileEntityReservoir) world.getTileEntity(
						x + 1, y, z);
			break;
		}
		return reservoir;
	}

	public double getWater() {
		TileEntityReservoir pair = getWater(worldObj, xCoord, yCoord, zCoord);
		return pair.getWater();
	}

	protected double setOutput(World world, int x, int y, int z) {
		//calSpeed();
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
			LogHelper.debugLog("water=" + pair.getWater());
			LogHelper.debugLog("maxuse=" + pair.type.maxUse);
			double use = Math.min(pair.getWater(), pair.type.maxUse);
			LogHelper.debugLog("use=" + use);
			double baseEnergy = use * type.percent / 2048;
			LogHelper.debugLog("baseEnergy" + baseEnergy);
			LogHelper.debugLog("speed" + speed);
			double per = tickRotor();
			LogHelper.debugLog("per=" + per);
			if (per > 0) {
				double energy = baseEnergy * per; // * ((double) speed / 50);
				LogHelper.debugLog("energy = " + energy);
				if (energy > 0) {
					pair.useWater((int) use);
					damageRotor(1);
				}
				LogHelper.debugLog("use=" + use);
				return energy;
			}
		}
		return 0;
	}

	private boolean hasRotorImpl() {
		return !invSlots.isEmpty() && invSlots.get(0) != null
				&& !invSlots.get(0).isEmpty() && invSlots.get(0).get(0) != null
				&& invSlots.get(0).get(0).getItem() instanceof ItemRotor;
	}

	public boolean hasRotor() {
		return hasRotorImpl();
	}

	private ItemRotor getRotor() {
		return (ItemRotor) invSlots.get(0).get(0).getItem();
	}

	private void damageRotor(int tick) {
		ItemRotor rotor = getRotor();
		rotor.tickRotor(invSlots.get(0).get(0), this, worldObj);
		if (!rotor.type.isInfinite()) {
			if (invSlots.get(0).get(0).getItemDamage() + tick > invSlots.get(0)
					.get(0).getMaxDamage()) {
				invSlots.set(0, null);
			} else {
				int damage = invSlots.get(0).get(0).getItemDamage() + tick;
				invSlots.get(0).get(0).setItemDamage(damage);
			}
			markDirty();
		}
	}

	private void calSpeed() {
		TileEntityReservoir r = getWater(worldObj, xCoord, yCoord, zCoord);
		if (r != null && r.getWater() > 0 && hasRotor()) {
			// canOutput = true;
			speed++;
			if (speed > 50)
				speed = 50;
		} else {
			speed--;
			if (speed < 0)
				speed = 0;
		}
		if (speed > 0)
			damageRotor(1);
	}

	private double tickRotor() {
		if (!Reference.watermillNeedRotor)
			return 1;
		if (hasRotor()) {
			ItemRotor rotor = getRotor();
			if (worldObj.isRemote) {
				return rotor.type.efficiency;
			}

			return rotor.type.efficiency;
		}
		return 0;
	}

	@Override
	public String getInventoryName() {
		return type.getShowedName();
	}

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntityTurbine").id;
	}

	@Override
	public void readPacketData(NBTTagCompound tag) {
		super.readPacketData(tag);
		speed = tag.getInteger("speed");
		type = TurbineType.values()[tag.getInteger("type")];
	}

	@Override
	public void writePacketData(NBTTagCompound tag) {
		super.writePacketData(tag);
		if(type == null) tag.setInteger("type", 0);
		else tag.setInteger("speed", speed);
	}

	@Override
	protected void onUpdateClientAndServer() {
	}

}