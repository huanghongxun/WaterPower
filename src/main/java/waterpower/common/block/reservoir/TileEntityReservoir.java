package waterpower.common.block.reservoir;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.mutable.MutableObject;

import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import waterpower.Reference;
import waterpower.WaterPower;
import waterpower.api.IUpgrade;
import waterpower.api.IWaterReceiver;
import waterpower.client.gui.DefaultGuiIds;
import waterpower.common.block.inventory.InventorySlotConsumableLiquid;
import waterpower.common.block.inventory.InventorySlotOutput;
import waterpower.common.block.inventory.InventorySlotUpgrade;
import waterpower.common.block.tileentity.TileEntityMultiBlock;
import waterpower.util.Mods;
import waterpower.util.Pair;
import waterpower.util.Utils;

@InterfaceList({ @Interface(iface = "ic2.api.tile.IWrenchable", modid = Mods.IDs.IndustrialCraft2API, striprefs = true) })
public class TileEntityReservoir extends TileEntityMultiBlock {

    public EnumFacing side;
    public Reservoir size = new Reservoir();

    private int lastLength = -1, lastWidth = -1, lastHeight = -1;
    private double rainLevel, underLevel, overLevel;
    private int lastAddedWater = 0, preLastAddedWater = -999;
    private Pair ocean = new Pair(0, 0);
    boolean sendInitData;

    private final InventorySlotConsumableLiquid fluidSlot = new InventorySlotConsumableLiquid(this, "fluidSlot", 1);
    private final InventorySlotOutput outputSlot = new InventorySlotOutput(this, "output", 1);
    private final InventorySlotUpgrade upgradeSlot = new InventorySlotUpgrade(this, "upgrade", 4);
    private int defaultStorage, extraStorage, storage;

    public TileEntityReservoir() {
    	super(0);
    }
    
    public TileEntityReservoir(ReservoirType type) {
    	super(type.capacity);
    	temporaryType = type;
    }

    @Override
    public void readPacketData(NBTTagCompound tag) {
        super.readPacketData(tag);

        lastAddedWater = tag.getInteger("lastAddedWater");
        size.width = tag.getInteger("reservoirWidth");
        size.length = tag.getInteger("reservoirLength");
        size.height = tag.getInteger("reservoirHeight");
        size.valid = tag.getBoolean("reservoirValid");
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);

        tag.setInteger("lastAddedWater", lastAddedWater);
        
        tag.setInteger("reservoirWidth", size.width);
        tag.setInteger("reservoirLength", size.length);
        tag.setInteger("reservoirHeight", size.height);
        tag.setBoolean("reservoirValid", size.valid);
    }

    public InventorySlotConsumableLiquid getFluidSlot() {
        if (isMaster())
            return fluidSlot;
        else
            return ((TileEntityReservoir) masterBlock).fluidSlot;
    }

    public InventorySlotOutput getOutputSlot() {
        if (isMaster())
            return outputSlot;
        else
            return ((TileEntityReservoir) masterBlock).outputSlot;
    }

    public InventorySlotUpgrade getUpgradeSlot() {
        if (WaterPower.isClientSide() || isMaster())
            return upgradeSlot;
        else
            return ((TileEntityReservoir) masterBlock).upgradeSlot;
    }
    
    ReservoirType temporaryType = null;
    
    public ReservoirType getType() {
    	if (temporaryType != null) return temporaryType;
    	return temporaryType = ReservoirType.values()[getBlockMetadata()];
    }

    @Override
    public String getName() {
        return getType().getShowedName();
    }

    public void useLiquid(int use) {
        if (WaterPower.isClientSide() || isMaster())
            fluidTank.drain(use, true);
        else if (masterBlock != null)
            ((TileEntityReservoir) masterBlock).fluidTank.drain(use, true);
    }

    @Override
    protected boolean canBeMaster() {
        if (Reservoir.isRes(worldObj, pos.down(), getType()) || Reservoir.isRes(worldObj, pos.west(), getType())
                || Reservoir.isRes(worldObj, pos.north(), getType())) {
            return false;
        }
        return true;
    }

    private ArrayList<TileEntityMultiBlock> setSide(ArrayList<TileEntityMultiBlock> a, EnumFacing d) {
        for (TileEntityMultiBlock block : a) {
            TileEntityReservoir te = (TileEntityReservoir) block;
            te.side = d;
        }
        return a;
    }

    @Override
    protected ArrayList<TileEntityMultiBlock> test() {
        int length = 1;
        while (length < 65) {
            if (Reservoir.isRes(worldObj, pos.east(length), getType()))
                ++length;
            else
                break;
        }

        int width = 1;
        while (width < 65)
            if (Reservoir.isRes(worldObj, pos.south(width), getType()))
                ++width;
            else
                break;

        int height = 1;
        while (height < 65)
            if (Reservoir.isRes(worldObj, pos.up(height), getType()))
                ++height;
            else
                break;

        List<BlockPos> l1 = Reservoir.getNotHorizontalWall(worldObj, pos.getX(), pos.getY(), pos.getZ(), length, height, getType());
        if (l1.size() != 0) {
            size.valid = false;
            lastHeight = lastWidth = lastLength = -1;
            return null;
        }

        List<BlockPos> l2 = Reservoir.getNotHorizontalWall(worldObj, pos.getX(), pos.getY(), pos.getZ() + width - 1, length, height, getType());
        if (l2.size() != 0) {
            size.valid = false;
            lastHeight = lastWidth = lastLength = -1;
            return null;
        }

        List<BlockPos> l3 = Reservoir.getNotVerticalWall(worldObj, pos.getX(), pos.getY(), pos.getZ(), width, height, getType());
        if (l3.size() != 0) {
            size.valid = false;
            lastHeight = lastWidth = lastLength = -1;
            return null;
        }

        List<BlockPos> l4 = Reservoir.getNotVerticalWall(worldObj, pos.getX() + length - 1, pos.getY(), pos.getZ(), width, height, getType());
        if (l4.size() != 0) {
            size.valid = false;
            lastHeight = lastWidth = lastLength = -1;
            return null;
        }

        List<BlockPos> lfloor = Reservoir.getNotFloor(worldObj, pos.getX(), pos.getY(), pos.getZ(), length, width, getType());
        if (lfloor.size() != 0) {
            size.valid = false;
            lastHeight = lastWidth = lastLength = -1;
            return null;
        }

        lastHeight = height;
        lastWidth = width;
        lastLength = length;
        return clientSetMultiBlocks();
    }

    public ArrayList<TileEntityMultiBlock> clientSetMultiBlocks() {
        ArrayList<TileEntityMultiBlock> al = new ArrayList<TileEntityMultiBlock>();
        ArrayList<TileEntityMultiBlock> tmp;

        int length = lastLength, width = lastWidth, height = lastHeight;

        if (length == -1 || width == -1 || height == -1)
            return null;

        ocean.clear();
        tmp = Reservoir.getHorizontalWall(worldObj, pos.getX(), pos.getY(), pos.getZ(), length, height, getType());
        al.addAll(tmp);
        ocean.add(Reservoir.getHorizontalWallWater(worldObj, pos.getX(), pos.getY(), pos.getZ() - 1, length, height, getType()));
        tmp = Reservoir.getHorizontalWall(worldObj, pos.getX(), pos.getY(), pos.getZ() + width - 1, length, height, getType());
        al.addAll(tmp);
        ocean.add(Reservoir.getHorizontalWallWater(worldObj, pos.getX(), pos.getY(), pos.getZ() + width, length, height, getType()));
        tmp = Reservoir.getVerticalWall(worldObj, pos.getX(), pos.getY(), pos.getZ(), width, height, getType());
        al.addAll(tmp);
        ocean.add(Reservoir.getVerticalWallWater(worldObj, pos.getX() - 1, pos.getY(), pos.getZ(), width, height, getType()));
        tmp = Reservoir.getVerticalWall(worldObj, pos.getX() + length - 1, pos.getY(), pos.getZ(), width, height, getType());
        al.addAll(tmp);
        ocean.add(Reservoir.getVerticalWallWater(worldObj, pos.getX() + length, pos.getY(), pos.getZ(), width, height, getType()));
        tmp = Reservoir.getFloor(worldObj, pos.getX(), pos.getY(), pos.getZ(), length, width, getType());
        al.addAll(tmp);
        if (pos.getY() > 0)
            ocean.add(Reservoir.getFloorWater(worldObj, pos.getX(), pos.getY() - 1, pos.getZ(), length, width, getType()));

        size.valid = true;
        size.length = length;
        size.width = width;
        size.height = height;
        size.nonAirBlock = Reservoir.getNonAirBlock(worldObj, pos.getX(), pos.getY(), pos.getZ(), length, width, height);

        defaultStorage = size.getCapacity() * getType().capacity;
        setFluidTankCapacity(defaultStorage + extraStorage);

        return al;
    }

    @Override
    protected void onUpdate() {

        if (!isMaster() || WaterPower.isClientSide() || !size.valid)
            return;

        double[] ds = Utils.getBiomeRaining(worldObj, pos);
        int weather = (int) ds[0];
        double biomeGet = ds[1];
        double biomePut = ds[2];

        int length = size.getLength() - 2;
        int width = size.getWidth() - 2;
        int area = length * width;
        int cover = Reservoir.getCoverBlock(worldObj, pos.getX(), pos.getY() + size.getHeight(), pos.getZ(), size.getLength(), size.getWidth());

        double addWater = 0;
        double add = getType().capacity / 10000.0;
        // Rain Receiving
        addWater += rainLevel * weather * add * (area - cover) * biomeGet;
        // Tide Receiving
        addWater += Math.min(this.ocean.k, this.ocean.t) * getType().capacity / 100;
        // Underground water receiving
        addWater += underLevel * (1D - this.pos.getY() / 256) * add * (area - cover) * biomeGet;
        // Surface water receiving
        addWater += overLevel * (1D - Math.abs(64 - this.pos.getY()) / 64) * (area - cover) * biomeGet * add;

        String biomeID = worldObj.getBiomeGenForCoords(pos).getBiomeName();
        if (("ocean".equals(biomeID) || "river".equals(biomeID)) && pos.getY() < 64) {
            addWater += length * width * 0.5;
        }

        int delWater = (int) (weather == 0 ? area * 0.02 * biomePut : 0);

        preLastAddedWater = lastAddedWater;
        lastAddedWater = (int) ((addWater - delWater) * Reference.General.updateTick);

        if ((int) addWater * Reference.General.updateTick >= 1)
            fluidTank.fill(new FluidStack(FluidRegistry.WATER, (int) addWater * Reference.General.updateTick), true);
        fluidTank.drain(delWater * Reference.General.updateTick, true);
    }

    @Override
    public void update() {
        super.update();

        if (worldObj == null || worldObj.isRemote)
            return;

        boolean needsInvUpdate = false;

        if (needsFluid()) {
            MutableObject<ItemStack> output = new MutableObject<ItemStack>();

            if ((this.getFluidSlot().transferToTank(this.fluidTank, output, true))
                    && ((output.getValue() == null) || (this.outputSlot.canAdd(output.getValue())))) {
                needsInvUpdate = this.getFluidSlot().transferToTank(this.fluidTank, output, false);

                if (output.getValue() != null)
                    this.getOutputSlot().add(output.getValue());
            }

        }

        MutableObject<ItemStack> output = new MutableObject<ItemStack>();
        if ((this.getFluidSlot().transferFromTank(this.fluidTank, output, true))
                && ((output.getValue() == null) || (this.outputSlot.canAdd(output.getValue())))) {
            needsInvUpdate = this.getFluidSlot().transferFromTank(this.fluidTank, output, false);

            if (output.getValue() != null)
                this.getOutputSlot().add(output.getValue());
        }

        if (needsInvUpdate) {
            markDirty();
        }

        boolean flag = false;

        for (EnumFacing f : EnumFacing.values()) {
            TileEntity te = worldObj.getTileEntity(pos.offset(f));
            if (te instanceof IWaterReceiver) {
                IWaterReceiver te2 = (IWaterReceiver) te;
                int i = te2.canProvideWater(this.getFluidAmount(), f.getOpposite(), this);
                if (i > 0) {
                    te2.provideWater(i);
                    this.useLiquid(i);
                    flag = true;
                }
            }
        }

        if (flag)
            sendUpdateToClient();
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityReservoir");
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        // return
        // FluidRegistry.getFluidName(fluid.getID()).contentEquals("water");
        return true;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        // /return
        // FluidRegistry.getFluidName(fluid.getID()).contentEquals("water");
        return true;
    }

    @Override
    protected void onTestFailed() {
        setFluidTankCapacity(0);
    }

    public int getLastAddedWater() {
        if (isMaster())
            return lastAddedWater;
        else
            return ((TileEntityReservoir) masterBlock).getLastAddedWater();
    }

    @Override
    public void onLoaded() {
        if (isServerSide())
            refreshPlugins();

        super.onLoaded();
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (isServerSide())
            refreshPlugins();
    }

    public void refreshPlugins() {
        if (!isMaster())
            return;
        extraStorage = 0;
        rainLevel = overLevel = underLevel = 0;

        for (int i = 0; i < this.upgradeSlot.size(); i++) {
            ItemStack stack = this.upgradeSlot.get(i);

            if ((stack == null) || (!(stack.getItem() instanceof IUpgrade)))
                continue;
            IUpgrade upgrade = (IUpgrade) stack.getItem();
            rainLevel += upgrade.getRainAdditionalValue(stack);
            overLevel += upgrade.getOverworldAdditionalValue(stack);
            underLevel += upgrade.getUnderworldAdditionalValue(stack);
            extraStorage += upgrade.getStorageAdditionalValue(stack);
        }

        setFluidTankCapacity(defaultStorage + extraStorage);
    }

    @Override
    protected boolean allowedSendPacketTank() {
        return isMaster();
    }
}