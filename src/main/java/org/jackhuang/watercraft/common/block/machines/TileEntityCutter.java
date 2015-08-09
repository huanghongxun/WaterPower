package org.jackhuang.watercraft.common.block.machines;

import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.block.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.recipe.MultiRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipes;
import org.jackhuang.watercraft.integration.ic2.IndustrialCraftRecipeManager;
import org.jackhuang.watercraft.integration.ic2.IndustrialCraftModule;
import org.jackhuang.watercraft.util.Mods;

public class TileEntityCutter extends TileEntityStandardWaterMachine {

    public TileEntityCutter() {
        super(8000, 1 * 20);

        /*
         * if(Mods.GregTech.isAvailable) this.inputSlot = new
         * InventorySlotProcessableGreg(this, "input", 1,
         * Recipes.matterAmplifier, GT_Recipe_Map.sCutterRecipes); else
         */
        this.inputSlot = new InventorySlotProcessableGeneric(this, "input", 1,
                MyRecipes.cutter);
    }

    public static void init() {
        MyRecipes.cutter = new MultiRecipeManager();
        if (Mods.IndustrialCraft2.isAvailable)
            ((MultiRecipeManager) MyRecipes.cutter)
                    .addRecipeManager(new IndustrialCraftRecipeManager(
                            IndustrialCraftModule.cutter));
    }

    public String getInventoryName() {
        return "Cutter";
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityCutter");
    }
}
