/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package org.jackhuang.compactwatermills.watermills;

import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.rotors.ItemRotor;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.network.NetworkHelper;
import ic2.api.tile.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

/**
 * 
 * @author Aroma1997
 * 
 */
public class TileEntityWatermill extends TileEntity
	implements IWrenchable, IEnergySource, INetworkDataProvider, INetworkUpdateListener,
		IInventory {
	
	public TileEntityWatermill() {
		this(WaterType.ELV);
	}
	
	public TileEntityWatermill(WaterType type) {
		super();
		this.type = type;
		tick = random.nextInt(CompactWatermills.updateTick);
		inventoryContent = new ItemStack[1];
	}
	
	private static int getWaterBlocks(World world, int x, int y, int z, WaterType type) {
		int waterBlocks = 0;
		
		for (int xTest = -1; xTest <= 1; xTest++) {
			for (int yTest = -1; yTest <= 1; yTest++) {
				for (int zTest = -1; zTest <= 1; zTest++) {
					int id = world.getBlockId(x + xTest, y + yTest, z + zTest);
					if (id != Block.waterMoving.blockID && id != Block.waterStill.blockID) {
			        	continue;
			        }
					waterBlocks++;
				}
			}
		}
		return waterBlocks;
	}
	
	private Random random = new Random();
	
	private WaterType type;
	
	private boolean initialized;
	
	private int tick;
	
	private boolean compatibilityMode;
	
	private double output = 0;
	
	private ItemStack[] inventoryContent;
	
	private short facing = 2;
	
	private short prevFacing = 2;
		
	@Override
	public short getFacing() {
		return 0;
	}
	
	public double getOutputUntilNexttTick() {
		if(output > type.output) {
			output -= type.output;
			if(output > 2147483647) output = 2147483647;
			return type.output;
		} else
			return 0;
	}
	
	public WaterType getType() {
		return type;
	}
	
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(CompactWatermills.waterMill, 1, type.ordinal());
	}
	
	public float getWrenchDropRate() {
		return 1.0F;
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nBTTagCompound) {
		super.readFromNBT(nBTTagCompound);
		NBTTagList nBTTagList = nBTTagCompound.getTagList("Items");
		inventoryContent = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nBTTagList.tagCount(); i++) {
			NBTTagCompound nBTTagCompoundTemp = (NBTTagCompound) nBTTagList.tagAt(i);
			int slotNumb = nBTTagCompoundTemp.getByte("Slot") & 0xff;
			if (slotNumb >= 0 && slotNumb < inventoryContent.length) {
				inventoryContent[slotNumb] = ItemStack.loadItemStackFromNBT(nBTTagCompoundTemp);
			}
		}
		
		prevFacing = facing = nBTTagCompound.getShort("facing");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nBTTagCompound) {
		super.writeToNBT(nBTTagCompound);
		NBTTagList nBTTagList = new NBTTagList();
		for (int i = 0; i < inventoryContent.length; i++) {
			if (inventoryContent[i] != null) {
				NBTTagCompound nBTTagCompoundTemp = new NBTTagCompound();
				nBTTagCompoundTemp.setByte("Slot", (byte) i);
				inventoryContent[i].writeToNBT(nBTTagCompoundTemp);
				nBTTagList.appendTag(nBTTagCompoundTemp);
			}
		}
		
		nBTTagCompound.setTag("Items", nBTTagList);
		
		nBTTagCompound.setShort("facing", facing);
	}
	
	
	private double setOutput(World world, int x, int y, int z) {		
		int waterBlocks = getWaterBlocks(world, x, y, z, type);
		double energy = type.output * waterBlocks / 26;
		energy *= (1 + tickRotor());
		return energy;
	}
	
	private double tickRotor() {
		if (inventoryContent[0] != null && inventoryContent[0].getItem() instanceof ItemRotor) {
			ItemRotor rotor = (ItemRotor) inventoryContent[0].getItem();
			if (worldObj.isRemote) {
				return rotor.type.efficiency;
			}
			rotor.tickRotor(inventoryContent[0], this, worldObj);
			if (!rotor.type.isInfinite()) {
				if (inventoryContent[0].getItemDamage() + CompactWatermills.updateTick > inventoryContent[0].getMaxDamage()) {
					inventoryContent[0] = null;
				}
				else {
					int damage = inventoryContent[0].getItemDamage() + CompactWatermills.updateTick;
					inventoryContent[0].setItemDamage(damage);
				}
				onInventoryChanged();
			}

			return rotor.type.efficiency;
		}
		return 0;
	}
	
	@Override
	public void updateEntity() {
		if (compatibilityMode) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord,
				type.ordinal(), 0);
			compatibilityMode = false;
		}
		if (! initialized && worldObj != null) {
			if (worldObj.isRemote) {
				NetworkHelper.updateTileEntityField(this, "facing");
			}
			else {
				EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
				MinecraftForge.EVENT_BUS.post(loadEvent);
			}
			initialized = true;
		}
		if (tick-- == 0) {
			output += setOutput(worldObj, xCoord, yCoord, zCoord) * CompactWatermills.updateTick;
			tick = CompactWatermills.updateTick;
		}
		if (worldObj.isRemote) return;
		if (output > 0) {
//			EnergyTileSourceEvent sourceEvent = new EnergyTileSourceEvent(this,
//				output);
//			MinecraftForge.EVENT_BUS.post(sourceEvent);
//			if (sourceEvent.amount == output) {
//				damageRotor = false;
//			}
		}
	}	
	
	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return true;
	}
	
	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return facing != side && side != 0;
	}

	@Override
	public void setFacing(short facing) {
		if (facing == 0) {
			return;
		}
		this.facing = facing;
		LogHelper.debugLog(Level.INFO, "Setting Watermill to facing:" + facing);
		
		if (prevFacing != facing) {
			NetworkHelper.updateTileEntityField(this, "facing");
		}
		
		prevFacing = facing;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return true;
	}

	@Override
	public double getOfferedEnergy() {
		return this.getOutputUntilNexttTick();
	}

	@Override
	public void drawEnergy(double amount) {
	}

	@Override
	public void onNetworkUpdate(String field) {
		if (field.equals("facing") && prevFacing != facing) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			prevFacing = facing;
		}
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public List<String> getNetworkedFields() {
		List list = new Vector(2);
		list.add("facing");
		
		return list;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventoryContent[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (inventoryContent[i] != null) {
			ItemStack itemstack;
			
			if (inventoryContent[i].stackSize <= j) {
				itemstack = inventoryContent[i];
				inventoryContent[i] = null;
				onInventoryChanged();
				return itemstack;
			}
			else {
				itemstack = inventoryContent[i].splitStack(j);
				
				if (inventoryContent[i].stackSize == 0) {
					inventoryContent[i] = null;
				}
				
				onInventoryChanged();
				return itemstack;
			}
		}
		else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (inventoryContent[i] != null) {
			ItemStack var2 = inventoryContent[i];
			inventoryContent[i] = null;
			return var2;
		}
		else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventoryContent[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return type.showedName;
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
		NetworkHelper.updateTileEntityField(this, "inventoryContent");
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		if (itemStack == null) {
			return false;
		}
		if (itemStack.getItem() instanceof ItemRotor) {
			return true;
		}
		return false;
	}
}
