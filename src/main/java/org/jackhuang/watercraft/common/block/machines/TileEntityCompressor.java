package org.jackhuang.watercraft.common.block.machines;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.recipe.HashMapRecipeManager;
import org.jackhuang.watercraft.common.recipe.MultiRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipes;
import org.jackhuang.watercraft.common.tileentity.TileEntityStandardWaterMachine;
import org.jackhuang.watercraft.integration.MekanismModule;
import org.jackhuang.watercraft.integration.ic2.IndustrialCraftModule;
import org.jackhuang.watercraft.integration.ic2.IndustrialCraftRecipeManager;

public class TileEntityCompressor extends TileEntityStandardWaterMachine {

	public TileEntityCompressor() {
		super(2000, 2*20);

		this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
				1, MyRecipes.compressor);
	}
	
	public static void init() {
		MyRecipes.compressor = new MultiRecipeManager()
				.addRecipeManager(IndustrialCraftModule.IS_INDUSTRIAL_CRAFT_RECIPES_AVAILABLE, new IndustrialCraftRecipeManager(IndustrialCraftModule.compressor));
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
		return DefaultGuiIds.get("tileEntityCompressor");
	}

}