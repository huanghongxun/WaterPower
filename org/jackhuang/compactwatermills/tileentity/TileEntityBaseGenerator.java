package org.jackhuang.compactwatermills.tileentity;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;

import java.util.Random;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.EnergyType;
import org.jackhuang.compactwatermills.gui.IHasGUI;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

public abstract class TileEntityBaseGenerator extends TileEntityBlock implements
		IEnergySource, IHasGUI, IEnergyHandler, IPowerEmitter {
	public static Random random = new Random();

	public double storage = 0.0D;
	public final double maxStorage;
	public int production;
	// private boolean initialized = false;
	public boolean addedToEnergyNet = false;

	private int tick;

	private IEnergyHandler[] energyHandlerCache;
	private IPowerReceptor[] powerReceptorCache;
	private boolean deadCache;

	public TileEntityBaseGenerator(int production, double maxStorage) {
		this.production = production;
		this.maxStorage = maxStorage;
		tick = CompactWatermills.updateTick;
	}

	@Override
	public void validate() {
		super.validate();
		this.deadCache = true;
		this.energyHandlerCache = new IEnergyHandler[6];
		this.powerReceptorCache = new IPowerReceptor[6];
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
		if (energyType() == EnergyType.EU)
			return Math.min(this.production, this.storage);
		else
			return 0;
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

	protected void onUpdate() {
	}

	protected abstract EnergyType energyType();

	public void updateEntity() {
		super.updateEntity();
		/*
		 * if (!initialized && worldObj != null) { if (worldObj.isRemote) {
		 * NetworkHelper.updateTileEntityField(this, "facing"); } else {
		 * EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
		 * MinecraftForge.EVENT_BUS.post(loadEvent); } initialized = true; }
		 */

		if (this.deadCache) {
			for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
				onNeighborTileEntityChange(xCoord + direction.offsetX, yCoord
						+ direction.offsetY, zCoord + direction.offsetZ);
			}
			this.deadCache = false;
		}

		if (this.storage > this.maxStorage)
			this.storage = this.maxStorage;

		if (worldObj.isRemote)
			return;

		if (tick-- == 0) {
			onUpdate();
			tick = CompactWatermills.updateTick;
			storage += setOutput(worldObj, xCoord, yCoord, zCoord) * tick;
			
			if(energyType() == EnergyType.MJ || energyType() == EnergyType.RF) {
				double j = Math.min(this.production, this.storage);
				storage -= j;
				storage += producePower((int) (j / 5 * 2)) / 2 * 5;
			}

			sendUpdateToClient();
		}
	}

	public void onNeighborTileEntityChange(int par1, int par2, int par3) {
		TileEntity te = this.worldObj.getBlockTileEntity(par1, par2, par3);
		if (par1 < xCoord)
			addCache(te, 4);
		else if (par1 > xCoord)
			addCache(te, 5);
		else if (par3 < zCoord)
			addCache(te, 2);
		else if (par3 > zCoord)
			addCache(te, 3);
		else if (par2 < yCoord)
			addCache(te, 0);
		else if (par2 > yCoord)
			addCache(te, 1);
	}

	private void addCache(TileEntity tileEntity, int par) {
		if (tileEntity instanceof IEnergyHandler) {
			this.energyHandlerCache[par] = (IEnergyHandler) tileEntity;
		} else if (tileEntity instanceof IPowerReceptor) {
			this.powerReceptorCache[par] = (IPowerReceptor) tileEntity;
		} else {
			this.energyHandlerCache[par] = null;
			this.powerReceptorCache[par] = null;
		}
	}

	protected final int producePower(int paramInt) {
		for (int i = this.energyHandlerCache.length; i-- > 0;) {
			IEnergyHandler localIEnergyHandler = this.energyHandlerCache[i];
			if (localIEnergyHandler == null) {
				continue;
			}
			ForgeDirection localForgeDirection1 = ForgeDirection.VALID_DIRECTIONS[i];
			if (localIEnergyHandler.canInterface(localForgeDirection1)) {
				if (localIEnergyHandler.receiveEnergy(localForgeDirection1,
						paramInt, true) > 0)
					paramInt -= localIEnergyHandler.receiveEnergy(
							localForgeDirection1, paramInt, false);
				if (paramInt <= 0) {
					return 0;
				}
			}
		}
		float f1 = paramInt / 10.0F;
		float f2 = f1;

		for (int j = this.powerReceptorCache.length; j-- > 0;) {
			IPowerReceptor localIPowerReceptor = this.powerReceptorCache[j];
			if (localIPowerReceptor == null) {
				continue;
			}
			ForgeDirection localForgeDirection2 = ForgeDirection.VALID_DIRECTIONS[j];
			PowerHandler.PowerReceiver localPowerReceiver = localIPowerReceptor
					.getPowerReceiver(localForgeDirection2);
			float f3;
			if ((localPowerReceiver != null)
					&& (Math.min(
							f3 = localPowerReceiver.getMaxEnergyReceived(),
							localPowerReceiver.getMaxEnergyStored()
									- localPowerReceiver.getEnergyStored()) > 0.0F)) {
				float f4 = Math.min(Math.min(f3, f2),
						localPowerReceiver.getMaxEnergyStored()
								- localPowerReceiver.getEnergyStored());

				localPowerReceiver.receiveEnergy(PowerHandler.Type.GATE, f4,
						localForgeDirection2);

				f2 -= f4;
				if (f2 <= 0.0F) {
					return 0;
				}
			}
		}
		paramInt = (int) (paramInt - (f1 - f2));

		return paramInt;
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

	@Override
	public int extractEnergy(ForgeDirection paramForgeDirection, int paramInt,
			boolean paramBoolean) {
		if (energyType() == EnergyType.RF) {
			int returns = (int) (storage * 5);
			storage = 0;
			return returns;
		} else
			return 0;
	}

	@Override
	public int receiveEnergy(ForgeDirection paramForgeDirection, int paramInt,
			boolean paramBoolean) {
		return 0;
	}

	@Override
	public boolean canInterface(ForgeDirection paramForgeDirection) {
		return true;
	}

	@Override
	public boolean canEmitPowerFrom(ForgeDirection side) {
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection paramForgeDirection) {
		return (int) storage * 5;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection paramForgeDirection) {
		return (int) maxStorage * 5;
	}

}