package org.jackhuang.watercraft.common.block.machines;

import org.jackhuang.watercraft.api.BasicMachineRecipeManager;
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
				1, MyRecipes.centrifuge_gt);
	}

	public static void init() {
		MyRecipes.centrifuge_gt = new BasicMachineRecipeManager();

	}

	@Override
	public String getInventoryName() {
		return "Sawmill";
	}

	public float getWrenchDropRate() {
		return 1f;
	}

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntitySawmill");
	}
}
