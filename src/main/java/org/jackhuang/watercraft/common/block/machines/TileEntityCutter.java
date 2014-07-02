package org.jackhuang.watercraft.common.block.machines;

import org.jackhuang.watercraft.WaterCraft;
import org.jackhuang.watercraft.api.BasicMachineRecipeManager;
import org.jackhuang.watercraft.api.MyRecipes;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.inventory.InventorySlotProcessableGreg;
import org.jackhuang.watercraft.common.tileentity.TileEntityStandardWaterMachine;

import gregtech.api.util.GT_Recipe;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;

public class TileEntityCutter extends TileEntityStandardWaterMachine {

	public TileEntityCutter() {
		super(8000, 1*20);

		if(WaterCraft.isGregTechLoaded)
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
