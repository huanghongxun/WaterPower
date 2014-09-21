package org.jackhuang.watercraft.common.tileentity;

import factorization.api.Charge;
import factorization.api.Coord;
import factorization.api.IChargeConductor;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IKineticSource;

import java.util.Random;

import org.jackhuang.watercraft.WaterCraft;
import org.jackhuang.watercraft.client.gui.IHasGui;
import org.jackhuang.watercraft.common.EnergyType;
import org.jackhuang.watercraft.util.mods.Mods;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import buildcraft.api.mj.IBatteryObject;
import buildcraft.api.mj.MjAPI;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

@InterfaceList({
	@Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = Mods.IDs.IndustrialCraft2API, striprefs = true),
	@Interface(iface = "ic2.api.energy.tile.IKineticSource",  modid = Mods.IDs.IndustrialCraft2API, striprefs = true),
	@Interface(iface = "cofh.api.energy.IEnergyConnection", modid = Mods.IDs.CoFHAPIEnergy),
	@Interface(iface = "buildcraft.api.power.IPowerEmitter", modid = Mods.IDs.BuildCraftPower),
	@Interface(iface = "factorization.api.IChargeConductor", modid = Mods.IDs.Factorization)
})
public abstract class TileEntityBaseGenerator extends TileEntityBlock implements
		IEnergySource, IHasGui, IKineticSource, IUnitChangeable, IPowerEmitter,
		IEnergyConnection, IChargeConductor, IFluidHandler {
	public static Random random = new Random();

	public double storage = 0.0D;
	public double maxStorage;
	public double lastestOutput = 0;
	public int production;
	public boolean addedToEnergyNet = false;
	public EnergyType energyType = EnergyType.EU;

	private int tick;

	private boolean deadCache;
	
	private Object charge;

	public TileEntityBaseGenerator() {
		super(0);
	}

	public TileEntityBaseGenerator(int production, float maxStorage) {
		super(Math.round(maxStorage / 10));
		this.production = production;
		this.maxStorage = maxStorage;
		tick = WaterCraft.updateTick;
		
		if(Mods.Factorization.isAvailable)
			initCharge();
	}
	
	@Method(modid = Mods.IDs.Factorization)
	public void initCharge() {
		charge = new Charge(this);
	}
	
	@Method(modid = "IC2API")
	public void loadEnergyTile() {

		if (WaterCraft.isSimulating()) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

			this.addedToEnergyNet = true;
		}
	}
	
	@Method(modid = "IC2API")
	public void unloadEnergyTile() {
		if ((WaterCraft.isSimulating()) && (this.addedToEnergyNet)) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

			this.addedToEnergyNet = false;
		}
	}

	@Override
	public void validate() {
		super.validate();

		if(Mods.IndustrialCraft2.isAvailable)
			loadEnergyTile();
		
		// BuildCraft & Thermal Expansion integration begins
		deadCache = true;
		this.handlerCache = null;
	}

	@Override
	public void invalidate() {
		if(Mods.IndustrialCraft2.isAvailable)
			unloadEnergyTile();

		super.invalidate();
	}

	@Override
	@Method(modid = "IC2API")
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return energyType == EnergyType.EU;
	}

	public boolean enableUpdateEntity() {
		return WaterCraft.isSimulating();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.storage = nbttagcompound.getDouble("storage");
		
		if(Mods.Factorization.isAvailable && charge != null)
			((Charge) charge).readFromNBT(nbttagcompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setDouble("storage", this.storage);
		
		if(Mods.Factorization.isAvailable && charge != null)
			((Charge) charge).writeToNBT(nbttagcompound);
	}

	@Override
	@Method(modid = "IC2API")
	public double getOfferedEnergy() {
		if (energyType == EnergyType.EU)
			return Math.min(this.getProduction(), this.storage);
		return 0;
	}

	@Override
	@Method(modid = "IC2API")
	public void drawEnergy(double amount) {
		this.storage -= amount;
	}

	@Override
	@Method(modid = "IC2API")
	public float getWrenchDropRate() {
		return 1f;
	}

	protected abstract double computeOutput(World world, int x, int y, int z);

	protected int getProduction() {
		return production;
	}

	protected void onUpdate() {
	}

	protected void onUpdateClientAndServer() {}

	public void updateEntity() {
		super.updateEntity();

		if (this.storage > this.maxStorage)
			this.storage = this.maxStorage;

		onUpdateClientAndServer();

		if (worldObj.isRemote)
			return;
		
		if(Mods.Factorization.isAvailable && charge != null)
			((Charge) charge).update();
		
		pushFluidToConsumers(getFluidTankCapacity() / 40);

		if (!isRedstonePowered()) {
			lastestOutput = computeOutput(worldObj, xCoord, yCoord, zCoord);
			if (energyType == EnergyType.EU && Mods.IndustrialCraft2.isAvailable)
				storage += lastestOutput;
			if (energyType == EnergyType.RF && Mods.ThermalExpansion.isAvailable) {
				if (this.deadCache)
					reCache();
				storage += lastestOutput;
				int j = (int) Math.min(this.production, storage);
				storage -= j;
				storage += EnergyType.RF2EU(transmitEnergy((int)EnergyType.EU2RF(j)));
			}
			if (energyType == EnergyType.MJ && Mods.BuildCraftPower.isAvailable) {
				storage += lastestOutput;
				for(ForgeDirection d : ForgeDirection.values())
					sendPower(d);
			}
			if (energyType == EnergyType.FZ && Mods.Factorization.isAvailable) {
				((Charge)charge).setValue((int)EnergyType.EU2FZ(lastestOutput));
			}
			if (energyType == EnergyType.Water) {
				if(getFluidTank().getFluid().getFluid().getID() != FluidRegistry.WATER.getID())
					getFluidTank().setFluid(null);
				getFluidTank().fill(new FluidStack(FluidRegistry.WATER, (int)EnergyType.EU2Water(lastestOutput)), true);
			}
			if (energyType == EnergyType.Steam) {
				boolean outputed = false;
				if(FluidRegistry.isFluidRegistered("steam") && !outputed) {
					Fluid f = FluidRegistry.getFluid("steam");
					if(getFluidTank().getFluid().getFluid().getID() != f.getID())
						getFluidTank().setFluid(null);
					outputed = true;
					getFluidTank().fill(new FluidStack(f, (int)EnergyType.EU2Steam(lastestOutput)), true);
				}
				if(FluidRegistry.isFluidRegistered("ic2steam") && !outputed) {
					Fluid f = FluidRegistry.getFluid("ic2steam");
					if(getFluidTank().getFluid().getFluid().getID() != f.getID())
						getFluidTank().setFluid(null);
					outputed = true;
					getFluidTank().fill(new FluidStack(FluidRegistry.getFluid("ic2steam"), (int)EnergyType.EU2Steam(lastestOutput)), true);
				}
			}

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
		lastestOutput = tag.getDouble("lastestOutput");
	}

	@Override
	public void writePacketData(NBTTagCompound tag) {
		super.writePacketData(tag);
		tag.setDouble("storage", storage);
		tag.setInteger("production", production);
		tag.setDouble("lastestOutput", lastestOutput);
	}

	public boolean isEnergyFull() {
		return storage >= maxStorage;
	}

	@Override
	@Method(modid = "IC2API")
	public int getSourceTier() {
		return 1;
	}

	@Override
	@Method(modid = "IC2API")
	public int maxrequestkineticenergyTick(ForgeDirection directionFrom) {
		if (energyType == EnergyType.KU)
			return (int) (EnergyType.EU2KU(lastestOutput));
		return 0;
	}

	@Override
	@Method(modid = "IC2API")
	public int requestkineticenergy(ForgeDirection directionFrom,
			int requestkineticenergy) {
		if (energyType == EnergyType.KU)
			return Math.min(requestkineticenergy,
					maxrequestkineticenergyTick(directionFrom));
		return 0;
	}

	@Override
	public void setUnitId(int id) {
		energyType = EnergyType.values()[id];
	}

	@Override
	@Method(modid = "BuildCraftAPI|power")
	public boolean canEmitPowerFrom(ForgeDirection side) {
		return true;
	}

	@Override
	@Method(modid = "CoFHAPI|energy")
	public boolean canConnectEnergy(ForgeDirection paramForgeDirection) {
		return true;
	}

	private IEnergyHandler[] handlerCache;

	@Method(modid = "CoFHAPI|energy")
	protected final int transmitEnergy(int paramInt) {
		int i;
		if (this.handlerCache != null) {
			for (i = this.handlerCache.length; i-- > 0;) {
				IEnergyHandler localIEnergyHandler = this.handlerCache[i];
				if (localIEnergyHandler == null) {
					continue;
				}
				ForgeDirection localForgeDirection = ForgeDirection.VALID_DIRECTIONS[i];
				if (localIEnergyHandler.receiveEnergy(localForgeDirection,
						paramInt, true) > 0)
					paramInt -= localIEnergyHandler.receiveEnergy(
							localForgeDirection, paramInt, false);
				if (paramInt <= 0)
					return 0;
			}
		}
		return paramInt;
	}

	@Method(modid = "CoFHAPI|energy")
	private void reCache() {
		if (this.deadCache) {
			for (ForgeDirection localForgeDirection : ForgeDirection.VALID_DIRECTIONS) {
				onNeighborTileChange(this.xCoord + localForgeDirection.offsetX,
						this.yCoord + localForgeDirection.offsetY, this.zCoord
								+ localForgeDirection.offsetZ);
			}
			this.deadCache = false;
		}
	}

	@Override
	@Method(modid = "CoFHAPI|energy")
	public void onNeighborTileChange(int paramInt1, int paramInt2, int paramInt3) {
		TileEntity localTileEntity = this.worldObj.getTileEntity(paramInt1,
				paramInt2, paramInt3);

		if (paramInt1 < this.xCoord)
			addCache(localTileEntity, 5);
		else if (paramInt1 > this.xCoord)
			addCache(localTileEntity, 4);
		else if (paramInt3 < this.yCoord)
			addCache(localTileEntity, 3);
		else if (paramInt3 > this.yCoord)
			addCache(localTileEntity, 2);
		else if (paramInt2 < this.zCoord)
			addCache(localTileEntity, 1);
		else if (paramInt2 > this.zCoord)
			addCache(localTileEntity, 0);
	}

	@Method(modid = "BuildCraftAPI|power")
	public boolean isPoweredTile(TileEntity tile, ForgeDirection side, ForgeDirection orientation) {
		if (tile == null) {
			return false;
		} else if (MjAPI.getMjBattery(tile, MjAPI.DEFAULT_POWER_FRAMEWORK, orientation.getOpposite()) != null) {
			return true;
		} else if (tile instanceof IPowerReceptor) {
			return ((IPowerReceptor) tile).getPowerReceiver(side.getOpposite()) != null;
		} else {
			return false;
		}
	}

	@Method(modid = "BuildCraftAPI|power")
	public double extractEnergy(double min, double max, boolean doExtract) {
		double energy = EnergyType.EU2MJ(storage);
		if (energy < min) {
			return 0;
		}

		double actualMax;

		if (max > EnergyType.EU2MJ(production)) {
			actualMax = EnergyType.EU2MJ(production);
		} else {
			actualMax = max;
		}

		if (actualMax < min) {
			return 0;
		}

		double extracted;

		if (energy >= actualMax) {
			extracted = actualMax;

			if (doExtract) {
				energy -= actualMax;
			}
		} else {
			extracted = energy;

			if (doExtract) {
				energy = 0;
			}
		}
		storage = EnergyType.MJ2EU(energy);

		return extracted;
	}

	@Method(modid = "BuildCraftAPI|power")
	private double getPowerToExtract(TileEntity tile, ForgeDirection orientation) {

		IBatteryObject battery = MjAPI.getMjBattery(tile, MjAPI.DEFAULT_POWER_FRAMEWORK, orientation.getOpposite());

		if (battery != null) {
			return extractEnergy(0, battery.getEnergyRequested(), false);
		} else if (tile instanceof IPowerReceptor) {
			PowerReceiver receptor = ((IPowerReceptor) tile)
					.getPowerReceiver(orientation.getOpposite());

			return extractEnergy(receptor.getMinEnergyReceived(),
					receptor.getMaxEnergyReceived(), false);
		} else {
			return 0;
		}
	}

	@Method(modid = "BuildCraftAPI|power")
	private void sendPower(ForgeDirection orientation) {
		TileEntity tile = worldObj.getTileEntity(xCoord + orientation.offsetX, yCoord + orientation.offsetY, zCoord + orientation.offsetZ);
		if (isPoweredTile(tile, orientation, orientation)) {
			double extracted = getPowerToExtract(tile, orientation);

			IBatteryObject battery = MjAPI.getMjBattery(tile, MjAPI.DEFAULT_POWER_FRAMEWORK, orientation.getOpposite());

			if (battery != null) {
				battery.addEnergy(extractEnergy(0, battery.maxReceivedPerCycle(),
						true));
			} else if (tile instanceof IPowerReceptor) {
				PowerReceiver receptor = ((IPowerReceptor) tile)
						.getPowerReceiver(orientation.getOpposite());

				if (extracted > 0) {
					double needed = receptor.receiveEnergy(
							PowerHandler.Type.ENGINE, extracted,
							orientation.getOpposite());

					extractEnergy(receptor.getMinEnergyReceived(), needed, true);
				}
			}
		}
	}

	@Method(modid = "CoFHAPI|energy")
	private void addCache(TileEntity paramTileEntity, int paramInt) {
		if (this.handlerCache != null) {
			this.handlerCache[paramInt] = null;
		}
		if ((paramTileEntity instanceof IEnergyHandler)) {
			if (((IEnergyHandler) paramTileEntity)
					.canConnectEnergy(ForgeDirection.VALID_DIRECTIONS[paramInt])) {
				if (this.handlerCache == null)
					this.handlerCache = new IEnergyHandler[6];
				this.handlerCache[paramInt] = ((IEnergyHandler) paramTileEntity);
			}
		}
	}
	
	@Override
	public boolean isActive() {
		return !isRedstonePowered();
	}
	
	@Override
	@Method(modid = Mods.IDs.Factorization)
	public Charge getCharge() {
		return (Charge) charge;
	}
	
	@Override
	@Method(modid = Mods.IDs.Factorization)
	public Coord getCoord() {
		return new Coord(this);
	}
	
	@Override
	@Method(modid = Mods.IDs.Factorization)
	public String getInfo() {
		return "Stored/EU: " + storage + "\nProduction/EU: " + getProduction();
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public boolean canFill(ForgeDirection paramForgeDirection, Fluid paramFluid) {
		return false;
	}
	
	@Override
	public boolean canDrain(ForgeDirection paramForgeDirection, Fluid paramFluid) {
		return true;
	}

}