package org.jackhuang.compactwatermills.common.inventory;

import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jackhuang.compactwatermills.common.tileentity.TileEntityInventory;

public class InventorySlotDischarge extends InventorySlot {
	public int tier;

	public InventorySlotDischarge(TileEntityInventory base,
			int tier) {
		this(base, tier, InventorySlot.InvSide.ANY);
	}

	public InventorySlotDischarge(TileEntityInventory base,
			int tier, InventorySlot.InvSide preferredSide) {
		super(base, "discharge", InventorySlot.Access.IO, 1,
				preferredSide);

		this.tier = tier;
	}

	public boolean accepts(ItemStack itemStack) {
		Item item = itemStack.getItem();

		if ((item instanceof IElectricItem))
			return (((IElectricItem) item).canProvideEnergy(itemStack))
					&& (((IElectricItem) item).getTier(itemStack) <= this.tier);
		if (item == Items.redstone) {
			return true;
		}

		return (Info.itemEnergy.getEnergyValue(itemStack) > 0)
				&& ((!(item instanceof IElectricItem)) || (((IElectricItem) item)
						.getTier(itemStack) <= this.tier));
	}

	public IElectricItem getItem() {
		ItemStack itemStack = get(0);
		if (itemStack == null)
			return null;

		return (IElectricItem) itemStack.getItem();
	}

	public int discharge(int amount, boolean ignoreLimit) {
		ItemStack itemStack = get(0);
		if (itemStack == null)
			return 0;

		int energyValue = Info.itemEnergy.getEnergyValue(itemStack);
		if (energyValue == 0)
			return 0;

		Item item = itemStack.getItem();

		if ((item instanceof IElectricItem)) {
			IElectricItem elItem = (IElectricItem) item;

			if ((!elItem.canProvideEnergy(itemStack))
					|| (elItem.getTier(itemStack) > this.tier)) {
				return 0;
			}

			return ElectricItem.manager.discharge(itemStack, amount, this.tier,
					ignoreLimit, false);
		}
		itemStack.stackSize -= 1;
		if (itemStack.stackSize <= 0)
			put(0, null);

		return energyValue;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}
}
