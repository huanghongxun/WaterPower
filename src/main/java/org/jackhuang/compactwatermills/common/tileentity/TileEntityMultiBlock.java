package org.jackhuang.compactwatermills.common.tileentity;

import java.util.ArrayList;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.client.gui.IHasGui;
import org.jackhuang.compactwatermills.common.network.CompactWatermillsPacket;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public abstract class TileEntityMultiBlock extends TileEntityLiquidTankInventory
		implements IHasGui {

	public TileEntityMultiBlock masterBlock;
	protected boolean tested, isMaster;
	private int tick = 0, tick2 = CompactWatermills.updateTick;
	protected ArrayList<TileEntityMultiBlock> blockList;

	public TileEntityMultiBlock(int tankSize) {
		super(tankSize);
		this.tested = CompactWatermills.isSimulating();
	}

	protected void setMaster(TileEntityMultiBlock master) {
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
	public abstract CompactWatermillsPacket serverSendMultiBlocks();
	public abstract ArrayList<TileEntityMultiBlock> clientSetMultiBlocks(CompactWatermillsPacket packet);

	protected void onUpdate() {
	}

	protected boolean canBeMaster() {
		return true;
	}
	
	@Override
	public FluidTank getFluidTank() {
		if(!CompactWatermills.isSimulating()) return fluidTank;
		if(isMaster)
			return fluidTank;
		else if (masterBlock == null)
			return fluidTank;
		else
			return masterBlock.fluidTank;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (worldObj == null || worldObj.isRemote) return;

		if (tick-- == 0) {
			tick = 25;
			if (canBeMaster()) {
				tested = true;
				if(blockList != null) {
					for (TileEntityMultiBlock block : blockList) {
						block.tested = false;
						block.setMaster(null);
						block.isMaster = false;
						block.sendUpdateToClient();
					}
				}
				blockList = test();
				if (blockList == null) {
					tested = false;
					isMaster = false;
					onTestFailed();
				} else {
					for (TileEntityMultiBlock block : blockList) {
						block.tested = true;
						block.setMaster(this);
						block.isMaster = false;
						block.sendUpdateToClient();
					}
					//PacketDispatcher.sendPacketToAllInDimension(serverSendMultiBlocks().getPacket(), this.worldObj.provider.dimensionId);
				}
				isMaster = true;
				
				sendUpdateToClient();
				
			}
		}

		if (tick2-- == 0) {
			tick2 = CompactWatermills.updateTick;
			onUpdate();
		}
	}
	
	protected abstract void onTestFailed();
}
