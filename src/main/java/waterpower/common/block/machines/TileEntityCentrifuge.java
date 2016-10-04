package waterpower.common.block.machines;

import net.minecraft.block.state.IBlockState;
import waterpower.client.gui.DefaultGuiIds;
import waterpower.common.block.inventory.InventorySlotProcessableGeneric;
import waterpower.common.recipe.MultiRecipeManager;
import waterpower.common.recipe.MyRecipeOutput;
import waterpower.common.recipe.MyRecipes;

public class TileEntityCentrifuge extends TileEntityStandardWaterMachine {

    public TileEntityCentrifuge() {
        super(80, 10 * 20, 4);

        this.inputSlot = new InventorySlotProcessableGeneric(this, "input", 2, MyRecipes.centrifuge);
    }
    
    @Override
    public IBlockState getBlockState() {
    	return super.getBlockState().withProperty(BlockMachines.MACHINE_TYPES, MachineType.CENTRIFUGE);
    }

    public static void init() {
        MyRecipes.centrifuge = new MultiRecipeManager();
    }

    @Override
    public String getName() {
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
