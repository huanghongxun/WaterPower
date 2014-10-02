package org.jackhuang.watercraft.common.block.machines;

import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.tileentity.TileEntityStandardWaterMachine;
import org.jackhuang.watercraft.util.mods.Mods;

import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import ic2.api.recipe.Recipes;

public class TileEntityCutter extends TileEntityStandardWaterMachine {

	public TileEntityCutter() {
		super(8000, 1*20);

		/*if(Mods.GregTech.isAvailable)
			this.inputSlot = new InventorySlotProcessableGreg(this, "input",
					1, Recipes.matterAmplifier, GT_Recipe_Map.sCutterRecipes);
		else*/
			this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
					1, Recipes.matterAmplifier);
	}

	public String getInventoryName() {
		return "Cutter";
	}

	public float getWrenchDropRate() {
		return 1f;
	}

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntityCutter");
	}
}
