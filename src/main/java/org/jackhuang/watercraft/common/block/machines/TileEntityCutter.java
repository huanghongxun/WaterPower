package org.jackhuang.watercraft.common.block.machines;

import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.recipe.MultiRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipes;
import org.jackhuang.watercraft.common.tileentity.TileEntityStandardWaterMachine;
import org.jackhuang.watercraft.integration.IndustrialCraftRecipeManager;
import org.jackhuang.watercraft.integration.IndustrialCraftRecipes;
import org.jackhuang.watercraft.util.mods.Mods;

public class TileEntityCutter extends TileEntityStandardWaterMachine {

	public TileEntityCutter() {
		super(8000, 1*20);

		/*if(Mods.GregTech.isAvailable)
			this.inputSlot = new InventorySlotProcessableGreg(this, "input",
					1, Recipes.matterAmplifier, GT_Recipe_Map.sCutterRecipes);
		else*/
			this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
					1, MyRecipes.cutter);
	}
	
	public static void init() {
		MyRecipes.cutter = new MultiRecipeManager().addRecipeManager(IndustrialCraftRecipes.IS_INDUSTRIAL_CRAFT_RECIPES_AVAILABLE, new IndustrialCraftRecipeManager(IndustrialCraftRecipes.cutter));
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
