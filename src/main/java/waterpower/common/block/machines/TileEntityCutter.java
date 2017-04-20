package waterpower.common.block.machines;

import net.minecraft.block.state.IBlockState;
import waterpower.client.gui.DefaultGuiIds;
import waterpower.common.block.inventory.InventorySlotProcessableGeneric;
import waterpower.common.recipe.MultiRecipeManager;
import waterpower.common.recipe.MyRecipes;
import waterpower.integration.ic2.IndustrialCraftModule;
import waterpower.integration.ic2.IndustrialCraftRecipeManager;
import waterpower.util.Mods;

public class TileEntityCutter extends TileEntityStandardWaterMachine {

    public TileEntityCutter() {
        super(8000, 1 * 20);

        /*
         * if(Mods.GregTech.isAvailable) this.inputSlot = new
         * InventorySlotProcessableGreg(this, "input", 1,
         * Recipes.matterAmplifier, GT_Recipe_Map.sCutterRecipes); else
         */
        this.inputSlot = new InventorySlotProcessableGeneric(this, "input", 1, MyRecipes.cutter);
    }
    
    @Override
    public IBlockState getBlockState() {
    	return super.getBlockState().withProperty(BlockMachines.MACHINE_TYPES, MachineType.CUTTER);
    }

    public static void init() {
        MyRecipes.cutter = new MultiRecipeManager();
        if (Mods.IndustrialCraft2.isAvailable)
            ((MultiRecipeManager) MyRecipes.cutter).addRecipeManager(new IndustrialCraftRecipeManager(IndustrialCraftModule.getCutterMachineManager()));
    }

    @Override
	public String getName() {
        return "Cutter";
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityCutter");
    }
}
