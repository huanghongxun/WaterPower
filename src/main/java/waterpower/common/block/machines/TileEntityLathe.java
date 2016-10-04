package waterpower.common.block.machines;

import net.minecraft.block.state.IBlockState;
import waterpower.client.gui.DefaultGuiIds;
import waterpower.common.block.inventory.InventorySlotProcessableGeneric;
import waterpower.common.recipe.MultiRecipeManager;
import waterpower.common.recipe.MyRecipes;

public class TileEntityLathe extends TileEntityStandardWaterMachine {

    public TileEntityLathe() {
        super(80, 10 * 20);

        this.inputSlot = new InventorySlotProcessableGeneric(this, "input", 1, MyRecipes.lathe);
    }
    
    @Override
    public IBlockState getBlockState() {
    	return super.getBlockState().withProperty(BlockMachines.MACHINE_TYPES, MachineType.LATHE);
    }

    public static void init() {
        MyRecipes.lathe = new MultiRecipeManager();
    }

    @Override
    public String getName() {
        return "Water-Powered Lathe";
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityLathe");
    }
}
