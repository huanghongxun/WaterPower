package org.jackhuang.compactwatermills.block.watermills;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.DefaultGuiIds;
import org.jackhuang.compactwatermills.InventorySlot;
import org.jackhuang.compactwatermills.TileEntityBaseGenerator;
import org.jackhuang.compactwatermills.InventorySlot.Access;
import org.jackhuang.compactwatermills.rotors.ItemRotor;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * 
 * @author jackhuang1998
 * 
 */
public class TileEntityWatermill extends TileEntityBaseGenerator {

	public TileEntityWatermill() {
		this(WaterType.ELV);
	}

	public TileEntityWatermill(WaterType type) {
		super(type.output, 32767);
		this.type = type;
		addInvSlot(new InventorySlot(this, "rotor", 0, Access.IO, 1));
	}

	private static int getWaterBlocks(World world, int x, int y, int z,
			WaterType type) {
		int waterBlocks = 0;

		for (int xTest = -1; xTest <= 1; xTest++) {
			for (int yTest = -1; yTest <= 1; yTest++) {
				for (int zTest = -1; zTest <= 1; zTest++) {
					int id = world.getBlockId(x + xTest, y + yTest, z + zTest);
					if (id != Block.waterMoving.blockID
							&& id != Block.waterStill.blockID) {
						continue;
					}
					waterBlocks++;
				}
			}
		}
		return waterBlocks;
	}

	private WaterType type;

	public WaterType getType() {
		return type;
	}

	protected double setOutput(World world, int x, int y, int z) {
		int waterBlocks = getWaterBlocks(world, x, y, z, type);
		double energy = type.output * waterBlocks / 26;
		energy *= (1 + tickRotor());
		return energy;
	}

	private double tickRotor() {
		if (!invSlots.isEmpty() && invSlots.get(0) != null &&
				!invSlots.get(0).isEmpty() && invSlots.get(0).get(0) != null
				&& invSlots.get(0).get(0).getItem() instanceof ItemRotor) {
			ItemRotor rotor = (ItemRotor) invSlots.get(0).get(0).getItem();
			if (worldObj.isRemote) {
				return rotor.type.efficiency;
			}
			rotor.tickRotor(invSlots.get(0).get(0), this, worldObj);
			if (!rotor.type.isInfinite()) {
				if (invSlots.get(0).get(0).getItemDamage()
						+ CompactWatermills.updateTick > invSlots.get(0).get(0)
						.getMaxDamage()) {
					invSlots.set(0, null);
				} else {
					int damage = invSlots.get(0).get(0).getItemDamage()
							+ CompactWatermills.updateTick;
					invSlots.get(0).get(0).setItemDamage(damage);
				}
				onInventoryChanged();
			}

			return rotor.type.efficiency;
		}
		return 0;
	}

	@Override
	public String getInvName() {
		return type.showedName;
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
		return DefaultGuiIds.get("tileEntityWatermill");
	}
}
