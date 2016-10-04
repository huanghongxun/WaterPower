package waterpower.common.block.tileentity;

import waterpower.Reference;
import waterpower.common.item.rotor.ItemRotor;
import waterpower.common.item.rotor.RotorInventorySlot;

public abstract class TileEntityRotor extends TileEntityGenerator {

    public final RotorInventorySlot slotRotor = new RotorInventorySlot(this);
    
    {
    	addInvSlot(slotRotor);
    }
    
    public TileEntityRotor() {
        super();
    }
    
    public TileEntityRotor(int production, float maxStorage) {
    	super(production, maxStorage);
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
