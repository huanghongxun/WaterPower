package org.jackhuang.compactwatermills.tileentity;

import java.util.ArrayList;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.gui.IHasGUI;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityMultiBlock extends TileEntityLiquidTankInventory
		implements IHasGUI {

	public TileEntityMultiBlock masterBlock;
	protected boolean tested, isMaster;
	private int tick = 0, tick2 = CompactWatermills.updateTick;
	protected ArrayList<TileEntityMultiBlock> blockList;

	public TileEntityMultiBlock(int tankSize) {
		super(tankSize);
		this.tested = CompactWatermills.isSimulating();
	}

	private void setMaster(TileEntityMultiBlock master) {
		this.masterBlock = master;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);

		isMaster = nbtTagCompound.getBoolean("master");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);

		nbtTagCompound.setBoolean("master", isMaster);
	}

	protected abstract ArrayList<TileEntityMultiBlock> test();

	protected void onUpdate() {
	}

	protected boolean canBeMaster() {
		return true;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (worldObj == null || worldObj.isRemote) return;

		if (tick-- == 0) {
			tick = 25;
			if (canBeMaster()) {
				tested = true;
				isMaster = true;
				blockList = test();
				if (blockList == null) {
					tested = false;
					isMaster = false;
				} else {
					for (TileEntityMultiBlock block : blockList) {
						block.tested = true;
						block.setMaster(this);
						block.sendUpdateToClient();
					}

				}
			}
		}

		if (tick2-- == 0) {
			tick2 = CompactWatermills.updateTick;
			onUpdate();
		}
	}
	

}
