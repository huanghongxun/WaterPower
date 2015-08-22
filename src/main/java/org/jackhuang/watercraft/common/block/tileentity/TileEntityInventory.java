package org.jackhuang.watercraft.common.block.tileentity;

import java.util.ArrayList;
import java.util.List;

import org.jackhuang.watercraft.common.block.inventory.InventorySlot;
import org.jackhuang.watercraft.common.block.inventory.InventorySlot.InvSide;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class TileEntityInventory extends TileEntityBase implements
		ISidedInventory {
	private final List<InventorySlot> invSlots = new ArrayList<InventorySlot>();

    @Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);

		NBTTagCompound invSlotsTag = nbtTagCompound.getCompoundTag("InvSlots");

		for (InventorySlot invSlot : this.invSlots)
			invSlot.readFromNBT(invSlotsTag.getCompoundTag(invSlot.name));
	}

    @Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);

		NBTTagCompound invSlotsTag = new NBTTagCompound();

		for (InventorySlot invSlot : this.invSlots) {
			NBTTagCompound invSlotTag = new NBTTagCompound();

			invSlot.writeToNBT(invSlotTag);

			invSlotsTag.setTag(invSlot.name, invSlotTag);
		}

		nbtTagCompound.setTag("InvSlots", invSlotsTag);
	}
	
	public List<InventorySlot> getInventorySlots() {
	    return invSlots;
	}

    @Override
	public int getSizeInventory() {
		int ret = 0;
		for (InventorySlot invSlot : this.invSlots)
			ret += invSlot.size();
		return ret;
	}

    @Override
	public ItemStack getStackInSlot(int index) {
		for (InventorySlot invSlot : this.invSlots) {
			if (index < invSlot.size())
				return invSlot.get(index);
			index -= invSlot.size();
		}

		return null;
	}

    @Override
	public ItemStack decrStackSize(int index, int amount) {
		ItemStack itemStack = getStackInSlot(index);
		if (itemStack == null)
			return null;

		if (amount >= itemStack.stackSize) {
			setInventorySlotContents(index, null);

			return itemStack;
		}

		itemStack.stackSize -= amount;

		ItemStack ret = itemStack.copy();
		ret.stackSize = amount;

		return ret;
	}

    @Override
	public ItemStack getStackInSlotOnClosing(int index) {
		ItemStack ret = getStackInSlot(index);

		if (ret != null)
			setInventorySlotContents(index, null);

		return ret;
	}

    @Override
	public void setInventorySlotContents(int index, ItemStack itemStack) {
		for (InventorySlot invSlot : this.invSlots) {
			if (index < invSlot.size()) {
				invSlot.put(index, itemStack);
				break;
			}
			index -= invSlot.size();
		}
	}

    @Override
	public boolean isInvNameLocalized() {
		return false;
	}

    @Override
	public int getInventoryStackLimit() {
		return 64;
	}

    @Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
		return entityPlayer.getDistance(this.xCoord + 0.5D, this.yCoord + 0.5D,
				this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

    @Override
	public boolean isItemValidForSlot(int index, ItemStack itemStack) {
		InventorySlot invSlot = getInvSlot(index);

		return (invSlot != null) && (invSlot.canInput())
				&& (invSlot.accepts(itemStack));
	}

    @Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		int[] ret = new int[getSizeInventory()];

		for (int i = 0; i < ret.length; i++) {
			ret[i] = i;
		}

		return ret;
	}

    @Override
	public boolean canInsertItem(int index, ItemStack itemStack, int side) {
		InventorySlot targetSlot = getInvSlot(index);
		if (targetSlot == null)
			return false;

		if ((!targetSlot.canInput()) || (!targetSlot.accepts(itemStack)))
			return false;
		if ((targetSlot.preferredSide != InventorySlot.InvSide.ANY)
				&& (targetSlot.preferredSide.matches(side)))
			return true;

		for (InventorySlot invSlot : this.invSlots) {
			if ((invSlot != targetSlot)
					&& (invSlot.preferredSide != InventorySlot.InvSide.ANY)
					&& (invSlot.preferredSide.matches(side))
					&& (invSlot.canInput()) && (invSlot.accepts(itemStack))) {
				return false;
			}
		}

		return true;
	}

    @Override
	public boolean canExtractItem(int index, ItemStack itemStack, int side) {
		InventorySlot targetSlot = getInvSlot(index);
		if (targetSlot == null)
			return false;

		if (!targetSlot.canOutput())
			return false;
		if ((targetSlot.preferredSide != InventorySlot.InvSide.ANY)
				&& (targetSlot.preferredSide.matches(side)))
			return true;

		for (InventorySlot invSlot : this.invSlots) {
			if ((invSlot != targetSlot)
					&& (invSlot.preferredSide != InventorySlot.InvSide.ANY)
					&& (invSlot.preferredSide.matches(side))
					&& (invSlot.canOutput())) {
				return false;
			}
		}

		return true;
	}

	public void addInvSlot(InventorySlot invSlot) {
		this.invSlots.add(invSlot);
	}

	private InventorySlot getInvSlot(int index) {
		for (InventorySlot invSlot : this.invSlots) {
			if (index < invSlot.size()) {
				return invSlot;
			}
			index -= invSlot.size();
		}

		return null;
	}

}