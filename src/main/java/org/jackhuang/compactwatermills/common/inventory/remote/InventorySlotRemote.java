package org.jackhuang.compactwatermills.common.inventory.remote;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jackhuang.compactwatermills.common.inventory.InventorySlot;
import org.jackhuang.compactwatermills.common.inventory.InventorySlot.Access;
import org.jackhuang.compactwatermills.common.inventory.InventorySlot.InvSide;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityInventory;

public class InventorySlotRemote extends InventorySlot {

	private InventorySlot baseSlot;

	public InventorySlotRemote(TileEntityInventory base, String name,
			Access access, int count) {
		super(base, name, access, count);
	}
	
	public InventorySlotRemote(TileEntityInventory base, String name,
			 Access access, int count, InvSide preferredSide) {
		super(base, name, access, count, preferredSide);
	}
	
	protected InventorySlot getParent() {
		return baseSlot;
	}

	public void setParent(InventorySlot slot) {
		baseSlot = slot;
	}

	@Override
	public void readFromNbt(NBTTagCompound nbtTagCompound) {
	}

	@Override
	public void writeToNbt(NBTTagCompound nbtTagCompound) {
	}

	@Override
	public int size() {
		if (baseSlot == null)
			return super.size();
		else
			return baseSlot.size();
	}

	@Override
	public ItemStack get() {
		if (baseSlot == null)
			return baseSlot.get();
		else
			return baseSlot.get();
	}

	@Override
	public ItemStack get(int index) {
		if (baseSlot == null)
			return baseSlot.get(index);
		else
			return baseSlot.get(index);
	}

	@Override
	public void put(int index, ItemStack content) {
		if (baseSlot == null)
			baseSlot.put(index, content);
		else
			baseSlot.put(index, content);
	}

	@Override
	public void put(ItemStack content) {
		if (baseSlot == null)
			baseSlot.put(content);
		else
			baseSlot.put(content);
	}

	@Override
	public void clear() {
		if (baseSlot == null)
			baseSlot.clear();
		else
			baseSlot.clear();
	}

	@Override
	public boolean accepts(ItemStack itemStack) {
		if (baseSlot == null)
			return baseSlot.accepts(itemStack);
		else
			return baseSlot.accepts(itemStack);
	}

	@Override
	public boolean canInput() {
		if (baseSlot == null)
			return baseSlot.canInput();
		else
			return baseSlot.canInput();
	}

	@Override
	public boolean canOutput() {
		if (baseSlot == null)
			return baseSlot.canOutput();
		else
			return baseSlot.canOutput();
	}

	@Override
	public boolean isEmpty() {
		if (baseSlot == null)
			return baseSlot.isEmpty();
		else
			return baseSlot.isEmpty();
	}

	@Override
	public void organize() {
		if (baseSlot == null)
			baseSlot.organize();
		else
			baseSlot.organize();
	}

	@Override
	public String toString() {
		if (baseSlot == null)
			return "[RemoteInvSlot: base: null, " + super.toString() + "]";
		else
			return "[RemoteInvSlot: base: " + baseSlot + "]";
	}

	@Override
	public ItemStack[] getCopiedContent() {
		if (baseSlot == null)
			return baseSlot.getCopiedContent();
		else
			return baseSlot.getCopiedContent();
	}

	@Override
	public boolean isEquals(ItemStack[] is) {
		if (baseSlot == null)
			return baseSlot.isEquals(is);
		else
			return baseSlot.isEquals(is);
	}
}
