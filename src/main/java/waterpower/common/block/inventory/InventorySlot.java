package waterpower.common.block.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import waterpower.common.block.tileentity.TileEntityInventory;
import waterpower.util.StackUtil;

public class InventorySlot {
    private final TileEntityInventory base;
    public final String name;
    protected final ItemStack[] contents;
    protected final Access access;
    public final InvSide preferredSide;

    public InventorySlot(TileEntityInventory base, String name, Access access, int count) {
        this(base, name, access, count, InvSide.ANY);
    }

    public InventorySlot(TileEntityInventory base, String name, Access access, int count, InvSide preferredSide) {
        this.contents = new ItemStack[count];

        this.base = base;
        this.name = name;
        this.access = access;
        this.preferredSide = preferredSide;

        base.addInvSlot(this);
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        NBTTagList contentsTag = nbtTagCompound.getTagList("Contents", 10);

        for (int i = 0; i < contentsTag.tagCount(); i++) {
            NBTTagCompound contentTag = contentsTag.getCompoundTagAt(i);

            int index = contentTag.getByte("Index") & 0xFF;
            ItemStack itemStack = ItemStack.loadItemStackFromNBT(contentTag);

            if (itemStack == null) {
                continue;
            }

            put(index, itemStack);
        }
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        NBTTagList contentsTag = new NBTTagList();

        for (int i = 0; i < this.contents.length; i++) {
            if (this.contents[i] == null) {
                continue;
            }
            NBTTagCompound contentTag = new NBTTagCompound();

            contentTag.setByte("Index", (byte) i);
            this.contents[i].writeToNBT(contentTag);

            contentsTag.appendTag(contentTag);
        }

        nbtTagCompound.setTag("Contents", contentsTag);
    }

    public int size() {
        return this.contents.length;
    }

    public ItemStack get() {
        return get(0);
    }

    public ItemStack get(int index) {
        return this.contents[index];
    }

    public void put(ItemStack content) {
        put(0, content);
    }

    public void put(int index, ItemStack content) {
        this.contents[index] = content;
    }

    public void clear() {
        for (int i = 0; i < this.contents.length; i++)
            this.contents[i] = null;
    }

    public boolean accepts(ItemStack itemStack) {
        return true;
    }

    public boolean canInput() {
        return (this.access == Access.I) || (this.access == Access.IO);
    }

    public boolean canOutput() {
        return (this.access == Access.O) || (this.access == Access.IO);
    }

    public boolean isEmpty() {
        for (ItemStack itemStack : this.contents) {
            if (itemStack != null)
                return false;
        }

        return true;
    }

    public void organize() {
        for (int dstIndex = 0; dstIndex < this.contents.length - 1; dstIndex++) {
            ItemStack dst = this.contents[dstIndex];

            if ((dst != null) && (dst.stackSize >= dst.getMaxStackSize()))
                continue;
            for (int srcIndex = dstIndex + 1; srcIndex < this.contents.length; srcIndex++) {
                ItemStack src = this.contents[srcIndex];
                if (src == null)
                    continue;
                if (dst == null) {
                    this.contents[srcIndex] = null;
                    ItemStack tmp85_83 = src;
                    dst = tmp85_83;
                    this.contents[dstIndex] = tmp85_83;
                } else if (StackUtil.isStackEqual(dst, src)) {
                    int space = dst.getMaxStackSize() - dst.stackSize;

                    if (src.stackSize <= space) {
                        this.contents[srcIndex] = null;
                        dst.stackSize += src.stackSize;
                    } else {
                        src.stackSize -= space;
                        dst.stackSize += space;
                        break;
                    }
                }
            }
        }
    }

    @Override
	public String toString() {
        String ret = this.name + "[" + this.contents.length + "]: ";

        for (int i = 0; i < this.contents.length; i++) {
            ret = ret + this.contents[i];

            if (i >= this.contents.length - 1)
                continue;
            ret = ret + ", ";
        }

        return ret;
    }

    public TileEntityInventory getTileEntity() {
        return base;
    }

    public ItemStack[] getCopiedContent() {
        return StackUtil.getCopiedStacks(contents);
    }

    public boolean isEquals(ItemStack[] is) {
        return StackUtil.isStacksEqual(is, contents);
    }

    public static enum InvSide {
        ANY, TOP, BOTTOM, SIDE;

        public boolean matches(EnumFacing side) {
            return (this == ANY) || ((side == EnumFacing.DOWN) && (this == BOTTOM)) || ((side == EnumFacing.UP) && (this == TOP)) || ((side.getIndex() >= 2) && (side.getIndex() <= 5) && (this == SIDE));
        }
    }

    public static enum Access {
        NONE, I, O, IO;
    }
}