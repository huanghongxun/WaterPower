package org.jackhuang.watercraft.common.block.inventory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.fluids.Fluid;

import org.jackhuang.watercraft.common.block.tileentity.TileEntityInventory;

public class InventorySlotConsumableLiquidByList extends InventorySlotConsumableLiquid {
    private final Set<Fluid> acceptedFluids;

    public InventorySlotConsumableLiquidByList(TileEntityInventory base, String name, int count, Fluid[] fluidlist) {
        super(base, name, count);

        this.acceptedFluids = new HashSet<Fluid>(Arrays.asList(fluidlist));
    }

    public InventorySlotConsumableLiquidByList(TileEntityInventory base, String name, InventorySlot.Access access, int count,
            InventorySlot.InvSide preferredSide, InventorySlotConsumableLiquid.OpType opType, Fluid[] fluidlist) {
        super(base, name, access, count, preferredSide, opType);

        this.acceptedFluids = new HashSet<Fluid>(Arrays.asList(fluidlist));
    }

    protected boolean acceptsLiquid(Fluid fluid) {
        return this.acceptedFluids.contains(fluid);
    }

    protected Iterable<Fluid> getPossibleFluids() {
        return this.acceptedFluids;
    }
}