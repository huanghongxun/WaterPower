package waterpower.common.block.watermills;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import waterpower.WaterPower;
import waterpower.client.gui.DefaultGuiIds;
import waterpower.common.block.tileentity.TileEntityRotor;
import waterpower.common.item.range.ItemRange;
import waterpower.common.item.range.RangeInventorySlot;
import waterpower.common.item.range.RangeType;
import waterpower.util.Utils;

/**
 * 
 * @author jackhuang1998
 * 
 */
public class TileEntityWatermill extends TileEntityRotor {

    private boolean canWheelTurn = false;
    public int waterBlocks, lavaBlocks;
    public int preWaterBlocks = -999, preLavaBlocks = -999;
    public int range = 0, rotor = -1;

    RangeInventorySlot slotUpdater;
    
    {
        slotUpdater = new RangeInventorySlot(this);
        addInvSlot(slotUpdater);
    }

    public TileEntityWatermill() {
        super();
    }
    
    public TileEntityWatermill(WaterType type) {
    	super(type.output, 2097152);
    	temporaryType = type;
    }
    
    @Override
    protected void func_190201_b(World p_190201_1_) {
    	super.func_190201_b(p_190201_1_);
    	worldObj = p_190201_1_;
    	getType();
    }
    
    @Override
    public IBlockState getBlockState() {
    	return super.getBlockState().withProperty(BlockWatermill.WATERMILL_TYPES, getType());
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);
        tag.setInteger("waterBlocks", waterBlocks);
        tag.setInteger("lavaBlocks", lavaBlocks);

        rotor = hasRotor() ? getRotor().type.ordinal() : -1;
        tag.setInteger("rotor", rotor);
        tag.setInteger("range", range);
    }

    @Override
    public void readPacketData(NBTTagCompound tag) {
        super.readPacketData(tag);
        waterBlocks = tag.getInteger("waterBlocks");
        lavaBlocks = tag.getInteger("lavaBlocks");

        rotor = tag.getInteger("rotor");
        range = tag.getInteger("range");
    }
    
    WaterType temporaryType = null;
    
    public WaterType getType() {
    	if (temporaryType != null) return temporaryType;
    	temporaryType = WaterType.values()[getBlockMetadata()];
        production = temporaryType.output;
        maxStorage = 2097152;
        return temporaryType;
    }

    private void getWaterBlocks() {
        preLavaBlocks = lavaBlocks;
        preWaterBlocks = waterBlocks;
        lavaBlocks = waterBlocks = 0;

        int range = getRange();
        if (range * range * range > 729) {
            lavaBlocks = waterBlocks = -1;
            return;
        }
        range /= 2;

        for (int xTest = -range; xTest <= range; xTest++) {
            for (int yTest = -range; yTest <= range; yTest++) {
                for (int zTest = -range; zTest <= range; zTest++) {
                	BlockPos newPos = pos.add(xTest, yTest, zTest);
                    if (Utils.isWater(worldObj, newPos)) {
                        waterBlocks++;
                    } else if (Utils.isLava(worldObj, newPos)) {
                        lavaBlocks++;
                    }
                }
            }
        }

        if (getType().ordinal() < 2)
            lavaBlocks = 0;
    }

    public boolean isRangeSupported() {
        return waterBlocks != -1 && lavaBlocks != -1;
    }

    @Override
    protected double computeOutput(World world, BlockPos pos) {
        if (waterBlocks == -1) {
            return 0;
        }

        double vol = getRange();
        vol *= vol * vol;
        double waterPercent = waterBlocks / (vol - 1);
        double lavaPercent = lavaBlocks / (vol - 1);
        double percent = waterPercent + lavaPercent * 4;
        double energy = getType().output * percent;
        energy *= tickRotor();
        if (energy > 0)
            damageRotor(1);
        return energy;
    }

    @Override
    protected int getProduction() {
        if (lavaBlocks > 0)
            return super.getProduction() * 4;
        else
            return super.getProduction();
    }

    public boolean hasRangeUpdater() {
        if (slotUpdater != null && !slotUpdater.isEmpty()) {
            for (int i = 0; i < slotUpdater.size(); i++)
                if (slotUpdater.get(i) != null && slotUpdater.get(i).getItem() instanceof ItemRange)
                    return true;
        }
        return false;
    }

    public int getRange() {
        if (WaterPower.isClientSide() && range != 0)
            return range;
        int range = getType().length;
        if (slotUpdater != null && !slotUpdater.isEmpty()) {
            for (int i = 0; i < slotUpdater.size(); i++) {
                ItemStack is = slotUpdater.get(i);
                if (is == null)
                    continue;
                if (is != null && slotUpdater.get(i).getItem() instanceof ItemRange && is.getItemDamage() >= RangeType.values().length)
                    return getType().length;
                range -= is.stackSize * RangeType.values()[is.getItemDamage()].range;
            }
        }
        if (range < 3)
            range = 3;
        return range;
    }

    @Override
    public String getName() {
        return getType().getShowedName();
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityWatermill");
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();

        getWaterBlocks();
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
