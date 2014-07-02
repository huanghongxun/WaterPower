package org.jackhuang.watercraft.common.tileentity;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;

import java.util.Random;

import org.jackhuang.watercraft.WaterCraft;
import org.jackhuang.watercraft.client.gui.IHasGui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileEntityBaseGenerator extends TileEntityBlock implements
		IEnergySource, IHasGui {
	public static Random random = new Random();

	public double storage = 0.0D;
	public double maxStorage;
	public int production;
	public boolean addedToEnergyNet = false;

	private int tick;

	private boolean deadCache;
	
	public TileEntityBaseGenerator() {
	}

	public TileEntityBaseGenerator(int production, double maxStorage) {
		this.production = production;
		this.maxStorage = maxStorage;
		tick = WaterCraft.updateTick;
	}

	@Override
	public void validate() {
		super.validate();

		if (WaterCraft.isSimulating()) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

			this.addedToEnergyNet = true;
		}
	}

	@Override
	public void invalidate() {
		if ((WaterCraft.isSimulating()) && (this.addedToEnergyNet)) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

			this.addedToEnergyNet = false;
		}

		super.invalidate();
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return true;
	}

	public boolean enableUpdateEntity() {
		return WaterCraft.isSimulating();
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.storage = nbttagcompound.getDouble("storage");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setDouble("storage", this.storage);
	}

	public double getOfferedEnergy() {
		return Math.min(this.getProduction(), this.storage);
	}

	public void drawEnergy(double amount) {
		this.storage -= amount;
	}

	public float getWrenchDropRate() {
		return 1f;
	}

	protected abstract double setOutput(World world, int x, int y, int z);
	
	protected int getProduction() {
		return production;
	}

	protected void onUpdate() {
	}

	protected abstract void onUpdateClientAndServer();

	public void updateEntity() {
		super.updateEntity();

		if (this.storage > this.maxStorage)
			this.storage = this.maxStorage;
		
		onUpdateClientAndServer();

		if (worldObj.isRemote)
			return;
		
		if(!isRedstonePowered()) {
			storage += setOutput(worldObj, xCoord, yCoord, zCoord);
			
			if (tick-- == 0) {
				onUpdate();
				tick = WaterCraft.updateTick;
	
				sendUpdateToClient();
			}
		}
	}

	@Override
	public void readPacketData(NBTTagCompound tag) {
		super.readPacketData(tag);
		production = tag.getInteger("production");
		storage = tag.getDouble("storage");
	}

	@Override
	public void writePacketData(NBTTagCompound tag) {
		super.writePacketData(tag);
		tag.setDouble("storage", storage);
		tag.setInteger("production", production);
	}
	
	public boolean isEnergyFull() {
		return storage >= maxStorage;
	}

}