package org.jackhuang.watercraft.common.block.machines;

import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.block.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.recipe.MultiRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipes;
import org.jackhuang.watercraft.util.Mods;

import ic2.api.recipe.Recipes;

public class TileEntityLathe extends TileEntityStandardWaterMachine {

    public TileEntityLathe() {
        super(80, 10*20);

        this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
                1, MyRecipes.lathe);
    }
    
    public static void init() {
        MyRecipes.lathe = new MultiRecipeManager();
    }

    public String getInventoryName() {
        return "Water-Powered Lathe";
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityLathe");
    }
}
