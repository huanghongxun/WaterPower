package waterpower.common.block.tileentity;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IHeatSource;
import ic2.api.energy.tile.IKineticSource;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import waterpower.client.gui.IHasGui;
import waterpower.common.EnergyType;
import waterpower.common.network.MessagePacketHandler;
import waterpower.common.network.PacketUnitChanged;
import waterpower.util.Mods;
/*import buildcraft.api.mj.IBatteryObject;
 import buildcraft.api.mj.MjAPI;
 import buildcraft.api.power.IPowerEmitter;
 import buildcraft.api.power.IPowerReceptor;
 import buildcraft.api.power.PowerHandler;
 import buildcraft.api.power.PowerHandler.PowerReceiver;*/
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyReceiver;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@InterfaceList({ @Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = Mods.IDs.IndustrialCraft2API, striprefs = true),
        @Interface(iface = "ic2.api.energy.tile.IKineticSource", modid = Mods.IDs.IndustrialCraft2API, striprefs = true),
        @Interface(iface = "ic2.api.energy.tile.IHeatSource", modid = Mods.IDs.IndustrialCraft2API, striprefs = true),
        @Interface(iface = "cofh.api.energy.IEnergyConnection", modid = Mods.IDs.CoFHAPIEnergy),
        //@Interface(iface = "factorization.api.IChargeConductor", modid = Mods.IDs.Factorization),
        @Interface(iface = "thaumcraft.api.aspects.IEssentiaTransport", modid = Mods.IDs.Thaumcraft),
        @Interface(iface = "thaumcraft.api.aspects.IAspectContainer", modid = Mods.IDs.Thaumcraft) })
public abstract class TileEntityGenerator extends TileEntityBlock implements IEnergySource, IHasGui, IKineticSource, IUnitChangeable, IEnergyConnection,
        /*IChargeConductor, */IFluidHandler, IHeatSource/*, IEssentiaTransport, IAspectContainer*/ {
    public static final Random random = new Random();

    public double storage = 0.0D;
    public double maxStorage;
    public double latestOutput = 0;
    public int production;
    public boolean addedToEnergyNet = false;
    public EnergyType energyType = EnergyType.EU;
    public boolean needsToAddToEnergyNet = false;

    private Object charge;

    public TileEntityGenerator() {
        super(0);
    }

    public TileEntityGenerator(int production, float maxStorage) {
        super(Math.round(maxStorage / 10));
        this.production = production;
        this.maxStorage = maxStorage;

        //if (Mods.Factorization.isAvailable)
        //    initCharge();
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
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.storage = tag.getDouble("storage");
        this.energyType = EnergyType.values()[tag.getInteger("energyType")];

        //if (Mods.Factorization.isAvailable && charge != null)
        //    ((Charge) charge).readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setDouble("storage", this.storage);
        tag.setInteger("energyType", energyType.ordinal());

        //if (Mods.Factorization.isAvailable && charge != null)
        //    ((Charge) charge).writeToNBT(tag);
        return tag;
    }

    @Override
    public void readPacketData(NBTTagCompound tag) {
        super.readPacketData(tag);
        latestOutput = tag.getDouble("latestOutput");
        storage = tag.getDouble("storage");
        energyType = EnergyType.values()[tag.getInteger("energyType")];
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);
        tag.setDouble("latestOutput", latestOutput);
        tag.setDouble("storage", this.storage);
        tag.setInteger("energyType", energyType.ordinal());
    }

    protected abstract double computeOutput(World world, BlockPos pos);

    protected int getProduction() {
        return production;
    }

    @Override
    public void update() {
        super.update();

        if (!isServerSide())
            return;

        if (this.storage > this.maxStorage)
            this.storage = this.maxStorage;

        if (Mods.IndustrialCraft2.isAvailable)
            loadEnergyTile();

        //if (Mods.Factorization.isAvailable && charge != null)
        //    ((Charge) charge).update();

        pushFluidToConsumers(getFluidTankCapacity() / 40);

        if (!isRedstonePowered()) {
            latestOutput = computeOutput(worldObj, pos);
            if (energyType == EnergyType.EU && Mods.IndustrialCraft2.isAvailable)
                storage += latestOutput;
            if (energyType == EnergyType.AspectWater && Mods.Thaumcraft.isAvailable)
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
            //if (energyType == EnergyType.Charge && Mods.Factorization.isAvailable) {
            //    ((Charge) charge).setValue((int) EnergyType.EU2Charge(latestOutput));
            //}
            if (energyType == EnergyType.Water) {
                if (!FluidRegistry.WATER.getName().equals(getFluidName()))
                    getFluidTank().setFluid(null);
                getFluidTank().fill(new FluidStack(FluidRegistry.WATER, (int) EnergyType.EU2Water(latestOutput)), true);
            }
            if (energyType == EnergyType.Steam) {
                boolean outputed = false;
                if (FluidRegistry.isFluidRegistered("steam") && !outputed) {
                    Fluid f = FluidRegistry.getFluid("steam");
                    if (!f.getName().equals(getFluidName()))
                        getFluidTank().setFluid(null);
                    outputed = true;
                    getFluidTank().fill(new FluidStack(f, (int) EnergyType.EU2Steam(latestOutput)), true);
                }
                if (FluidRegistry.isFluidRegistered("ic2steam") && !outputed) {
                    Fluid f = FluidRegistry.getFluid("ic2steam");
                    if (!f.getName().equals(getFluidName()))
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

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
        return energyType == EnergyType.EU;
    }

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
    public int maxrequestHeatTick(EnumFacing directionFrom) {
        if (energyType == EnergyType.HU)
            return (int) (EnergyType.EU2HU(latestOutput));
        return 0;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public int requestHeat(EnumFacing directionFrom, int requestheat) {
        if (energyType == EnergyType.HU)
            return Math.min(requestheat, maxrequestHeatTick(directionFrom));
        return 0;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public int maxrequestkineticenergyTick(EnumFacing directionFrom) {
        if (energyType == EnergyType.KU)
            return (int) (EnergyType.EU2KU(latestOutput));
        return 0;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public int requestkineticenergy(EnumFacing directionFrom, int requestkineticenergy) {
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
    public boolean canConnectEnergy(EnumFacing d) {
        return true;
    }

    @Method(modid = Mods.IDs.CoFHAPIEnergy)
    protected final int transmitEnergy(int e) {
        if (this.handlerCache != null) {
            for (int i = this.handlerCache.length; i-- > 0;) {
                IEnergyReceiver h = (IEnergyReceiver) this.handlerCache[i];
                if (h == null)
                    continue;
                EnumFacing d = EnumFacing.VALUES[i];
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
            for (EnumFacing d : EnumFacing.VALUES)
                onNeighborTileChange(pos.add(d.getDirectionVec()));
            this.deadCache = false;
        }
    }

    @Override
    @Method(modid = Mods.IDs.CoFHAPIEnergy)
    public void onNeighborTileChange(BlockPos neighbor) {
        TileEntity t = this.worldObj.getTileEntity(neighbor);
        
        if (neighbor.getX() < pos.getX())
            addCache(t, EnumFacing.EAST.ordinal());
        else if (neighbor.getX() > pos.getX())
            addCache(t, EnumFacing.WEST.ordinal());
        else if (neighbor.getY() < pos.getY())
            addCache(t, EnumFacing.UP.ordinal());
        else if (neighbor.getY() > pos.getY())
            addCache(t, EnumFacing.DOWN.ordinal());
        else if (neighbor.getZ() < pos.getZ())
            addCache(t, EnumFacing.NORTH.ordinal());
        else if (neighbor.getZ() > pos.getZ())
            addCache(t, EnumFacing.SOUTH.ordinal());
    }

    @Method(modid = Mods.IDs.CoFHAPIEnergy)
    private void addCache(TileEntity t, int side) {
        if (this.handlerCache != null) {
            this.handlerCache[side] = null;
        }
        if (t instanceof IEnergyReceiver && ((IEnergyReceiver) t).canConnectEnergy(EnumFacing.VALUES[side])) {
            if (this.handlerCache == null)
                this.handlerCache = new IEnergyReceiver[6];
            this.handlerCache[side] = (t);
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
/*
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

    @Override
    @Method(modid = Mods.IDs.Factorization)
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(I18n.format("cptwtrml.gui.latest_output") + "/" + energyType.name() + ": "
                + Utils.DEFAULT_DECIMAL_FORMAT.format(getFromEU(latestOutput)) + "\n");
        return sb.toString();
    }*/

    public double getFromEU(double eu) {
        return energyType.getFromEU(eu);
    }

    /**
     * ------------------------------------------------------
     * 
     * CHARGE(FACTORIZATION) INTEGRATION ENDS
     * 
     * ------------------------------------------------------
     */

    /**
     * ------------------------------------------------------
     * 
     * ESSENTIA(THAUMCRAFT) INTEGRATION BEGINS
     * 
     * ------------------------------------------------------
     */

    /*@Override
    public boolean isConnectable(EnumFacing arg0) {
        return true;
    }

    @Override
    public boolean canInputFrom(EnumFacing arg0) {
        return false;
    }

    @Override
    public boolean canOutputTo(EnumFacing arg0) {
        return true;
    }

    @Override
    public void setSuction(Aspect arg0, int arg1) {
    }

    @Override
    public Aspect getSuctionType(EnumFacing arg0) {
        return null;
    }

    @Override
    public int getSuctionAmount(EnumFacing arg0) {
        return 0;
    }

    @Override
    public int takeEssentia(Aspect arg0, int arg1, EnumFacing arg2) {
        return takeFromContainer(arg0, arg1) ? arg1 : 0;
    }

    @Override
    public int addEssentia(Aspect arg0, int arg1, EnumFacing arg2) {
        return 0;
    }

    @Override
    public AspectList getAspects() {
        return new AspectList().add(Aspect.WATER, (int) EnergyType.EU2Vis(storage));
    }

    @Override
    public void setAspects(AspectList arg0) {
    }

    @Override
    public boolean takeFromContainer(Aspect arg0, int arg1) {
        double eu = EnergyType.Vis2EU(arg1);
        if (eu > storage)
            return false;
        storage -= eu;
        return true;
    }

    @Override
    public boolean doesContainerContain(AspectList arg0) {
        return storage > 0 && arg0.getAmount(Aspect.WATER) > 0;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect arg0, int arg1) {
        return Aspect.WATER == arg0 && EnergyType.EU2Vis(storage) >= arg1;
    }

    @Override
    public int containerContains(Aspect arg0) {
        return Aspect.WATER == arg0 ? (int) EnergyType.EU2Vis(storage) : 0;
    }

    @Override
    public boolean doesContainerAccept(Aspect arg0) {
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList arg0) {
        return false;
    }

    @Override
    public Aspect getEssentiaType(EnumFacing arg0) {
        return Aspect.WATER;
    }

    @Override
    public int getEssentiaAmount(EnumFacing arg0) {
        return (int) EnergyType.EU2Vis(storage);
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }

    @Override
    public int addToContainer(Aspect arg0, int arg1) {
        return arg1;
    }*/

    @Override
    public boolean canDrain(EnumFacing side, Fluid fluid) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void onUnitChanged(EnergyType t) {
        energyType = t;
        MessagePacketHandler.INSTANCE.sendToServer(new PacketUnitChanged(Minecraft.getMinecraft().thePlayer.worldObj.provider.getDimension(), pos, energyType.ordinal()));
    }
}