package org.jackhuang.compactwatermills;

import java.util.Random;

import org.jackhuang.compactwatermills.util.StackUtil;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public abstract class InventorySlotConsumable extends InventorySlot {
	private static final Random random = new Random();

	public InventorySlotConsumable(TileEntityInventory base, String name,
			int oldStartIndex, int count) {
		super(base, name, oldStartIndex, InventorySlot.Access.I, count,
				InventorySlot.InvSide.TOP);
	}

	public InventorySlotConsumable(TileEntityInventory base, String name,
			int oldStartIndex, InventorySlot.Access access, int count,
			InventorySlot.InvSide preferredSide) {
		super(base, name, oldStartIndex, access, count, preferredSide);
	}

	public abstract boolean accepts(ItemStack paramItemStack);

	public boolean canOutput() {
		return (super.canOutput())
				|| ((this.access != InventorySlot.Access.NONE)
						&& (get() != null) && (!accepts(get())));
	}

	public ItemStack consume(int amount) {
		return consume(amount, false, false);
	}

	public ItemStack consume(int amount, boolean simulate,
			boolean consumeContainers) {
		ItemStack ret = null;

		for (int i = 0; i < size(); i++) {
			ItemStack itemStack = get(i);

			if ((itemStack == null)
					|| (itemStack.stackSize < 1)
					|| (!accepts(itemStack))
					|| ((ret != null) && (!StackUtil.isStackEqual(itemStack,
							ret)))
					|| ((itemStack.stackSize != 1) && (!consumeContainers) && (itemStack
							.getItem().hasContainerItem()))) {
				continue;
			}

			int currentAmount = Math.min(amount, itemStack.stackSize);

			amount -= currentAmount;

			if (!simulate) {
				if (itemStack.stackSize == currentAmount) {
					if ((!consumeContainers)
							&& (itemStack.getItem().hasContainerItem()))
						put(i,
								itemStack.getItem().getContainerItemStack(
										itemStack));
					else
						put(i, null);
				} else {
					itemStack.stackSize -= currentAmount;
				}
			}

			if (ret == null)
				ret = StackUtil.copyWithSize(itemStack, currentAmount);
			else {
				ret.stackSize += currentAmount;
			}

			if (amount == 0) {
				break;
			}
		}
		return ret;
	}

	public ItemStack damage(int amount) {
		return damage(amount, null);
	}

	public ItemStack damage(int amount, EntityLivingBase src) {
		ItemStack ret = null;
		int damageApplied = 0;

		for (int i = 0; i < size(); i++) {
			ItemStack itemStack = get(i);

			if ((itemStack == null)
					|| (!accepts(itemStack))
					|| (!itemStack.getItem().isDamageable())
					|| ((ret != null) && ((itemStack.itemID != ret.itemID) || (!ItemStack
							.areItemStackTagsEqual(itemStack, ret))))) {
				continue;
			}
			int currentAmount = Math.min(amount, itemStack.getMaxDamage()
					- itemStack.getItemDamage());

			damageApplied += currentAmount;
			amount -= currentAmount;

			if (src != null)
				itemStack.damageItem(currentAmount, src);
			else {
				itemStack.attemptDamageItem(currentAmount, random);
			}

			if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) {
				itemStack.stackSize -= 1;
				itemStack.setItemDamage(0);
			}

			if (itemStack.stackSize == 0)
				put(i, null);
			else {
				i--;
			}

			if (ret == null)
				ret = itemStack.copy();

			if (amount == 0) {
				break;
			}
		}
		ret.stackSize = (damageApplied / ret.getMaxDamage());
		ret.setItemDamage(damageApplied % ret.getMaxDamage());

		return ret;
	}
}