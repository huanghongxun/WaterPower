package org.jackhuang.watercraft.common.block.machines;

import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.recipe.HashMapRecipeManager;
import org.jackhuang.watercraft.common.recipe.MultiRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipes;
import org.jackhuang.watercraft.common.tileentity.TileEntityStandardWaterMachine;
import org.jackhuang.watercraft.integration.MekanismModule;
import org.jackhuang.watercraft.integration.ic2.IndustrialCraftRecipeManager;
import org.jackhuang.watercraft.integration.ic2.IndustrialCraftModule;
import org.jackhuang.watercraft.util.Mods;

import net.minecraft.item.ItemStack;

public class TileEntityMacerator extends TileEntityStandardWaterMachine {

	public TileEntityMacerator() {
		super(80, 10*20);

		this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
				1, MyRecipes.macerator);
	}
	
	public static void init() {
		MyRecipes.macerator = new MultiRecipeManager();
		if(Mods.IndustrialCraft2.isAvailable)
		    ((MultiRecipeManager)MyRecipes.macerator).addRecipeManager(new IndustrialCraftRecipeManager(IndustrialCraftModule.macerator));
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
		return DefaultGuiIds.get("tileEntityMacerator");
	}
}
