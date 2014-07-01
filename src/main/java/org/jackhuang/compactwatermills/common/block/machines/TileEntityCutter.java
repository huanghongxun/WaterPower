package org.jackhuang.compactwatermills.common.block.machines;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.api.BasicMachineRecipeManager;
import org.jackhuang.compactwatermills.api.MyRecipes;
import org.jackhuang.compactwatermills.client.gui.DefaultGuiIds;
import org.jackhuang.compactwatermills.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.compactwatermills.common.inventory.InventorySlotProcessableGreg;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityStandardWaterMachine;

import gregtech.api.util.GT_Recipe;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;

public class TileEntityCutter extends TileEntityStandardWaterMachine {

	public TileEntityCutter() {
		super(8000, 1*20);

		if(CompactWatermills.isGregTechLoaded)
			this.inputSlot = new InventorySlotProcessableGreg(this, "input",
					1, MyRecipes.cutter_gt, GT_Recipe.sCutterRecipes);
		else
			this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
					1, MyRecipes.cutter_gt);
	}
	
	public static void init() {
		MyRecipes.cutter_gt = new BasicMachineRecipeManager();
	}

	public String getInventoryName() {
		return "Cutter";
	}

	public float getWrenchDropRate() {
		return 1f;
	}

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntityCutter").id;
	}
}
