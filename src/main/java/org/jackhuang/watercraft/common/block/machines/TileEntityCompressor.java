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
import org.jackhuang.watercraft.integration.IndustrialCraftRecipeManager;
import org.jackhuang.watercraft.integration.IndustrialCraftRecipes;
import org.jackhuang.watercraft.integration.MekanismRecipes;

public class TileEntityCompressor extends TileEntityStandardWaterMachine {

    public TileEntityCompressor() throws ClassNotFoundException {
	super(2000, 2 * 20);

	this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
		1, MyRecipes.compressor);
    }

    public static void init() {
	MyRecipes.compressor = new MultiRecipeManager().addRecipeManager(MekanismRecipes.IS_MEKANISM_RECIPES_AVAILABLE, new HashMapRecipeManager(MekanismRecipes.compressor))
		.addRecipeManager(IndustrialCraftRecipes.IS_INDUSTRIAL_CRAFT_RECIPES_AVAILABLE, new IndustrialCraftRecipeManager(IndustrialCraftRecipes.compressor));
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
