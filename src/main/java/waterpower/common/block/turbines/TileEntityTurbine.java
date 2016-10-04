package waterpower.common.block.turbines;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import waterpower.client.gui.DefaultGuiIds;
import waterpower.common.block.reservoir.Reservoir;
import waterpower.common.block.reservoir.TileEntityReservoir;
import waterpower.common.block.tileentity.TileEntityRotor;
import waterpower.util.WPLog;

public class TileEntityTurbine extends TileEntityRotor {

    public static final int MAX_OUTPUT = 32767;

    public TileEntityTurbine() {
        super();
    }
    
    public TileEntityTurbine(TurbineType type) {
    	super(type.percent, 10000000);
    	temporaryType = type;
    }
    
    @Override
    public IBlockState getBlockState() {
    	return super.getBlockState().withProperty(BlockTurbine.TURBINE_TYPES, getType());
    }
    
    @Override
    protected void func_190201_b(World p_190201_1_) {
    	super.func_190201_b(p_190201_1_);
    	worldObj = p_190201_1_;
    	getType();
    }
    
    TurbineType temporaryType = null;
    
    public TurbineType getType() {
    	if (temporaryType != null) return temporaryType;
    	temporaryType = TurbineType.values()[getBlockMetadata()];
        production = temporaryType.percent;
        maxStorage = 10000000;
        return temporaryType;
    }

    private TileEntityReservoir getWater(World world, BlockPos pos) {
        TileEntityReservoir reservoir = null;
        BlockPos newPos = pos.offset(getFacing().getOpposite());
        if (newPos.getY() != pos.getY())
        	return null;
        if (Reservoir.isRes(world, newPos))
            reservoir = (TileEntityReservoir) world.getTileEntity(newPos);
        return reservoir;
    }

    public double getWater() {
        TileEntityReservoir pair = getWater(worldObj, getPos());
        return pair.getFluidAmount();
    }

    @Override
    protected double computeOutput(World world, BlockPos pos) {
        TileEntityReservoir pair = getWater(world, pos);
        if (pair == null || pair.getFluidTank() == null || pair.getFluidStackfromTank() == null || pair.getFluidfromTank() == null)
            return 0;
        else {
            WPLog.debugLog("fluidAmount=" + pair.getFluidAmount());
            WPLog.debugLog("maxUse=" + pair.getType().maxUse);
            double use = Math.min(pair.getFluidAmount(), pair.getType().maxUse);
            WPLog.debugLog("use=" + use);
            int multiple = 0;
            if (pair.getFluidfromTank() == FluidRegistry.WATER)
                multiple = 1;
            else if (FluidRegistry.isFluidRegistered("steam") && pair.getFluidfromTank() == FluidRegistry.getFluid("steam")
                    || FluidRegistry.isFluidRegistered("ic2steam") && pair.getFluidfromTank() == FluidRegistry.getFluid("ic2steam"))
                multiple = 5;
            if (multiple == 0)
                return 0;
            double baseEnergy = use * getType().percent / 2048 * multiple;
            WPLog.debugLog("baseEnergy" + baseEnergy);
            // WPLog.debugLog("speed" + speed);
            double per = tickRotor();
            WPLog.debugLog("per=" + per);
            if (per > 0) {
                double energy = baseEnergy * per; // * ((double) speed / 50);
                WPLog.debugLog("energy = " + energy);
                if (energy > 0) {
                    pair.useLiquid((int) use);
                    damageRotor(1);
                }
                WPLog.debugLog("use=" + use);
                return energy;
            }
        }
        return 0;
    }

    @Override
    public String getName() {
        return getType().getShowedName();
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityTurbine");
    }

    @Override
    public boolean isActive() {
        return super.isActive() && (storage < maxStorage);
    }

    @Override
    protected boolean allowedSendPacketTank() {
        return false;
    }

}