package org.jackhuang.watercraft.common.block.tileentity;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;

import java.util.Random;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.client.gui.IHasGui;
import org.jackhuang.watercraft.common.EnergyType;
import org.jackhuang.watercraft.common.network.PacketHandler;
import org.jackhuang.watercraft.common.network.WaterPowerPacket;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.Utils;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class TileEntityGenerator extends TileEntityBlock implements
		IEnergySource, IHasGui, IUnitChangeable, IFluidHandler, IPowerEmitter,
		IEnergyHandler {
	public static Random random = new Random();

	public double storage = 0.0D;
	public double maxStorage;
	public double latestOutput = 0;
	public int production;
	public boolean addedToEnergyNet = false;
	public EnergyType energyType = EnergyType.EU;
	public boolean needsToAddToEnergyNet = false;
	boolean sendInitDataGenerator = false;

	public TileEntityGenerator() {
		super(0);
	}

	public TileEntityGenerator(int production, float maxStorage) {
		super(Math.round(maxStorage / 10));
		this.production = production;
		this.maxStorage = maxStorage;
	}

	@Override
	public void onLoaded() {
		// EU INTEGRATION
		needsToAddToEnergyNet = true;

		// RF INTEGRATION
		deadCache = true;
		this.energyHandlerCache = new IEnergyHandler[6];
		this.powerReceptorCache = new IPowerReceptor[6];

		super.onLoaded();
	}

	@Override
	public void onUnloaded() {
		// EU INTEGRATION
		if (Mods.IndustrialCraft2.isAvailable)
			unloadEnergyTile();

		super.onUnloaded();
	}

	public boolean enableUpdateEntity() {
		return isServerSide();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.storage = nbttagcompound.getDouble("storage");
		this.energyType = EnergyType.values()[nbttagcompound
				.getInteger("energyType")];

		sendInitDataGenerator = true;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setDouble("storage", this.storage);
		nbttagcompound.setInteger("energyType", energyType.ordinal());
	}

	double preLatestOutput = -999;

	@Override
	public void readPacketData(NBTTagCompound tag) {
		super.readPacketData(tag);
		if (tag.hasKey("latestOutput"))
			latestOutput = tag.getDouble("latestOutput");
		if (tag.hasKey("sendInitDataGenerator")) {
			energyType = EnergyType.values()[tag.getInteger("energyType")];
		}
	}

	@Override
	public void writePacketData(NBTTagCompound tag) {
		super.writePacketData(tag);
		if (preLatestOutput != latestOutput) {
			tag.setDouble("latestOutput", latestOutput);
			preLatestOutput = latestOutput;
		}
		if (sendInitDataGenerator) {
			sendInitDataGenerator = false;
			tag.setBoolean("sendInitDataGenerator", true);
			tag.setInteger("energyType", energyType.ordinal());
		}
	}

	protected abstract double computeOutput(World world, int x, int y, int z);

	protected int getProduction() {
		return production;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!isServerSide())
			return;

		if (this.storage > this.maxStorage)
			this.storage = this.maxStorage;

		if (Mods.IndustrialCraft2.isAvailable)
			loadEnergyTile();

		pushFluidToConsumers(getFluidTankCapacity() / 40);

		if (!isRedstonePowered()) {
			latestOutput = computeOutput(worldObj, xCoord, yCoord, zCoord);
			if (energyType == EnergyType.EU
					&& Mods.IndustrialCraft2.isAvailable)
				storage += latestOutput;
			if (energyType == EnergyType.RF) {
				reCache();
				storage += latestOutput;
				int j = (int) Math.min(this.production, storage);
				storage -= j;
				storage += EnergyType.RF2EU(transmitEnergy((int) EnergyType
						.EU2RF(j)));
			}

			if (energyType == EnergyType.MJ) {
				reCache();
				storage += latestOutput;
				double j = Math.min(this.production, this.storage);
				storage -= j;
				storage += producePower(j);
			}

			if (energyType == EnergyType.Water) {
				if (getFluidID() != FluidRegistry.WATER.getID())
					getFluidTank().setFluid(null);
				getFluidTank().fill(
						new FluidStack(FluidRegistry.WATER,
								(int) EnergyType.EU2Water(latestOutput)), true);
			}
			if (energyType == EnergyType.Steam) {
				boolean outputed = false;
				if (FluidRegistry.isFluidRegistered("steam") && !outputed) {
					Fluid f = FluidRegistry.getFluid("steam");
					if (getFluidID() != f.getID())
						getFluidTank().setFluid(null);
					outputed = true;
					getFluidTank().fill(
							new FluidStack(f,
									(int) EnergyType.EU2Steam(latestOutput)),
							true);
				}
				if (FluidRegistry.isFluidRegistered("ic2steam") && !outputed) {
					Fluid f = FluidRegistry.getFluid("ic2steam");
					if (getFluidID() != f.getID())
						getFluidTank().setFluid(null);
					outputed = true;
					getFluidTank().fill(
							new FluidStack(FluidRegistry.getFluid("ic2steam"),
									(int) EnergyType.EU2Steam(latestOutput)),
							true);
				}
			}
		}
	}

	public boolean isEnergyFull() {
		return storage >= maxStorage;
	}

	/**
	 * ------------------------------------------------------
	 * 
	 * EU, HU, KU(INDUSTRIAL CRAFT 2 EXPERIMENTAL) INTEGRATION BEGINS
	 * 
	 * ------------------------------------------------------
	 */

	public void loadEnergyTile() {
		if (isServerSide() && this.needsToAddToEnergyNet) {
			needsToAddToEnergyNet = false;
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

			this.addedToEnergyNet = true;
		}
	}

	public void unloadEnergyTile() {
		if (isServerSide() && this.addedToEnergyNet) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

			this.addedToEnergyNet = false;
		}
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return energyType == EnergyType.EU;
	}

	@Override
	public double getOfferedEnergy() {
		if (energyType == EnergyType.EU)
			return Math.min(this.getProduction(), this.storage);
		return 0;
	}

	@Override
	public void drawEnergy(double amount) {
		this.storage -= amount;
	}

	/**
	 * ------------------------------------------------------
	 * 
	 * EU, HU, KU(INDUSTRIAL CRAFT 2 EXPERIMENTAL) INTEGRATION ENDS
	 * 
	 * ------------------------------------------------------
	 */

	@Override
	public void setUnitId(int id) {
		if (EnergyType.values()[id] != EnergyType.EU
				&& Mods.IndustrialCraft2.isAvailable) {
			unloadEnergyTile();
		}
		energyType = EnergyType.values()[id];
	}

	/**
	 * ------------------------------------------------------
	 * 
	 * MJ(BUILD CRAFT) INTEGRATION BEGINS
	 * 
	 * ------------------------------------------------------
	 */
	private IEnergyHandler[] energyHandlerCache;
	private IPowerReceptor[] powerReceptorCache;

	protected final double producePower(double paramInt) {
		for (int i = this.energyHandlerCache.length; i-- > 0;) {
			IEnergyHandler localIEnergyHandler = this.energyHandlerCache[i];
			if (localIEnergyHandler == null) {
				continue;
			}
			ForgeDirection localForgeDirection1 = ForgeDirection.VALID_DIRECTIONS[i];
			if (localIEnergyHandler.canInterface(localForgeDirection1)) {
				if (localIEnergyHandler.receiveEnergy(localForgeDirection1,
						(int) paramInt, true) > 0)
					paramInt -= EnergyType.RF2EU(localIEnergyHandler.receiveEnergy(
							localForgeDirection1, (int) EnergyType.EU2RF(paramInt), false));
				if (paramInt <= 0) {
					return 0;
				}
			}
		}
		float f1 = (float) EnergyType.EU2MJ(paramInt);
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

		return paramInt - EnergyType.MJ2EU(f1 - f2);
	}

	@Override
	public boolean canEmitPowerFrom(ForgeDirection side) {
		return true;
	}

	private boolean deadCache = true;

	protected final int transmitEnergy(int e) {
		if (this.energyHandlerCache != null) {
			for (int i = this.energyHandlerCache.length; i-- > 0;) {
				IEnergyHandler h = (IEnergyHandler) this.energyHandlerCache[i];
				if (h == null)
					continue;
				ForgeDirection d = ForgeDirection.VALID_DIRECTIONS[i];
				if (h.receiveEnergy(d, e, true) > 0)
					e -= h.receiveEnergy(d, e, false);
				if (e <= 0)
					return 0;
			}
		}
		return e;
	}

	private void reCache() {
		if (this.deadCache) {
			for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
				onNeighborTileChange(this.xCoord + d.offsetX, this.yCoord
						+ d.offsetY, this.zCoord + d.offsetZ);
			}
			this.deadCache = false;
		}
	}

	@Override
	public void onNeighborTileChange(int x, int y, int z) {
		TileEntity t = this.worldObj.getBlockTileEntity(x, y, z);

		if (x < this.xCoord)
			addCache(t, ForgeDirection.EAST.ordinal());
		else if (x > this.xCoord)
			addCache(t, ForgeDirection.WEST.ordinal());
		else if (y < this.yCoord)
			addCache(t, ForgeDirection.UP.ordinal());
		else if (y > this.yCoord)
			addCache(t, ForgeDirection.DOWN.ordinal());
		else if (z < this.zCoord)
			addCache(t, ForgeDirection.NORTH.ordinal());
		else if (z > this.zCoord)
			addCache(t, ForgeDirection.SOUTH.ordinal());
	}

	private void addCache(TileEntity tileEntity, int par) {
		this.energyHandlerCache[par] = null;
		this.powerReceptorCache[par] = null;
		if (tileEntity instanceof IEnergyHandler) {
			this.energyHandlerCache[par] = (IEnergyHandler) tileEntity;
		}
		if (tileEntity instanceof IPowerReceptor) {
			this.powerReceptorCache[par] = (IPowerReceptor) tileEntity;
		}
	}

	/**
	 * ------------------------------------------------------
	 * 
	 * RF(THERMAL EXPANSION) INTEGRATION ENDS
	 * 
	 * ------------------------------------------------------
	 */

	@Override
	public int getEnergyStored(ForgeDirection paramForgeDirection) {
		return (int) Math.round(EnergyType.EU2RF(storage));
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection paramForgeDirection) {
		return (int) Math.round(EnergyType.EU2RF(maxStorage));
	}

	@Override
	public boolean canInterface(ForgeDirection paramForgeDirection) {
		return true;
	}

	@Override
	public int extractEnergy(ForgeDirection paramForgeDirection,
			int maxExtract, boolean simulate) {
		double toSend = Math.min(storage,
				Math.min(production, EnergyType.RF2EU(maxExtract)));

		if (!simulate)
			storage -= toSend;

		return (int) Math.round(EnergyType.EU2RF(toSend));
	}

	@Override
	public int receiveEnergy(ForgeDirection paramForgeDirection,
			int maxReceive, boolean simulate) {
		double toAdd = Math.min(maxStorage - storage,
				EnergyType.RF2EU(maxReceive));

		if (!simulate)
			storage += toAdd;

		return (int) Math.round(EnergyType.EU2RF(toAdd));
	}

	/**
	 * ------------------------------------------------------
	 * 
	 * RF(Thermal Expansion) INTEGRATION ENDS
	 * 
	 * ------------------------------------------------------
	 */

	@Override
	public boolean isActive() {
		return !isRedstonePowered();
	}

	public double getFromEU(double eu) {
		return energyType.getFromEU(eu);
	}

	@Override
	public boolean canDrain(ForgeDirection paramForgeDirection, Fluid paramFluid) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void onUnitChanged(EnergyType t) {
		energyType = t;

		PacketDispatcher
				.sendPacketToServer(new WaterPowerPacket(
						0,
						null,
						new int[] {
								Minecraft.getMinecraft().thePlayer.worldObj.provider.dimensionId,
								xCoord, yCoord, zCoord, energyType.ordinal() })
						.getPacket());
	}
}