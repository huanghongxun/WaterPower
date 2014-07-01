package org.jackhuang.compactwatermills.common.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.tile.IWrenchable;

import java.util.List;
import java.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import org.jackhuang.compactwatermills.common.block.BlockTextureStitched;

public abstract class TileEntityBlock extends TileEntityInventory implements IWrenchable {
	private short facing = 0;

	public boolean prevActive = false;
	public short prevFacing = 0;

	@SideOnly(Side.CLIENT)
	private IIcon[] lastRenderIcons;

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		this.prevFacing = this.facing = nbttagcompound.getShort("facing");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setShort("facing", this.facing);
	}

	@SideOnly(Side.CLIENT)
	public void onRender() {
		Block block = getBlockType();

		if (this.lastRenderIcons == null)
			this.lastRenderIcons = new IIcon[6];

		for (int side = 0; side < 6; side++) {
			this.lastRenderIcons[side] = block.getIcon(this.worldObj,
					this.xCoord, this.yCoord, this.zCoord, side);
		}
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

	public void onBlockBreak(int id, int meta) {
	}
}