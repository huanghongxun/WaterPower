package org.jackhuang.compactwatermills.block.reservoir;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.mutable.MutableObject;
import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.block.turbines.Position;
import org.jackhuang.compactwatermills.gui.DefaultGuiIds;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.inventory.InventorySlotConsumableLiquid;
import org.jackhuang.compactwatermills.inventory.InventorySlotConsumableLiquidByList;
import org.jackhuang.compactwatermills.inventory.InventorySlotOutput;
import org.jackhuang.compactwatermills.tileentity.TileEntityMultiBlock;

public class TileEntityReservoir extends TileEntityMultiBlock {
	public ReservoirType type;
	public Reservoir size;
	//double water;

	private final InventorySlotConsumableLiquid fluidSlot;
	private final InventorySlotOutput outputSlot;

	public TileEntityReservoir(ReservoirType type) {
		super(0);
		this.type = type;

		this.fluidSlot = new InventorySlotConsumableLiquidByList(this,
				"fluidSlot", 1, 1, new Fluid[] { FluidRegistry.WATER });
		addInvSlot(fluidSlot);
		this.outputSlot = new InventorySlotOutput(this, "output", 2, 1);
		addInvSlot(outputSlot);
	}
	
	public InventorySlotConsumableLiquid getFluidSlot() {
		if(isMaster)
			return fluidSlot;
		else if (masterBlock == null)
			return fluidSlot;
		else return ((TileEntityReservoir) masterBlock).fluidSlot;
	}
	
	public InventorySlotOutput getOutputSlot() {
		if(isMaster)
			return outputSlot;
		else if (masterBlock == null)
			return outputSlot;
		else return ((TileEntityReservoir) masterBlock).outputSlot;
	}

	@Override
	public String getInvName() {
		return type.showedName;
	}

	public int getWater() {
		if(!CompactWatermills.isSimulating()) return getTankAmount();
		if (isMaster)
			//return water;
			return getTankAmount();
		else if (masterBlock == null)
			return 0;
		else
			return ((TileEntityReservoir) masterBlock).getTankAmount();

	}

	public void useWater(int use) {
		if (isMaster)
			fluidTank.drain(use, true);
		else if (masterBlock == null)
			return;
		else
			((TileEntityReservoir) masterBlock).fluidTank.drain(use, true);
	}

	public int getMaxWater() {
		if(!CompactWatermills.isSimulating()) return getFluidTankCapacity();
		if (isMaster)
			return getFluidTankCapacity();
		else if (masterBlock == null)
			return 0;
		else
			return ((TileEntityReservoir) masterBlock).getFluidTankCapacity();
	}

	@Override
	protected boolean canBeMaster() {

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

	@Override
	protected ArrayList<TileEntityMultiBlock> test() {
		ArrayList<TileEntityMultiBlock> al = new ArrayList<TileEntityMultiBlock>();

		int length = 1;
		while (length < 65)
			if (Reservoir.isRes(worldObj, xCoord + length, yCoord, zCoord,
					type.ordinal()))
				length++;
			else
				break;
		
		int width = 1;
		while (width < 65)
			if (Reservoir.isRes(worldObj, xCoord, yCoord, zCoord + width,
					type.ordinal()))
				width++;
			else
				break;
		
		int height = 1;
		while (height < 65)
			if (Reservoir.isRes(worldObj, xCoord, yCoord + height, zCoord,
					type.ordinal()))
				height++;
			else
				break;
		
		ArrayList<Position> l1 = Reservoir.getNotHorizontalWall(worldObj,
				xCoord, yCoord, zCoord, length, height, type.ordinal());
		if (l1.size() != 0) {
			size = null;
			return null;
		}
		
		ArrayList<Position> l2 = Reservoir.getNotHorizontalWall(worldObj,
				xCoord, yCoord, zCoord + width - 1, length, height,
				type.ordinal());
		if (l2.size() != 0) {
			size = null;
			return null;
		}
		
		ArrayList<Position> l3 = Reservoir.getNotVerticalWall(worldObj, xCoord,
				yCoord, zCoord, width, height, type.ordinal());
		if (l3.size() != 0) {
			size = null;
			return null;
		}
		
		ArrayList<Position> l4 = Reservoir.getNotVerticalWall(worldObj, xCoord
				+ length - 1, yCoord, zCoord, width, height, type.ordinal());
		if (l4.size() != 0) {
			size = null;
			return null;
		}
		
		ArrayList<Position> l5 = Reservoir.getNotFloor(worldObj, xCoord,
				yCoord, zCoord, length, width, type.ordinal());
		if (l5.size() != 0) {
			size = null;
			return null;
		}
		
		al.addAll(Reservoir.getHorizontalWall(worldObj, xCoord, yCoord, zCoord,
				length, height, type.ordinal()));
		al.addAll(Reservoir.getHorizontalWall(worldObj, xCoord, yCoord, zCoord
				+ width - 1, length, height, type.ordinal()));
		al.addAll(Reservoir.getVerticalWall(worldObj, xCoord, yCoord, zCoord,
				width, height, type.ordinal()));
		al.addAll(Reservoir.getVerticalWall(worldObj, xCoord + length - 1,
				yCoord, zCoord, width, height, type.ordinal()));
		al.addAll(Reservoir.getFloor(worldObj, xCoord, yCoord, zCoord, length,
				width, type.ordinal()));
		

		size = new Reservoir(length, width, height,
				Reservoir.getNonAirBlock(worldObj, xCoord, yCoord, zCoord, length, width, height));

		setFluidTankCapacity(size.getCapacity() * type.capacity);
		return al;
	}

	@Override
	protected void onUpdate() {

		
		LogHelper.debugLog("maxwater=" + getMaxWater());
		
		if (!isMaster)
			return;
		if (size == null)
			return;
		int weather = worldObj.isThundering() ? 2 : worldObj.isRaining() ? 1
				: 0;
		int biomeID = worldObj.getBiomeGenForCoords(xCoord, yCoord).biomeID;
		double biomeGet = 0, biomePut = 0;
		if (biomeID == BiomeGenBase.beach.biomeID) {
			biomeGet = 1;
			biomePut = 0.75;
		} else if (biomeID == BiomeGenBase.forest.biomeID) {
			biomeGet = 1;
			biomePut = 1;
		} else if (biomeID == BiomeGenBase.river.biomeID) {
			biomeGet = 1;
			biomePut = 0.75;
		} else if (biomeID == BiomeGenBase.forestHills.biomeID) {
			biomeGet = 1;
			biomePut = 0.75;
		} else if (biomeID == BiomeGenBase.extremeHills.biomeID) {
			biomeGet = 0.75;
			biomePut = 1;
		} else if (biomeID == BiomeGenBase.extremeHillsEdge.biomeID) {
			biomeGet = 0.75;
			biomePut = 1;
		} else if (biomeID == BiomeGenBase.ocean.biomeID) {
			biomeGet = 1.2;
			biomePut = 0.75;
		} else if (biomeID == BiomeGenBase.plains.biomeID) {
			biomeGet = 0.75;
			biomePut = 1;
		} else if (biomeID == BiomeGenBase.mushroomIsland.biomeID) {
			biomeGet = 1.2;
			biomePut = 0.75;
		} else if (biomeID == BiomeGenBase.mushroomIslandShore.biomeID) {
			biomeGet = 1.2;
			biomePut = 0.75;
		} else if (biomeID == BiomeGenBase.desert.biomeID) {
			biomeGet = 0;
			biomePut = 4;
		} else if (biomeID == BiomeGenBase.desertHills.biomeID) {
			biomeGet = 0;
			biomePut = 4;
		} else if (biomeID == BiomeGenBase.frozenOcean.biomeID) {
			biomeGet = 1.2;
			biomePut = 0.5;
		} else if (biomeID == BiomeGenBase.frozenRiver.biomeID) {
			biomeGet = 1.2;
			biomePut = 0.5;
		} else if (biomeID == BiomeGenBase.iceMountains.biomeID) {
			biomeGet = 1;
			biomePut = 0.5;
		} else if (biomeID == BiomeGenBase.icePlains.biomeID) {
			biomeGet = 1;
			biomePut = 0.5;
		} else if (biomeID == BiomeGenBase.jungle.biomeID) {
			biomeGet = 1.5;
			biomePut = 0.5;
		} else if (biomeID == BiomeGenBase.jungleHills.biomeID) {
			biomeGet = 1.5;
			biomePut = 0.5;
		} else if (biomeID == BiomeGenBase.swampland.biomeID) {
			biomeGet = 1.2;
			biomePut = 0.75;
		} else if (biomeID == BiomeGenBase.taiga.biomeID) {
			biomeGet = 1;
			biomePut = 0.75;
		} else if (biomeID == BiomeGenBase.taigaHills.biomeID) {
			biomeGet = 1;
			biomePut = 0.75;
		} else if (biomeID == BiomeGenBase.hell.biomeID) {
			biomeGet = 0;
			biomePut = 4;
		}
		
		int length = size.getLength() - 2;
		int width = size.getWidth() - 2;
		int area = length * width;
		int cover = Reservoir.getCoverBlock(worldObj, xCoord, yCoord + size.getHeight(), zCoord, size.getLength(), size.getWidth());
		
		int addWater = (int) ((area - cover) * 2 * weather * biomeGet);

		if (biomeID == BiomeGenBase.ocean.biomeID
				|| biomeID == BiomeGenBase.river.biomeID) {
			if (yCoord < 64)
				addWater += length * width * 0.5;
		}

		int delWater = (int) (weather == 0 ? area * 0.02
				* biomePut : 0);

		fluidTank.fill(new FluidStack(FluidRegistry.WATER, addWater * CompactWatermills.updateTick), true);
		fluidTank.drain(delWater * CompactWatermills.updateTick, true);
		
		sendUpdateToClient();
		
		for(TileEntityMultiBlock b : blockList) {
			b.sendUpdateToClient();
		}
		
		//LogHelper.log("?" + this + " " + getWater());
		//water += (addWater - delWater) * CompactWatermills.updateTick;
		//if (water < 0)
		//	water = 0;
		//if (water > getMaxWater())
		//	water = getMaxWater();
		// LogHelper.log("water=" + water);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if(worldObj == null || worldObj.isRemote) return;

		boolean needsInvUpdate = false;

		if (needsFluid()) {
			MutableObject<ItemStack> output = new MutableObject<ItemStack>();

			if ((this.getFluidSlot().transferToTank(this.fluidTank, output, true))
					&& ((output.getValue() == null) || (this.outputSlot
							.canAdd(output.getValue())))) {
				needsInvUpdate = this.getFluidSlot().transferToTank(this.fluidTank,
						output, false);

				if (output.getValue() != null)
					this.getOutputSlot().add(output.getValue());
			}

		}

		if (needsInvUpdate) {
			onInventoryChanged();
		}
	}
	
	@Override
	public void readPacketData(NBTTagCompound tag) {
		super.readPacketData(tag);
		
		setFluidTankCapacity(tag.getInteger("maxWater"));
		setTankAmount(tag.getInteger("water"), FluidRegistry.WATER.getID());
	}
	
	@Override
	public void writePacketData(NBTTagCompound tag) {
		super.writePacketData(tag);
		
		tag.setInteger("maxWater", getMaxWater());
		tag.setInteger("water", getWater());
	}

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntityReservoir");
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		//return FluidRegistry.getFluidName(fluid.getID()).contentEquals("water");
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		///return FluidRegistry.getFluidName(fluid.getID()).contentEquals("water");
		return true;
	}
}
