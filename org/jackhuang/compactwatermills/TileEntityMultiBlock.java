package org.jackhuang.compactwatermills;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityMultiBlock extends TileEntityLiquidTankInventory
		implements IHasGUI {

	public TileEntityMultiBlock masterBlock;
	private int uuid, uuidMaster;
	protected boolean tested, isMaster;
	private int tick = 0, tick2 = CompactWatermills.updateTick;

	public TileEntityMultiBlock(int tankSize) {
		super(tankSize);
		this.tested = CompactWatermills.isSimulating();
		this.uuid = UUIDManager.registerUUID();
		this.uuidMaster = -1;
	}

	protected void onMasterBlockChanged() {
	}

	private void setMaster(TileEntityMultiBlock master) {
		this.masterBlock = master;

		if (this.uuidMaster != -1 && !(this.uuidMaster == master.uuid)) {
			onMasterBlockChanged();
		}
		this.uuidMaster = master.uuid;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);

		isMaster = nbtTagCompound.getBoolean("master");
		uuid = nbtTagCompound.getInteger("uuid");
		uuidMaster = nbtTagCompound.getInteger("uuidMaster");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);

		nbtTagCompound.setBoolean("master", isMaster);
		nbtTagCompound.setInteger("uuid", uuid);
		nbtTagCompound.setInteger("uuidMaster", uuidMaster);
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
		
		//LogHelper.log((worldObj == null)  + "||" +  worldObj.isRemote);

		if (!CompactWatermills.isSimulating()) return;
		//LogHelper.log("master=" + masterBlock + "this=" + this);

		//LogHelper.log("before");
		if (tick-- == 0) {
			tick = 25;
			//LogHelper.log("before2");
			if (canBeMaster()) {
				tested = true;
				isMaster = true;
				//LogHelper.log("before3");
				ArrayList<TileEntityMultiBlock> al = test();
				if (al == null) {
					//LogHelper.log("HEHE");
					tested = false;
					isMaster = false;
				} else {
					//LogHelper.log("HAHA");
					for (TileEntityMultiBlock block : al) {
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
