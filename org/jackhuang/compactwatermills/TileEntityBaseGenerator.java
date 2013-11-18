package org.jackhuang.compactwatermills;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

public abstract class TileEntityBaseGenerator extends TileEntityBlock
		implements IEnergySource, IHasGUI {
	public static Random random = new Random();

	public double storage = 0.0D;
	public final double maxStorage;
	public int production;
	// private boolean initialized = false;
	public boolean addedToEnergyNet = false;

	private int tick;

	public TileEntityBaseGenerator(int production, double maxStorage) {
		this.production = production;
		this.maxStorage = maxStorage;
		tick = CompactWatermills.updateTick;
	}

	@Override
	public void onLoaded() {
		super.onLoaded();

		if (CompactWatermills.isSimulating()) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

			this.addedToEnergyNet = true;
		}
	}

	@Override
	public void onUnloaded() {
		if ((CompactWatermills.isSimulating()) && (this.addedToEnergyNet)) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

			this.addedToEnergyNet = false;
		}

		super.onUnloaded();
	}

	public boolean enableUpdateEntity() {
		return CompactWatermills.isSimulating();
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.storage = nbttagcompound.getDouble("storage");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setDouble("storage", this.storage);
	}

	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return true;
	}

	public double getOfferedEnergy() {
		return Math.min(this.production, this.storage);
	}

	public void drawEnergy(double amount) {
		this.storage -= amount;
	}

	public abstract String getInvName();

	public String getOperationSoundFile() {
		return null;
	}

	public float getWrenchDropRate() {
		return 1f;
	}

	protected abstract double setOutput(World world, int x, int y, int z);
	protected void onUpdate(){}

	public void updateEntity() {
		super.updateEntity();
		/*
		 * if (!initialized && worldObj != null) { if (worldObj.isRemote) {
		 * NetworkHelper.updateTileEntityField(this, "facing"); } else {
		 * EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
		 * MinecraftForge.EVENT_BUS.post(loadEvent); } initialized = true; }
		 */
		if (this.storage > this.maxStorage)
			this.storage = this.maxStorage;
		
		if(worldObj.isRemote) return;

		if (tick-- == 0) {
			onUpdate();
			tick = CompactWatermills.updateTick;
			storage += setOutput(worldObj, xCoord, yCoord, zCoord) * tick;

			sendUpdateToClient();
		}
	}
	
	@Override
	public void readPacketData(NBTTagCompound tag) {
		super.readPacketData(tag);
		storage = tag.getDouble("storage");
	}
	
	@Override
	public void writePacketData(NBTTagCompound tag) {
		super.writePacketData(tag);
		tag.setDouble("storage", storage);
	}
}