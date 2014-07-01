package org.jackhuang.compactwatermills.common.block.machines;

import ic2.api.recipe.Recipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.jackhuang.compactwatermills.client.gui.DefaultGuiIds;
import org.jackhuang.compactwatermills.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityStandardWaterMachine;

public class TileEntityCompressor extends TileEntityStandardWaterMachine {

	public TileEntityCompressor() {
		super(2000, 2*20);

		this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
				1, Recipes.compressor);
	}

	@Override
	public String getInventoryName() {
		return "Compressor";
	}

	public float getWrenchDropRate() {
		return 1;
	}

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntityCompressor").id;
	}

}