package org.jackhuang.watercraft.common.block.tileentity;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.tile.IWrenchable;

import java.util.List;
import java.util.Vector;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.common.block.IDroppable;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

@InterfaceList({
        @Interface(iface = "ic2.api.tile.IWrenchable", modid = Mods.IDs.IndustrialCraft2API, striprefs = true)})
public abstract class TileEntityBlock extends TileEntityLiquidTankInventory implements IWrenchable, IDroppable {
	
	public TileEntityBlock(int tanksize) {
		super(tanksize);
	}

	private short facing = 0;

	public boolean prevActive = false;
	private short prevFacing = 0;
	private boolean needsUpdate = false;

	@SideOnly(Side.CLIENT)
	private IIcon[] lastRenderIcons;

	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);

		this.prevFacing = this.facing = tag.getShort("facing");
	}

	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		tag.setShort("facing", this.facing);
	}
	
	@Override
	public void readPacketData(NBTTagCompound tag) {
		super.readPacketData(tag);

		this.prevFacing = this.facing = tag.getShort("facing");
		needsUpdate = tag.getBoolean("needsUpdate");
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if(needsUpdate && !isServerSide()) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			needsUpdate = false;
		}
	}
	
	@Override
	public void writePacketData(NBTTagCompound tag) {
		super.writePacketData(tag);

		tag.setShort("facing", this.facing);
		tag.setBoolean("needsUpdate", needsUpdate);
		if(needsUpdate) needsUpdate = false;
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
	
	/* ---------------------------------------
	 * 
	 * WRENCH(INDUSTRIAL CRAFT 2) INTEGRATION BEGINS
	 * 
	 * ---------------------------------------
	 */
    
    public short getPrevFacing() {
        return this.prevFacing;
    }

    @Override
	@Method(modid = Mods.IDs.IndustrialCraft2API)
	public short getFacing() {
		return getDirection();
	}

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return facing != side;
	}

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
	public void setFacing(short facing) {
        setDirection(facing);
	}

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public float getWrenchDropRate() {
        return 1.0F;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return getDroppedItemStack();
    }
    
    /* ---------------------------------------
     * 
     * WRENCH(INDUSTRIAL CRAFT 2) INTEGRATION ENDS
     * 
     * ---------------------------------------
     */
    
    public short getDirection() {
        return facing;
    }
    
    public boolean setDirection(int side) {
        this.facing = (short)side;

        if (this.prevFacing != facing) {
            if (isServerSide()) {
                needsUpdate = true;
                sendUpdateToClient();
            } else {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
        }

        boolean flag = prevFacing != facing;
        this.prevFacing = facing;
        return flag;
    }

	public void onBlockBreak(int id, int meta) {
	}
	
	@Override
	public boolean canDrain(ForgeDirection paramForgeDirection, Fluid paramFluid) {
		return false;
	}
	
	@Override
	public boolean canFill(ForgeDirection paramForgeDirection, Fluid paramFluid) {
		return false;
	}
	
	@Override
	public ItemStack getDroppedItemStack() {
	    return new ItemStack(this.worldObj.getBlock(this.xCoord, this.yCoord,
                this.zCoord), 1, this.worldObj.getBlockMetadata(this.xCoord,
                this.yCoord, this.zCoord));
	}
}