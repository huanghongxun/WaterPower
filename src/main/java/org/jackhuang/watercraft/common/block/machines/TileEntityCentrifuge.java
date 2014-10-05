package org.jackhuang.watercraft.common.block.machines;

import org.jackhuang.watercraft.api.BasicMachineRecipeManager;
import org.jackhuang.watercraft.api.MyRecipeOutput;
import org.jackhuang.watercraft.api.MyRecipes;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.tileentity.TileEntityStandardWaterMachine;
import org.jackhuang.watercraft.util.StackUtil;
import org.jackhuang.watercraft.util.mods.Mods;

import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class TileEntityCentrifuge extends TileEntityStandardWaterMachine {

	public TileEntityCentrifuge() {
		super(80, 10 * 20, 4);

		this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
				2, MyRecipes.centrifuge_gt);
	}

	public static void init() {
		MyRecipes.centrifuge_gt = new BasicMachineRecipeManager();

	}

	@Override
	public String getInventoryName() {
		return "Centrifuge";
	}

	public float getWrenchDropRate() {
		return 1f;
	}

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntityCentrifuge");
	}
	
	@Override
	protected void beginProcess(MyRecipeOutput output) {
		/*if(output.gt_Recipe!=null) {
			defaultEnergyConsume = output.gt_Recipe.mEUt * 500;
			defaultOperationLength = output.gt_Recipe.mDuration / 50;
		} else {*/
			defaultEnergyConsume = 80;
			defaultOperationLength = 10 * 20;
		//}
	}
	
	@Override
	protected void failedProcess() {
		super.failedProcess();

		defaultEnergyConsume = 80;
		defaultOperationLength = 10 * 20;
	}
}
