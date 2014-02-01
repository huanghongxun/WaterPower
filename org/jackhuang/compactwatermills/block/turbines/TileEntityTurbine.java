package org.jackhuang.compactwatermills.block.turbines;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.EnergyType;
import org.jackhuang.compactwatermills.block.reservoir.Reservoir;
import org.jackhuang.compactwatermills.block.reservoir.TileEntityReservoir;
import org.jackhuang.compactwatermills.client.gui.DefaultGuiIds;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.inventory.RotorInventorySlot;
import org.jackhuang.compactwatermills.item.rotors.ItemRotor;
import org.jackhuang.compactwatermills.tileentity.TileEntityBaseGenerator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileEntityTurbine extends TileEntityBaseGenerator {

	public static int maxOutput = 32767;
	public int speed;
	private TurbineType type;
	
	public TileEntityTurbine() {
		type = TurbineType.MK2;
		addInvSlot(new RotorInventorySlot(this));
	}

	public TileEntityTurbine(TurbineType type) {
		super(type.percent, 10000000);
		addInvSlot(new RotorInventorySlot(this));
	}
	
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.speed = nbttagcompound.getInteger("speed");
		if(nbttagcompound.hasKey("type")) {
			type = TurbineType.values()[nbttagcompound.getInteger("type")];
		}
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		
		nbttagcompound.setInteger("speed", this.speed);
		nbttagcompound.setInteger("type", type.ordinal());
	}
	
	private TileEntityReservoir getWater(World world, int x, int y, int z) {
		TileEntityReservoir reservoir = null;
		switch (getFacing()) {
		case 2:
			//z+1
			if(Reservoir.isRes(world, x, y, z+1)) {
				reservoir = (TileEntityReservoir)world.getBlockTileEntity(x, y, z+1);
			}
			break;
		case 5:
			//x-1
			if(Reservoir.isRes(world, x-1, y, z))
				reservoir = (TileEntityReservoir)world.getBlockTileEntity(x-1, y, z);
			break;
		case 3:
			//z-1
			if(Reservoir.isRes(world, x, y, z-1))
				reservoir = (TileEntityReservoir)world.getBlockTileEntity(x, y, z-1);
			break;
		case 4:
			//x+1
			if(Reservoir.isRes(world, x+1, y, z))
				reservoir = (TileEntityReservoir)world.getBlockTileEntity(x+1, y, z);
			break;
		}
		return reservoir;
	}

	protected double setOutput(World world, int x, int y, int z) {
		/*
		double use = Math.min(water, ReservoirType.values()[size.blockType].maxUse);
		double baseEnergy = use * 5000;
		LogHelper.log("Can output?" + canOutput);
		if(!canOutput) return 0;
		double per = tickRotor();
		LogHelper.log("per?" + per);
		if(per > 0) {
			double energy = baseEnergy * per * (speed / 1000);
			LogHelper.log("energy?" + energy);
			LogHelper.log("use?" + use);
			water -= use;
			return energy;
		}
		return 0;*/
		TileEntityReservoir pair = getWater(world, x, y, z);
		if(pair == null) return 0;
		else {
			LogHelper.debugLog("-530.-384" + world.getBlockTileEntity(-530, 71, -384));
			LogHelper.debugLog("res" + pair);
			LogHelper.debugLog("x,y,z=" + pair.xCoord + "," + pair.yCoord + "," + pair.zCoord);
			LogHelper.debugLog("hasmaster=" + pair.masterBlock);
			LogHelper.debugLog("water=" + pair.getWater());
			LogHelper.debugLog("maxuse=" + pair.type.maxUse);
			double use = Math.min(pair.getWater(), pair.type.maxUse);
			LogHelper.debugLog("use=" + use);
			double baseEnergy = use * type.percent / 2048;
			LogHelper.debugLog("baseEnergy" + baseEnergy);
			LogHelper.debugLog("speed" + speed);
			double per = tickRotor();
			LogHelper.debugLog("per=" + per);
			if(per > 0) {
				double energy = baseEnergy * per * ((double)speed / 1000);
				LogHelper.debugLog("energy = " + energy);
				pair.useWater((int)use);
				LogHelper.debugLog("use=" + use);
				return energy;
			}
		}
		return 0;
	}

	private boolean hasRotorImpl() {
		return !invSlots.isEmpty() && invSlots.get(0) != null
				&& !invSlots.get(0).isEmpty() && invSlots.get(0).get(0) != null
				&& invSlots.get(0).get(0).getItem() instanceof ItemRotor;
	}
	
	public boolean hasRotor() {
		return hasRotorImpl();
	}
	
	private ItemRotor getRotor() {
		return (ItemRotor) invSlots.get(0).get(0).getItem();
	}
	
	private void damageRotor(int tick) {
		ItemRotor rotor = getRotor();
		rotor.tickRotor(invSlots.get(0).get(0), this, worldObj);
		if (!rotor.type.isInfinite()) {
			if (invSlots.get(0).get(0).getItemDamage()
					+ tick > invSlots.get(0).get(0)
					.getMaxDamage()) {
				invSlots.set(0, null);
			} else {
				int damage = invSlots.get(0).get(0).getItemDamage()
						+ tick;
				invSlots.get(0).get(0).setItemDamage(damage);
			}
			onInventoryChanged();
		}
	}

	private double tickRotor() {
		//if (water <= 0)
		//	return 0;
		if (hasRotor()) {
			ItemRotor rotor = getRotor();
			if (worldObj.isRemote) {
				return rotor.type.efficiency;
			}

			return rotor.type.efficiency;
		}
		return 0;
	}

	@Override
	public String getInvName() {
		return "水轮机";
	}

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntityTurbine");
	}

	@Override
	protected void onUpdate() {
/*		size = getReservoir(worldObj, xCoord, yCoord, zCoord);
		if (size != null) {
			//this.production = ReservoirType.values()[size.blockType].maxOutput;
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
			LogHelper.log(size.width + "*" + size.length);
			double addWater = size.width * size.length * 0.002 * weather
					* biomeGet;
			
			if (biomeID == BiomeGenBase.ocean.biomeID ||
					biomeID == BiomeGenBase.river.biomeID) {
				if(yCoord < 64)
					addWater += size.width * size.length * 0.0005;
			}

			double delWater = weather == 0 ? size.width * size.length * 0.00002
					* biomePut : 0;

			water += (addWater - delWater) * CompactWatermills.updateTick;
			if (water < 0)
				water = 0;
			if (water > size.getMaxWater())
				water = size.getMaxWater();

			canOutput = false;
			*/
		TileEntityReservoir r = getWater(worldObj, xCoord, yCoord, zCoord);
			if (r != null && r.getWater() > 0 && hasRotor()) {
				//canOutput = true;
				speed += CompactWatermills.updateTick;
				if(speed > 1000)
					speed = 1000;
			} else {
				speed -= CompactWatermills.updateTick;
				if(speed < 0)
					speed = 0;
			}
			if(speed > 0)
				damageRotor(1);
	}
	
	@Override
	public void readPacketData(NBTTagCompound tag) {
		super.readPacketData(tag);
		speed = tag.getInteger("speed");
	}
	
	@Override
	public void writePacketData(NBTTagCompound tag) {
		super.writePacketData(tag);
		tag.setInteger("speed", speed);
	}

	@Override
	protected EnergyType energyType() {
		return EnergyType.EU;
	}

	@Override
	protected void onUpdateClientAndServer() {
		// TODO Auto-generated method stub
		
	}

}