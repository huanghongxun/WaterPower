package org.jackhuang.watercraft.common.block.tileentity;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.item.rotors.ItemRotor;
import org.jackhuang.watercraft.common.item.rotors.RotorInventorySlot;

public abstract class TileEntityRotor extends TileEntityElectricMetaBlock {

    public final RotorInventorySlot slotRotor;

    public TileEntityRotor(int i, int j) {
        super(i, j);
        addInvSlot(slotRotor = new RotorInventorySlot(this));
    }

    public boolean hasRotor() {
        return slotRotor != null && !slotRotor.isEmpty() && slotRotor.get(0).getItem() instanceof ItemRotor;
    }

    public ItemRotor getRotor() {
        return (ItemRotor) slotRotor.get(0).getItem();
    }

    protected void damageRotor(int tick) {
        if (!hasRotor())
            return;
        ItemRotor rotor = getRotor();
        rotor.tickRotor(slotRotor.get(0), this, worldObj);
        if (!rotor.type.isInfinite()) {
            if (slotRotor.get(0).getItemDamage() + tick > slotRotor.get(0).getMaxDamage()) {
                slotRotor.put(0, null);
            } else {
                int damage = slotRotor.get(0).getItemDamage() + tick;
                slotRotor.get(0).setItemDamage(damage);
            }
            markDirty();
        }
    }

    protected double tickRotor() {
        if (!Reference.General.watermillNeedsRotor)
            return 1;
        return hasRotor() ? getRotor().type.getEfficiency() : 0;
    }

}
