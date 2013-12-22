package org.jackhuang.compactwatermills.inventory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jackhuang.compactwatermills.tileentity.TileEntityInventory;

import net.minecraftforge.fluids.Fluid;

public class InventorySlotConsumableLiquidByList extends
		InventorySlotConsumableLiquid {
	private final Set<Fluid> acceptedFluids;

	public InventorySlotConsumableLiquidByList(TileEntityInventory base,
			String name, int oldStartIndex, int count, Fluid[] fluidlist) {
		super(base, name, oldStartIndex, count);

		this.acceptedFluids = new HashSet<Fluid>(Arrays.asList(fluidlist));
	}

	public InventorySlotConsumableLiquidByList(TileEntityInventory base,
			String name, int oldStartIndex, InventorySlot.Access access,
			int count, InventorySlot.InvSide preferredSide,
			InventorySlotConsumableLiquid.OpType opType, Fluid[] fluidlist) {
		super(base, name, oldStartIndex, access, count, preferredSide, opType);

		this.acceptedFluids = new HashSet<Fluid>(Arrays.asList(fluidlist));
	}

	protected boolean acceptsLiquid(Fluid fluid) {
		return this.acceptedFluids.contains(fluid);
	}

	protected Iterable<Fluid> getPossibleFluids() {
		return this.acceptedFluids;
	}
}