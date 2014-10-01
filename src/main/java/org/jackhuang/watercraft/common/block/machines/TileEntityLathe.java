package org.jackhuang.watercraft.common.block.machines;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.api.BasicMachineRecipeManager;
import org.jackhuang.watercraft.api.MyRecipes;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.tileentity.TileEntityStandardWaterMachine;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;

public class TileEntityLathe extends TileEntityStandardWaterMachine {

	public TileEntityLathe() {
		super(80, 10*20);

		if(WaterPower.isGregTechLoaded)
			//this.inputSlot = new InventorySlotProcessableGreg(this, "input",
			//		1, MyRecipes.lathe_gt, GT_Recipe.sLatheRecipes);
			;
		else
			this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
					1, MyRecipes.lathe_gt);
	}
	
	public static void init() {
		MyRecipes.lathe_gt = new BasicMachineRecipeManager();
	}

	public String getInventoryName() {
		return "Lathe";
	}

	public float getWrenchDropRate() {
		return 1f;
	}

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntityLathe");
	}
}
