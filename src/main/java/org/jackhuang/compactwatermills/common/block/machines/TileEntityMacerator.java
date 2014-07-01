package org.jackhuang.compactwatermills.common.block.machines;

import org.jackhuang.compactwatermills.client.gui.DefaultGuiIds;
import org.jackhuang.compactwatermills.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityStandardWaterMachine;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;

public class TileEntityMacerator extends TileEntityStandardWaterMachine {

	public TileEntityMacerator() {
		super(80, 10*20);

		this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
				1, Recipes.macerator);
	}

	@Override
	public String getInventoryName() {
		return "Macerator";
	}

	public float getWrenchDropRate() {
		return 1f;
	}

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntityMacerator").id;
	}
}
