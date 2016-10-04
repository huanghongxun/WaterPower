package waterpower.common.block.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import waterpower.common.block.inventory.InventorySlot;

public abstract class TileEntityInventory extends TileEntityBase implements ISidedInventory {
    private final List<InventorySlot> invSlots = new ArrayList<InventorySlot>();

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        NBTTagCompound invSlotsTag = nbtTagCompound.getCompoundTag("InvSlots");

        for (InventorySlot invSlot : this.invSlots)
            invSlot.readFromNBT(invSlotsTag.getCompoundTag(invSlot.name));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        NBTTagCompound invSlotsTag = new NBTTagCompound();

        for (InventorySlot invSlot : this.invSlots) {
            NBTTagCompound invSlotTag = new NBTTagCompound();

            invSlot.writeToNBT(invSlotTag);

            invSlotsTag.setTag(invSlot.name, invSlotTag);
        }

        nbtTagCompound.setTag("InvSlots", invSlotsTag);
        
        return nbtTagCompound;
    }

    public List<InventorySlot> getInventorySlots() {
        return invSlots;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}

    @Override
    public int getSizeInventory() {
        int ret = 0;
        for (InventorySlot invSlot : this.invSlots)
            ret += invSlot.size();
        return ret;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        for (InventorySlot invSlot : this.invSlots) {
            if (index < invSlot.size())
                return invSlot.get(index);
            index -= invSlot.size();
        }

        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        ItemStack itemStack = getStackInSlot(index);
        if (itemStack == null)
            return null;

        if (amount >= itemStack.stackSize) {
            setInventorySlotContents(index, null);

            return itemStack;
        }

        itemStack.stackSize -= amount;

        ItemStack ret = itemStack.copy();
        ret.stackSize = amount;

        return ret;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack ret = getStackInSlot(index);

        if (ret != null)
            setInventorySlotContents(index, null);

        return ret;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack itemStack) {
        for (InventorySlot invSlot : this.invSlots) {
            if (index < invSlot.size()) {
                invSlot.put(index, itemStack);
                break;
            }
            index -= invSlot.size();
        }
    }

    public boolean isInvNameLocalized() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return entityPlayer.getDistanceSq(pos.add(0.5, 0.5, 0.5)) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        InventorySlot invSlot = getInvSlot(index);

        return (invSlot != null) && (invSlot.canInput()) && (invSlot.accepts(itemStack));
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        int[] ret = new int[getSizeInventory()];

        for (int i = 0; i < ret.length; i++)
            ret[i] = i;

        return ret;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStack, EnumFacing side) {
        InventorySlot targetSlot = getInvSlot(index);
        if (targetSlot == null)
            return false;

        if ((!targetSlot.canInput()) || (!targetSlot.accepts(itemStack)))
            return false;
        if ((targetSlot.preferredSide != InventorySlot.InvSide.ANY) && (targetSlot.preferredSide.matches(side)))
            return true;

        for (InventorySlot invSlot : this.invSlots) {
            if ((invSlot != targetSlot) && (invSlot.preferredSide != InventorySlot.InvSide.ANY) && (invSlot.preferredSide.matches(side))
                    && (invSlot.canInput()) && (invSlot.accepts(itemStack))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStack, EnumFacing side) {
        InventorySlot targetSlot = getInvSlot(index);
        if (targetSlot == null)
            return false;

        if (!targetSlot.canOutput())
            return false;
        if ((targetSlot.preferredSide != InventorySlot.InvSide.ANY) && (targetSlot.preferredSide.matches(side)))
            return true;

        for (InventorySlot invSlot : this.invSlots) {
            if ((invSlot != targetSlot) && (invSlot.preferredSide != InventorySlot.InvSide.ANY) && (invSlot.preferredSide.matches(side))
                    && (invSlot.canOutput())) {
                return false;
            }
        }

        return true;
    }

    public void addInvSlot(InventorySlot invSlot) {
        this.invSlots.add(invSlot);
    }

    private InventorySlot getInvSlot(int index) {
        for (InventorySlot invSlot : this.invSlots) {
            if (index < invSlot.size()) {
                return invSlot;
            }
            index -= invSlot.size();
        }

        return null;
    }

    @Override
    public void clear() {
        for (InventorySlot invSlot : this.invSlots)
            invSlot.clear();
    }

}