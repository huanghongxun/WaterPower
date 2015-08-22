package org.jackhuang.watercraft.common.block.tileentity;

import factorization.api.Charge;
import factorization.api.Coord;
import factorization.api.IChargeConductor;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;

import java.util.Random;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.client.gui.IHasGui;
import org.jackhuang.watercraft.common.EnergyType;
import org.jackhuang.watercraft.common.network.MessagePacketHandler;
import org.jackhuang.watercraft.common.network.PacketUnitChanged;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.Utils;

import buildcraft.api.mj.IBatteryObject;
import buildcraft.api.mj.MjAPI;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

@InterfaceList({
        @Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = Mods.IDs.IndustrialCraft2API, striprefs = true),
        @Interface(iface = "buildcraft.api.power.IPowerEmitter", modid = Mods.IDs.BuildCraftPower),
        @Interface(iface = "factorization.api.IChargeConductor", modid = Mods.IDs.Factorization) })
public abstract class TileEntityGenerator extends TileEntityBlock implements
        IEnergySource, IHasGui, IUnitChangeable, IPowerEmitter,
        IChargeConductor, IFluidHandler {
    public static Random random = new Random();

    public double storage = 0.0D;
    public double maxStorage;
    public double latestOutput = 0;
    public int production;
    public boolean addedToEnergyNet = false;
    public EnergyType energyType = EnergyType.EU;
    public boolean needsToAddToEnergyNet = false;
    boolean sendInitDataGenerator = false;

    private Object charge;

    public TileEntityGenerator() {
        super(0);
    }

    public TileEntityGenerator(int production, float maxStorage) {
        super(Math.round(maxStorage / 10));
        this.production = production;
        this.maxStorage = maxStorage;

        if (Mods.Factorization.isAvailable)
            initCharge();
    }

    @Override
    public void onLoaded() {
        // EU INTEGRATION
        needsToAddToEnergyNet = true;

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

        if (Mods.Factorization.isAvailable && charge != null)
            ((Charge) charge).readFromNBT(nbttagcompound);

        sendInitDataGenerator = true;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.setDouble("storage", this.storage);
        nbttagcompound.setInteger("energyType", energyType.ordinal());

        if (Mods.Factorization.isAvailable && charge != null)
            ((Charge) charge).writeToNBT(nbttagcompound);
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

        if (Mods.Factorization.isAvailable && charge != null)
            ((Charge) charge).update();

        pushFluidToConsumers(getFluidTankCapacity() / 40);

        if (!isRedstonePowered()) {
            latestOutput = computeOutput(worldObj, xCoord, yCoord, zCoord);
            if (energyType == EnergyType.EU
                    && Mods.IndustrialCraft2.isAvailable)
                storage += latestOutput;
            
             if (energyType == EnergyType.MJ &&
             Mods.BuildCraftPower.isAvailable) { storage += latestOutput;
             for(ForgeDirection d : ForgeDirection.values()) sendPower(d); }
             
            if (energyType == EnergyType.Charge
                    && Mods.Factorization.isAvailable) {
                ((Charge) charge).setValue((int) EnergyType
                        .EU2Charge(latestOutput));
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

    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public void loadEnergyTile() {

        if (isServerSide()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

            this.addedToEnergyNet = true;
        }
    }

    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public void unloadEnergyTile() {
        if (isServerSide() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

            this.addedToEnergyNet = false;
        }
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
        return energyType == EnergyType.EU;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public double getOfferedEnergy() {
        if (energyType == EnergyType.EU)
            return Math.min(this.getProduction(), this.storage);
        return 0;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
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
     * RF(THERMAL EXPANSION, BUILD CRAFT) INTEGRATION BEGINS
     * 
     * ------------------------------------------------------
     */
    @Method(modid = "BuildCraftAPI|power")
	public boolean canEmitPowerFrom(ForgeDirection side) {
		return true;
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

    /**
     * ------------------------------------------------------
     * 
     * RF(THERMAL EXPANSION, BUILD CRAFT) INTEGRATION ENDS
     * 
     * ------------------------------------------------------
     */

    @Override
    public boolean isActive() {
        return !isRedstonePowered();
    }

    /**
     * ------------------------------------------------------
     * 
     * CHARGE(FACTORIZATION) INTEGRATION BEGINS
     * 
     * ------------------------------------------------------
     */

    @Method(modid = Mods.IDs.Factorization)
    public void initCharge() {
        charge = new Charge(this);
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

    public double getFromEU(double eu) {
        return energyType.getFromEU(eu);
    }

    @Override
    @Method(modid = Mods.IDs.Factorization)
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(StatCollector.translateToLocal("cptwtrml.gui.latest_output")
                + "/" + energyType.name() + ": "
                + Utils.DEFAULT_DECIMAL_FORMAT.format(getFromEU(latestOutput))
                + "\n");
        return sb.toString();
    }

    /**
     * ------------------------------------------------------
     * 
     * CHARGE(FACTORIZATION) INTEGRATION ENDS
     * 
     * ------------------------------------------------------
     */

    @Override
    public boolean canDrain(ForgeDirection paramForgeDirection, Fluid paramFluid) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void onUnitChanged(EnergyType t) {
        energyType = t;
        MessagePacketHandler.INSTANCE
                .sendToServer(new PacketUnitChanged(
                        Minecraft.getMinecraft().thePlayer.worldObj.provider.dimensionId,
                        xCoord, yCoord, zCoord, energyType.ordinal()));
    }
}