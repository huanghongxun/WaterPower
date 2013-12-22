package org.jackhuang.compactwatermills.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.network.NetworkHelper;
import ic2.api.tile.IWrenchable;

import java.util.List;
import java.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.Icon;

import org.jackhuang.compactwatermills.block.BlockTextureStitched;

public abstract class TileEntityBlock extends TileEntityInventory implements
		INetworkDataProvider, INetworkUpdateListener, IWrenchable {
	private short facing = 0;

	public boolean prevActive = false;
	public short prevFacing = 0;

	public boolean loaded = false;

	@SideOnly(Side.CLIENT)
	private Icon[] lastRenderIcons;

	@Override
	public void validate() {
		super.validate();

		onLoaded();
	}

	@Override
	public void invalidate() {
		if (this.loaded)
			onUnloaded();

		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		if (this.loaded)
			onUnloaded();

		super.onChunkUnload();
	}

	public void onLoaded() {
		this.loaded = true;
	}

	public void onUnloaded() {
		this.loaded = false;
	}

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
			this.lastRenderIcons = new Icon[6];

		for (int side = 0; side < 6; side++) {
			this.lastRenderIcons[side] = block.getBlockTexture(this.worldObj,
					this.xCoord, this.yCoord, this.zCoord, side);
		}
	}

	public short getFacing() {
		return this.facing;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> getNetworkedFields() {
		List ret = new Vector(1);

		ret.add("facing");

		return ret;
	}

	public void onNetworkUpdate(String field) {
		if (field.equals("facing") && this.prevFacing != this.facing) {
			Block block = getBlockType();

			if (this.lastRenderIcons != null) {
				for (int side = 0; side < 6; side++) {
					Icon oldIcon = this.lastRenderIcons[side];
					if ((oldIcon instanceof BlockTextureStitched))
						oldIcon = ((BlockTextureStitched) oldIcon)
								.getRealTexture();

					Icon newIcon = block.getBlockTexture(this.worldObj,
							this.xCoord, this.yCoord, this.zCoord, side);
					if ((newIcon instanceof BlockTextureStitched))
						newIcon = ((BlockTextureStitched) newIcon)
								.getRealTexture();
				}
			}

			if (this.prevFacing != this.facing) {
				this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord,
						this.zCoord);
			}

			this.prevFacing = this.facing;
		}
	}

	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return facing != side;
	}

	public void setFacing(short facing) {
		this.facing = facing;

		if (this.prevFacing != facing)
			NetworkHelper.updateTileEntityField(this, "facing");

		this.prevFacing = facing;
	}

	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return true;
	}

	public float getWrenchDropRate() {
		return 1.0F;
	}

	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(this.worldObj.getBlockId(this.xCoord, this.yCoord,
				this.zCoord), 1, this.worldObj.getBlockMetadata(this.xCoord,
				this.yCoord, this.zCoord));
	}

	public void onBlockBreak(int id, int meta) {
	}
}