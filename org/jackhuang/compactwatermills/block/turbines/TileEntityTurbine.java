package org.jackhuang.compactwatermills.block.turbines;

import org.jackhuang.compactwatermills.DefaultGuiIds;
import org.jackhuang.compactwatermills.InventorySlot;
import org.jackhuang.compactwatermills.TileEntityBaseGenerator;
import org.jackhuang.compactwatermills.InventorySlot.Access;
import org.jackhuang.compactwatermills.rotors.ItemRotor;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TileEntityTurbine extends TileEntityBaseGenerator {
	
	public static int maxOutput = 32767;

	public TileEntityTurbine() {
		super(32767, 32767);
		addInvSlot(new InventorySlot(this, "rotor", 0, Access.IO, 1));
	}
	
	private void getReservoir(World world, int x, int y, int z) {
		
	}

	protected double setOutput(World world, int x, int y, int z) {
		return 32767;
	}

	@Override
	public String getInvName() {
		return "水轮机";
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

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntityTurbine");
	}

}
/*
package org.jackhuang.compactwatermills.block.turbines;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.network.NetworkHelper;
import ic2.api.tile.IWrenchable;

import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.ContainerBase;
import org.jackhuang.compactwatermills.IHasGUI;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.rotors.ItemRotor;

import net.minecraft.client.gui.GuiScreen;
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

public class TileEntityTurbine extends TileEntity implements IWrenchable,
		IEnergySource, INetworkDataProvider, INetworkUpdateListener, IInventory,
		IHasGUI {
	
	public static int maxOutput = 32767;

	public TileEntityTurbine() {
		tick = random.nextInt(CompactWatermills.updateTick);
		inventoryContent = new ItemStack[2];
	}

	private Random random = new Random();

	private boolean initialized;

	private int tick;

	private boolean compatibilityMode;

	private double output = 0;

	private ItemStack[] inventoryContent;

	private short facing = 2;

	private short prevFacing = 2;

	@Override
	public short getFacing() {
		return facing;
	}

	public double getOutputUntilNexttTick() {
		if (output > maxOutput) {
			output -= maxOutput;
			if (output > 2147483647)
				output = 2147483647;
			return maxOutput;
		} else
			return 0;
	}

	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(CompactWatermills.turbine, 1, 0);
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
			NBTTagCompound nBTTagCompoundTemp = (NBTTagCompound) nBTTagList
					.tagAt(i);
			int slotNumb = nBTTagCompoundTemp.getByte("Slot") & 0xff;
			if (slotNumb >= 0 && slotNumb < inventoryContent.length) {
				inventoryContent[slotNumb] = ItemStack
						.loadItemStackFromNBT(nBTTagCompoundTemp);
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
		return 32767;
	}

	@Override
	public void updateEntity() {
		if (compatibilityMode) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord,
					0, 0);
			compatibilityMode = false;
		}
		if (!initialized && worldObj != null) {
			if (worldObj.isRemote) {
				NetworkHelper.updateTileEntityField(this, "facing");
			} else {
				EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
				MinecraftForge.EVENT_BUS.post(loadEvent);
			}
			initialized = true;
		}
		if (tick-- == 0) {
			output += setOutput(worldObj, xCoord, yCoord, zCoord)
					* CompactWatermills.updateTick;
			tick = CompactWatermills.updateTick;
		}
		if (worldObj.isRemote)
			return;
		if (output > 0) {
			// EnergyTileSourceEvent sourceEvent = new
			// EnergyTileSourceEvent(this,
			// output);
			// MinecraftForge.EVENT_BUS.post(sourceEvent);
			// if (sourceEvent.amount == output) {
			// damageRotor = false;
			// }
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
		LogHelper.debugLog(Level.INFO, "Setting Turbine to facing:" + facing);

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
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
			} else {
				itemstack = inventoryContent[i].splitStack(j);

				if (inventoryContent[i].stackSize == 0) {
					inventoryContent[i] = null;
				}

				onInventoryChanged();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (inventoryContent[i] != null) {
			ItemStack var2 = inventoryContent[i];
			inventoryContent[i] = null;
			return var2;
		} else {
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
		return "水轮机";
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

	@Override
	public ContainerBase getGuiContainer(EntityPlayer paramEntityPlayer) {
		return new ContainerTurbine(paramEntityPlayer.inventory, this);
	}

	@Override
	public GuiScreen getGui(EntityPlayer paramEntityPlayer, boolean paramBoolean) {
		return ClientGUITurbine.makeGUI(paramEntityPlayer.inventory, this);
	}

	@Override
	public void onGuiClosed(EntityPlayer paramEntityPlayer) {
		// TODO Auto-generated method stub
		
	}

}*/
