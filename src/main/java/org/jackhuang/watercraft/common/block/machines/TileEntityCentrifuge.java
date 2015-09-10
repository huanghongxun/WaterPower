package org.jackhuang.watercraft.common.block.machines;

import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.block.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.recipe.MultiRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipeOutput;
import org.jackhuang.watercraft.common.recipe.MyRecipes;

public class TileEntityCentrifuge extends TileEntityStandardWaterMachine {

    public TileEntityCentrifuge() {
        super(80, 10 * 20, 4);

        this.inputSlot = new InventorySlotProcessableGeneric(this, "input", 2, MyRecipes.centrifuge);
    }

    public static void init() {
        MyRecipes.centrifuge = new MultiRecipeManager();
    }

    @Override
    public String getInventoryName() {
        return "Centrifuge";
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityCentrifuge");
    }

    @Override
    protected void beginProcess(MyRecipeOutput output) {
        /*
         * if(output.gt_Recipe!=null) { defaultEnergyConsume =
         * output.gt_Recipe.mEUt * 500; defaultOperationLength =
         * output.gt_Recipe.mDuration / 50; } else {
         */
        defaultEnergyConsume = 80;
        defaultOperationLength = 10 * 20;
        // }
    }

    @Override
    protected void failedProcess() {
        super.failedProcess();

        defaultEnergyConsume = 80;
        defaultOperationLength = 10 * 20;
    }
}
