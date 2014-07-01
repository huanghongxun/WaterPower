package org.jackhuang.compactwatermills.common.tileentity;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.jackhuang.compactwatermills.api.IWaterReceiver;

public abstract class TileEntityWaterMachine extends TileEntityInventory
		implements IWaterReceiver, IWrenchable {
	private short facing = 0;
	public short prevFacing = 0;

	public int water = 0, maxWater;
	
	public TileEntityWaterMachine(int maxWater) {
		this.maxWater = maxWater;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.water = nbttagcompound.getInteger("water");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("water", this.water);
	}

	@Override
	public int canProvideWater(int water, ForgeDirection side, TileEntity provider) {
		int need = maxWater - this.water;
		need = Math.min(need, water);
		
		sendUpdateToClient();
		return need;
	}

	public short getFacing() {
		return this.facing;
	}

	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return facing != side;
	}

	public void setFacing(short facing) {
		this.facing = facing;

		if (this.prevFacing != facing)
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);

		this.prevFacing = facing;
	}

	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return true;
	}

	public float getWrenchDropRate() {
		return 1.0F;
	}

	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(this.worldObj.getBlock(this.xCoord, this.yCoord,
				this.zCoord), 1, this.worldObj.getBlockMetadata(this.xCoord,
				this.yCoord, this.zCoord));
	}
	
	@Override
	public void provideWater(int provide) {
		this.water += provide;
	}
	
	@Override
	public void readPacketData(NBTTagCompound tag) {
		super.readPacketData(tag);
		
		this.water = tag.getInteger("water");
	}

	@Override
	public void writePacketData(NBTTagCompound tag) {
		super.writePacketData(tag);

		tag.setInteger("water", this.water);
	}
}
