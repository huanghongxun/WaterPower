package org.jackhuang.watercraft.common.block.reservoir;

import ic2.api.tile.IWrenchable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.plaf.metal.OceanTheme;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenOcean;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.mutable.MutableObject;
import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.api.IUpgrade;
import org.jackhuang.watercraft.api.IWaterReceiver;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.block.IDroppable;
import org.jackhuang.watercraft.common.block.turbines.Position;
import org.jackhuang.watercraft.common.inventory.InventorySlotConsumableLiquid;
import org.jackhuang.watercraft.common.inventory.InventorySlotConsumableLiquidByList;
import org.jackhuang.watercraft.common.inventory.InventorySlotOutput;
import org.jackhuang.watercraft.common.inventory.InventorySlotUpgrade;
import org.jackhuang.watercraft.common.tileentity.TileEntityMetaMultiBlock;
import org.jackhuang.watercraft.common.tileentity.TileEntityMultiBlock;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.Pair;
import org.jackhuang.watercraft.util.Utils;
import org.lwjgl.opengl.HPOcclusionTest;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;

@InterfaceList({
    @Interface(iface = "ic2.api.tile.IWrenchable", modid = Mods.IDs.IndustrialCraft2API, striprefs = true)})
public class TileEntityReservoir extends TileEntityMetaMultiBlock implements
	IWrenchable, IDroppable {

    public Direction side;
    public ReservoirType type;
    public Reservoir size;

    private int lastLength = -1, lastWidth = -1, lastHeight = -1;
    private double rainLevel, underLevel, overLevel;
    private int lastAddedWater = 0;
    private Pair ocean = new Pair(0, 0);

    private final InventorySlotConsumableLiquid fluidSlot;
    private final InventorySlotOutput outputSlot;
    private final InventorySlotUpgrade upgradeSlot;
    private int highPotentialEnergyWater;
    private int defaultStorage, extraStorage, storage;

    public TileEntityReservoir() {
	super(0);

	this.fluidSlot = new InventorySlotConsumableLiquid(this,
		"fluidSlot", 1);
	this.outputSlot = new InventorySlotOutput(this, "output", 1);
	this.upgradeSlot = new InventorySlotUpgrade(this, "upgrade", 4);

	highPotentialEnergyWater = 0;
    }

    @Override
    public void initNBT(NBTTagCompound nbt, int meta) {
	if (meta == -1) {
	    type = ReservoirType.values()[nbt.getInteger("type")];
	} else {
	    type = ReservoirType.values()[meta];
	}
	sendUpdateToClient();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
	super.writeToNBT(nbttagcompound);
	if (type == null) {
	    nbttagcompound.setInteger("type", 0);
	} else {
	    nbttagcompound.setInteger("type", type.ordinal());
	}

	nbttagcompound.setInteger("hpWater", highPotentialEnergyWater);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
	super.readFromNBT(nbt);

	highPotentialEnergyWater = nbt.getInteger("hpWater");
    }

    public TileEntityReservoir(ReservoirType type) {
	this();
	this.type = type;
    }

    public InventorySlotConsumableLiquid getFluidSlot() {
	if (isMaster) {
	    return fluidSlot;
	} else if (masterBlock == null) {
	    return fluidSlot;
	} else {
	    return ((TileEntityReservoir) masterBlock).fluidSlot;
	}
    }

    public InventorySlotOutput getOutputSlot() {
	if (isMaster) {
	    return outputSlot;
	} else if (masterBlock == null) {
	    return outputSlot;
	} else {
	    return ((TileEntityReservoir) masterBlock).outputSlot;
	}
    }

    public InventorySlotUpgrade getUpgradeSlot() {
	if (isMaster) {
	    return upgradeSlot;
	} else if (masterBlock == null) {
	    return upgradeSlot;
	} else {
	    return ((TileEntityReservoir) masterBlock).upgradeSlot;
	}
    }

    @Override
    public String getInventoryName() {
	if (type == null) {
	    return "NULL";
	}
	return type.getShowedName();
    }

    public int getFluidAmount() {
	if (!isServerSide()) {
	    return getTankAmount();
	}
	if (isMaster) // return water;
	{
	    return getTankAmount();
	} else if (masterBlock == null) {
	    return 0;
	} else {
	    return ((TileEntityReservoir) masterBlock).getTankAmount();
	}

    }

    public int getHPWater() {
	if (!isServerSide()) {
	    return highPotentialEnergyWater;
	}
	if (isMaster) {
	    return highPotentialEnergyWater;
	} else if (masterBlock == null) {
	    return 0;
	} else {
	    return ((TileEntityReservoir) masterBlock).highPotentialEnergyWater;
	}
    }

    public void useLiquid(int use) {
	if (isMaster) {
	    fluidTank.drain(use, true);
	} else if (masterBlock == null) {
	    return;
	} else {
	    ((TileEntityReservoir) masterBlock).fluidTank.drain(use, true);
	}
    }

    public void useHPWater(int use) {
	if (isMaster) {
	    highPotentialEnergyWater -= use;
	} else if (masterBlock == null) {
	    return;
	} else {
	    ((TileEntityReservoir) masterBlock).highPotentialEnergyWater -= use;
	}
    }

    public int getMaxFluidAmount() {
	if (!isServerSide()) {
	    return getFluidTankCapacity();
	}
	if (isMaster) {
	    return getFluidTankCapacity();
	} else if (masterBlock == null) {
	    return 0;
	} else {
	    return ((TileEntityReservoir) masterBlock).getFluidTankCapacity();
	}
    }

    @Override
    protected boolean canBeMaster() {
	if (type == null) {
	    return false;
	}
	if (Reservoir.isRes(worldObj, xCoord, yCoord - 1, zCoord,
		type.ordinal())
		|| Reservoir.isRes(worldObj, xCoord - 1, yCoord, zCoord,
			type.ordinal())
		|| Reservoir.isRes(worldObj, xCoord, yCoord, zCoord - 1,
			type.ordinal())) {
	    return false;
	}
	return true;
    }

    private ArrayList<TileEntityMultiBlock> setSide(
	    ArrayList<TileEntityMultiBlock> a, Direction d) {
	for (TileEntityMultiBlock block : a) {
	    TileEntityReservoir te = (TileEntityReservoir) block;
	    te.side = d;
	}
	return a;
    }

    @Override
    protected ArrayList<TileEntityMultiBlock> test() {
	if (type == null) {
	    return null;
	}

	int length = 1;
	while (length < 65) {
	    if (Reservoir.isRes(worldObj, xCoord + length, yCoord, zCoord,
		    type.ordinal())) {
		length++;
	    } else {
		break;
	    }
	}

	int width = 1;
	while (width < 65) {
	    if (Reservoir.isRes(worldObj, xCoord, yCoord, zCoord + width,
		    type.ordinal())) {
		width++;
	    } else {
		break;
	    }
	}

	int height = 1;
	while (height < 65) {
	    if (Reservoir.isRes(worldObj, xCoord, yCoord + height, zCoord,
		    type.ordinal())) {
		height++;
	    } else {
		break;
	    }
	}

	ArrayList<Position> l1 = Reservoir.getNotHorizontalWall(worldObj,
		xCoord, yCoord, zCoord, length, height, type.ordinal());
	if (l1.size() != 0) {
	    size = null;
	    lastHeight = lastWidth = lastLength = -1;
	    return null;
	}

	ArrayList<Position> l2 = Reservoir.getNotHorizontalWall(worldObj,
		xCoord, yCoord, zCoord + width - 1, length, height,
		type.ordinal());
	if (l2.size() != 0) {
	    size = null;
	    lastHeight = lastWidth = lastLength = -1;
	    return null;
	}

	ArrayList<Position> l3 = Reservoir.getNotVerticalWall(worldObj, xCoord,
		yCoord, zCoord, width, height, type.ordinal());
	if (l3.size() != 0) {
	    size = null;
	    lastHeight = lastWidth = lastLength = -1;
	    return null;
	}

	ArrayList<Position> l4 = Reservoir.getNotVerticalWall(worldObj, xCoord
		+ length - 1, yCoord, zCoord, width, height, type.ordinal());
	if (l4.size() != 0) {
	    size = null;
	    lastHeight = lastWidth = lastLength = -1;
	    return null;
	}

	ArrayList<Position> lfloor = Reservoir.getNotFloor(worldObj, xCoord,
		yCoord, zCoord, length, width, type.ordinal());
	if (lfloor.size() != 0) {
	    size = null;
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
	ArrayList<TileEntityMultiBlock> tmp = new ArrayList<TileEntityMultiBlock>();

	int length = lastLength, width = lastWidth, height = lastHeight;

	if (length == -1 || width == -1 || height == -1) {
	    return null;
	}

	ocean.clear();
	tmp = Reservoir.getHorizontalWall(worldObj, xCoord, yCoord, zCoord,
		length, height, type.ordinal());
	al.addAll(tmp);
	ocean.add(Reservoir.getHorizontalWallWater(worldObj, xCoord, yCoord,
		zCoord - 1, length, height, type.ordinal()));
	tmp = Reservoir.getHorizontalWall(worldObj, xCoord, yCoord, zCoord
		+ width - 1, length, height, type.ordinal());
	al.addAll(tmp);
	ocean.add(Reservoir.getHorizontalWallWater(worldObj, xCoord, yCoord,
		zCoord + width, length, height, type.ordinal()));
	tmp = Reservoir.getVerticalWall(worldObj, xCoord, yCoord, zCoord,
		width, height, type.ordinal());
	al.addAll(tmp);
	ocean.add(Reservoir.getVerticalWallWater(worldObj, xCoord - 1, yCoord,
		zCoord, width, height, type.ordinal()));
	tmp = Reservoir.getVerticalWall(worldObj, xCoord + length - 1, yCoord,
		zCoord, width, height, type.ordinal());
	al.addAll(tmp);
	ocean.add(Reservoir.getVerticalWallWater(worldObj, xCoord + length,
		yCoord, zCoord, width, height, type.ordinal()));
	tmp = Reservoir.getFloor(worldObj, xCoord, yCoord, zCoord, length, width,
		type.ordinal());
	al.addAll(tmp);
	if (yCoord > 0) {
	    ocean.add(Reservoir.getFloorWater(worldObj, xCoord, yCoord - 1,
		    zCoord, length, width, type.ordinal()));
	}

	size = new Reservoir(length, width, height, Reservoir.getNonAirBlock(
		worldObj, xCoord, yCoord, zCoord, length, width, height));

	defaultStorage = size.getCapacity() * type.capacity;
	setFluidTankCapacity(defaultStorage + extraStorage);

	return al;
    }

    @Override
    protected void onUpdate() {

	if (!isMaster) {
	    return;
	}
	if (size == null) {
	    return;
	}

	double[] ds = Utils.getBiomeRaining(worldObj, xCoord, zCoord);
	int weather = (int) ds[0];
	int biomeID = (int) ds[1];
	double biomeGet = ds[2];
	double biomePut = ds[3];

	int length = size.getLength() - 2;
	int width = size.getWidth() - 2;
	int area = length * width;
	int cover = Reservoir.getCoverBlock(worldObj, xCoord,
		yCoord + size.getHeight(), zCoord, size.getLength(),
		size.getWidth());

	//int addWater = (int) ((area - cover) * 2 * weather * biomeGet);
	double addWater = 0;
	double add = (double) type.capacity / 10000.0;
	//Rain Receiving
	addWater += rainLevel * (underLevel / 4) * (overLevel / 4) * weather * add * (area - cover) * biomeGet;
	//Tide Receiving
	addWater += Math.min(this.ocean.k, this.ocean.t) * type.capacity / 100;
	//Underground water receiving
	addWater += underLevel * (overLevel / 4) * (rainLevel / 4) * (1D - this.yCoord / 256) * add * (area - cover) * biomeGet;
	//Surface water receiving
	addWater += overLevel * (underLevel / 4) * (rainLevel / 4) * (1D - Math.abs(64 - this.yCoord) / 64) * (area - cover) * biomeGet * add;

	if (biomeID == BiomeGenBase.ocean.biomeID
		|| biomeID == BiomeGenBase.river.biomeID) {
	    if (yCoord < 64) {
		addWater += length * width * 0.5;
	    }
	}

	int delWater = (int) (weather == 0 ? area * 0.02 * biomePut : 0);

	lastAddedWater = (int) (addWater - delWater);

	if ((int) addWater * WaterPower.updateTick >= 1) {
	    fluidTank.fill(new FluidStack(FluidRegistry.WATER, (int) addWater
		    * WaterPower.updateTick), true);
	}
	highPotentialEnergyWater += addWater * WaterPower.updateTick;
	if (highPotentialEnergyWater > getMaxFluidAmount()) {
	    highPotentialEnergyWater = getMaxFluidAmount();
	}
	fluidTank.drain(delWater * WaterPower.updateTick, true);

	sendUpdateToClient();

	for (TileEntityMultiBlock b : blockList) {
	    b.sendUpdateToClient();
	}

	// LogHelper.log("?" + this + " " + getWater());
	// water += (addWater - delWater) * CompactWatermills.updateTick;
	// if (water < 0)
	// water = 0;
	// if (water > getMaxWater())
	// water = getMaxWater();
	// LogHelper.log("water=" + water);
    }

    @Override
    public void updateEntity() {
	super.updateEntity();

	if (worldObj == null || worldObj.isRemote) {
	    return;
	}

	boolean needsInvUpdate = false;

	if (needsFluid()) {
	    MutableObject<ItemStack> output = new MutableObject<ItemStack>();

	    if ((this.getFluidSlot().transferToTank(this.fluidTank, output,
		    true))
		    && ((output.getValue() == null) || (this.outputSlot
		    .canAdd(output.getValue())))) {
		needsInvUpdate = this.getFluidSlot().transferToTank(
			this.fluidTank, output, false);

		if (output.getValue() != null) {
		    this.getOutputSlot().add(output.getValue());
		}
	    }

	}

	if (needsInvUpdate) {
	    markDirty();
	}

	boolean flag = false;

	for (ForgeDirection direction : ForgeDirection.values()) {
	    TileEntity te = worldObj.getTileEntity(xCoord
		    + direction.offsetX, yCoord + direction.offsetY, zCoord
		    + direction.offsetZ);
	    if (te instanceof IWaterReceiver) {
		IWaterReceiver te2 = (IWaterReceiver) te;
		int i = te2.canProvideWater(this.getHPWater() * 20,
			direction.getOpposite(), this);
		if (i > 0) {
		    i /= 20;
		    te2.provideWater(i * 20);
		    this.useHPWater(i);
		    flag = true;
		}
		i = te2.canProvideWater(this.getFluidAmount(),
			direction.getOpposite(), this);
		if (i > 0) {
		    te2.provideWater(i);
		    this.useLiquid(i);
		    flag = true;
		}
	    }
	}

	if (flag) {
	    sendUpdateToClient();
	}
    }

    boolean markedBlockForUpdate = false;

    @Override
    public void readPacketData(NBTTagCompound tag) {
	super.readPacketData(tag);

	highPotentialEnergyWater = tag.getInteger("hpWater");
	defaultStorage = tag.getInteger("maxFluidAmount");
	extraStorage = tag.getInteger("extraStorage");
	storage = defaultStorage + extraStorage;
	setFluidTankCapacity(storage);
	if (tag.getInteger("fluid") > -1) {
	    setTankAmount(tag.getInteger("fluidAmount"), tag.getInteger("fluid"));
	}
	type = ReservoirType.values()[tag.getInteger("type")];
	if (!markedBlockForUpdate) {
	    markedBlockForUpdate = true;
	    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
	super.writePacketData(tag);

	tag.setInteger("hpWater", getHPWater());
	tag.setInteger("maxFluidAmount", getMaxFluidAmount());
	tag.setInteger("fluidAmount", getFluidAmount());
	if (getFluidTank() != null && getFluidTank().getFluid() != null) {
	    tag.setInteger("fluid", getFluidTank().getFluid().getFluidID());
	} else {
	    tag.setInteger("fluid", -1);
	}
	tag.setInteger("extraStorage", extraStorage);
	if (type == null) {
	    tag.setInteger("type", 0);
	} else {
	    tag.setInteger("type", type.ordinal());
	}
    }

    @Override
    public int getGuiId() {
	return DefaultGuiIds.get("tileEntityReservoir");
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
	// return
	// FluidRegistry.getFluidName(fluid.getID()).contentEquals("water");
	return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
	// /return
	// FluidRegistry.getFluidName(fluid.getID()).contentEquals("water");
	return true;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
	return false;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public short getFacing() {
	return 0;
    }

    @Override
    public void setFacing(short facing) {
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
	return true;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public float getWrenchDropRate() {
	return 1;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
	return getDroppedItemStack();
    }

    @Override
    protected void onTestFailed() {
	setFluidTankCapacity(0);
	highPotentialEnergyWater = 0;
    }

    public int getLastAddedWater() {
	return lastAddedWater;
    }

    @Override
    protected void setMaster(TileEntityMultiBlock master) {
	super.setMaster(master);

	/*if(master != null && master instanceof TileEntityReservoir) {
	 TileEntityReservoir r = (TileEntityReservoir) master;
	 upgradeSlot.setParent(r.upgradeSlot);
	 } else {
	 upgradeSlot.setParent(null);
	 }*/
    }

    @Override
    public void onLoaded() {
	if (isServerSide()) {
	    refreshPlugins();
	}

	super.onLoaded();
    }

    @Override
    public void markDirty() {
	super.markDirty();

	if (isServerSide()) {
	    refreshPlugins();
	}
    }

    public void refreshPlugins() {
	extraStorage = 0;
	rainLevel = overLevel = underLevel = 0;

	for (int i = 0; i < this.upgradeSlot.size(); i++) {
	    ItemStack stack = this.upgradeSlot.get(i);

	    if ((stack == null) || (!(stack.getItem() instanceof IUpgrade))) {
		continue;
	    }
	    IUpgrade upgrade = (IUpgrade) stack.getItem();
	    rainLevel += upgrade.getRainAdditionalValue(stack);
	    overLevel += upgrade.getOverworldAdditionalValue(stack);
	    underLevel += upgrade.getUnderworldAdditionalValue(stack);
	    extraStorage += upgrade.getStorageAdditionalValue(stack);
	}

	setFluidTankCapacity(defaultStorage + extraStorage);
    }

    @Override
    public ItemStack getDroppedItemStack() {
	return new ItemStack(this.getBlockType(), 1, type == null ? 0 : type.ordinal());
    }
}
