package org.jackhuang.compactwatermills.block.watermills;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.EnergyType;
import org.jackhuang.compactwatermills.client.gui.DefaultGuiIds;
import org.jackhuang.compactwatermills.entity.EntityWaterWheel;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.inventory.RotorInventorySlot;
import org.jackhuang.compactwatermills.item.rotors.ItemRotor;
import org.jackhuang.compactwatermills.tileentity.TileEntityBaseGenerator;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
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
	private EntityWaterWheel wheel;
	private boolean canWheelTurn = false;
	public int waterBlocks;

	public WaterType getType() {
		return type;
	}

	protected double setOutput(World world, int x, int y, int z) {
		double vol = type.length; vol *= vol * vol;
		double waterPercent = (double)waterBlocks / (vol - 1);
		double energy = type.output * waterPercent;
		energy *= tickRotor();
		if(energy > 0.001)
			damageRotor(1);
		return energy;
	}
	
	public boolean hasRotor() {
		return !invSlots.isEmpty() && invSlots.get(0) != null &&
				!invSlots.get(0).isEmpty() && invSlots.get(0).get(0) != null
				&& invSlots.get(0).get(0).getItem() instanceof ItemRotor;
	}
	
	public ItemRotor getRotor() {
		return (ItemRotor) invSlots.get(0).get(0).getItem();
	}
	
	private void damageRotor(int tick) {
		ItemRotor rotor = getRotor();
		rotor.tickRotor(invSlots.get(0).get(0), this, worldObj);
		if (!rotor.type.isInfinite()) {
			if (invSlots.get(0).get(0).getItemDamage()
					+ tick > invSlots.get(0).get(0)
					.getMaxDamage()) {
				invSlots.get(0).put(0, null);
			} else {
				int damage = invSlots.get(0).get(0).getItemDamage()
						+ tick;
				invSlots.get(0).get(0).setItemDamage(damage);
			}
			onInventoryChanged();
		}
	}

	private double tickRotor() {
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
	public void writePacketData(NBTTagCompound tag) {
		super.writePacketData(tag);
		tag.setInteger("waterBlocks", waterBlocks);
	}
	
	@Override
	public void readPacketData(NBTTagCompound tag) {
		super.readPacketData(tag);
		waterBlocks = tag.getInteger("waterBlocks");
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
		return EnergyType.EU;
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		waterBlocks = getWaterBlocks(worldObj, xCoord, yCoord, zCoord, type);
	}

	@Override
	protected void onUpdateClientAndServer() {
		/*if(wheel == null && hasRotor()) {
			EntityWaterWheel entity = new EntityWaterWheel(worldObj, this);
			wheel = entity;
			worldObj.spawnEntityInWorld(entity);
		} else if(wheel != null && !hasRotor()) {
			wheel.destroy();
			wheel = null;
		}
		if(wheel != null) {
			canWheelTurn = !isEnergyFull();
			wheel.parent = this;
			sendUpdateToClient();
		}*/
	}
}
