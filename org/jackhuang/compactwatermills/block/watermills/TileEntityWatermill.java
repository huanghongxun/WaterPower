package org.jackhuang.compactwatermills.block.watermills;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.EnergyType;
import org.jackhuang.compactwatermills.gui.DefaultGuiIds;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.inventory.RotorInventorySlot;
import org.jackhuang.compactwatermills.item.rotors.ItemRotor;
import org.jackhuang.compactwatermills.tileentity.TileEntityBaseGenerator;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

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
		addInvSlot(new RotorInventorySlot(this));
	}

	private static int getWaterBlocks(World world, int x, int y, int z,
			WaterType type) {
		int waterBlocks = 0;

		int range = type.length / 2;
		for (int xTest = -range; xTest <= range; xTest++) {
			for (int yTest = -range; yTest <= range; yTest++) {
				for (int zTest = -range; zTest <= range; zTest++) {
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
			LogHelper.debugLog("WATERMILL!");
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
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntityWatermill");
	}

	@Override
	protected EnergyType energyType() {
		return EnergyType.MJ;
	}
}
