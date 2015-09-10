package org.jackhuang.watercraft.common.block.tileentity;

import factorization.api.Charge;
import factorization.api.Coord;
import factorization.api.IChargeConductor;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IHeatSource;
import ic2.api.energy.tile.IKineticSource;

import java.util.Random;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.client.gui.IHasGui;
import org.jackhuang.watercraft.common.EnergyType;
import org.jackhuang.watercraft.common.network.MessagePacketHandler;
import org.jackhuang.watercraft.common.network.PacketUnitChanged;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.Utils;

/*import buildcraft.api.mj.IBatteryObject;
 import buildcraft.api.mj.MjAPI;
 import buildcraft.api.power.IPowerEmitter;
 import buildcraft.api.power.IPowerReceptor;
 import buildcraft.api.power.PowerHandler;
 import buildcraft.api.power.PowerHandler.PowerReceiver;*/
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
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

@InterfaceList({ @Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = Mods.IDs.IndustrialCraft2API, striprefs = true), @Interface(iface = "ic2.api.energy.tile.IKineticSource", modid = Mods.IDs.IndustrialCraft2API, striprefs = true), @Interface(iface = "ic2.api.energy.tile.IHeatSource", modid = Mods.IDs.IndustrialCraft2API, striprefs = true), @Interface(iface = "cofh.api.energy.IEnergyConnection", modid = Mods.IDs.CoFHAPIEnergy), @Interface(iface = "factorization.api.IChargeConductor", modid = Mods.IDs.Factorization) })
public abstract class TileEntityGenerator extends TileEntityBlock implements IEnergySource, IHasGui, IKineticSource, IUnitChangeable, IEnergyConnection, IChargeConductor, IFluidHandler, IHeatSource {
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

        // RF INTEGRATION
        deadCache = true;
        handlerCache = null;

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
        this.energyType = EnergyType.values()[nbttagcompound.getInteger("energyType")];

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
            if (energyType == EnergyType.EU && Mods.IndustrialCraft2.isAvailable)
                storage += latestOutput;
            if (energyType == EnergyType.RF && Mods.CoFHAPIEnergy.isAvailable) {
                reCache();
                storage += latestOutput;
                int j = (int) Math.min(this.production, storage);
                storage -= j;
                storage += EnergyType.RF2EU(transmitEnergy((int) EnergyType.EU2RF(j)));
            }
            /*
             * if (energyType == EnergyType.MJ &&
             * Mods.BuildCraftPower.isAvailable) { storage += latestOutput;
             * for(ForgeDirection d : ForgeDirection.values()) sendPower(d); }
             */
            if (energyType == EnergyType.Charge && Mods.Factorization.isAvailable) {
                ((Charge) charge).setValue((int) EnergyType.EU2Charge(latestOutput));
            }
            if (energyType == EnergyType.Water) {
                if (getFluidID() != FluidRegistry.WATER.getID())
                    getFluidTank().setFluid(null);
                getFluidTank().fill(new FluidStack(FluidRegistry.WATER, (int) EnergyType.EU2Water(latestOutput)), true);
            }
            if (energyType == EnergyType.Steam) {
                boolean outputed = false;
                if (FluidRegistry.isFluidRegistered("steam") && !outputed) {
                    Fluid f = FluidRegistry.getFluid("steam");
                    if (getFluidID() != f.getID())
                        getFluidTank().setFluid(null);
                    outputed = true;
                    getFluidTank().fill(new FluidStack(f, (int) EnergyType.EU2Steam(latestOutput)), true);
                }
                if (FluidRegistry.isFluidRegistered("ic2steam") && !outputed) {
                    Fluid f = FluidRegistry.getFluid("ic2steam");
                    if (getFluidID() != f.getID())
                        getFluidTank().setFluid(null);
                    outputed = true;
                    getFluidTank().fill(new FluidStack(FluidRegistry.getFluid("ic2steam"), (int) EnergyType.EU2Steam(latestOutput)), true);
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

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public int getSourceTier() {
        return 1;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public int maxrequestHeatTick(ForgeDirection directionFrom) {
        if (energyType == EnergyType.HU)
            return (int) (EnergyType.EU2HU(latestOutput));
        return 0;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public int requestHeat(ForgeDirection directionFrom, int requestheat) {
        if (energyType == EnergyType.HU)
            return Math.min(requestheat, maxrequestHeatTick(directionFrom));
        return 0;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public int maxrequestkineticenergyTick(ForgeDirection directionFrom) {
        if (energyType == EnergyType.KU)
            return (int) (EnergyType.EU2KU(latestOutput));
        return 0;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public int requestkineticenergy(ForgeDirection directionFrom, int requestkineticenergy) {
        if (energyType == EnergyType.KU)
            return Math.min(requestkineticenergy, maxrequestkineticenergyTick(directionFrom));
        return 0;
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
        if (EnergyType.values()[id] != EnergyType.EU && Mods.IndustrialCraft2.isAvailable) {
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

    private Object[] handlerCache;
    private boolean deadCache = true;

    @Override
    @Method(modid = Mods.IDs.CoFHAPIEnergy)
    public boolean canConnectEnergy(ForgeDirection d) {
        return true;
    }

    @Method(modid = Mods.IDs.CoFHAPIEnergy)
    protected final int transmitEnergy(int e) {
        if (this.handlerCache != null) {
            for (int i = this.handlerCache.length; i-- > 0;) {
                IEnergyHandler h = (IEnergyHandler) this.handlerCache[i];
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

    @Method(modid = Mods.IDs.CoFHAPIEnergy)
    private void reCache() {
        if (this.deadCache) {
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                onNeighborTileChange(this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
            }
            this.deadCache = false;
        }
    }

    @Override
    @Method(modid = Mods.IDs.CoFHAPIEnergy)
    public void onNeighborTileChange(int x, int y, int z) {
        TileEntity t = this.worldObj.getTileEntity(x, y, z);

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

    @Method(modid = Mods.IDs.CoFHAPIEnergy)
    private void addCache(TileEntity t, int side) {
        if (this.handlerCache != null) {
            this.handlerCache[side] = null;
        }
        if ((t instanceof IEnergyHandler)) {
            if (((IEnergyHandler) t).canConnectEnergy(ForgeDirection.VALID_DIRECTIONS[side])) {
                if (this.handlerCache == null)
                    this.handlerCache = new IEnergyHandler[6];
                this.handlerCache[side] = ((IEnergyHandler) t);
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
        sb.append(StatCollector.translateToLocal("cptwtrml.gui.latest_output") + "/" + energyType.name() + ": " + Utils.DEFAULT_DECIMAL_FORMAT.format(getFromEU(latestOutput)) + "\n");
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
        MessagePacketHandler.INSTANCE.sendToServer(new PacketUnitChanged(Minecraft.getMinecraft().thePlayer.worldObj.provider.dimensionId, xCoord, yCoord, zCoord, energyType.ordinal()));
    }
}