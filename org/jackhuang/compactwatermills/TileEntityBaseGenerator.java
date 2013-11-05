package org.jackhuang.compactwatermills;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.NetworkHelper;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

public abstract class TileEntityBaseGenerator extends TileEntityInventory
		implements IEnergySource, IHasGUI {
	public static Random random = new Random();

	public double storage = 0.0D;
	public final short maxStorage;
	public int production;
	private boolean initialized = false;

	private int tick;

	public TileEntityBaseGenerator(int production, int maxStorage) {
		this.production = production;
		this.maxStorage = (short) maxStorage;
		tick = CompactWatermills.updateTick;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		try {
			this.storage = nbttagcompound.getDouble("storage");
		} catch (Exception e) {
			this.storage = nbttagcompound.getShort("storage");
		}
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

	public boolean delayActiveUpdate() {
		return false;
	}

	public void onGuiClosed(EntityPlayer entityPlayer) {
	}

	public float getWrenchDropRate() {
		return 1f;
	}
	
	protected abstract double setOutput(World world, int x, int y, int z);

	public void updateEntity() {
		super.updateEntity();
		
		if (!initialized && worldObj != null) {
			if (worldObj.isRemote) {
				NetworkHelper.updateTileEntityField(this, "facing");
			} else {
				EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
				MinecraftForge.EVENT_BUS.post(loadEvent);
			}
			initialized = true;
		}
		
		if (this.storage > this.maxStorage)
			this.storage = this.maxStorage;
		
		if (tick-- == 0) {
			storage += setOutput(worldObj, xCoord, yCoord, zCoord)
					* CompactWatermills.updateTick;
			tick = CompactWatermills.updateTick;
		}
	}
}