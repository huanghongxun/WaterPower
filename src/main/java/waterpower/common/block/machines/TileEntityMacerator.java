package waterpower.common.block.machines;

import net.minecraft.block.state.IBlockState;
import waterpower.client.gui.DefaultGuiIds;
import waterpower.common.block.inventory.InventorySlotProcessableGeneric;
import waterpower.common.recipe.MultiRecipeManager;
import waterpower.common.recipe.MyRecipes;
import waterpower.integration.ic2.IndustrialCraftModule;
import waterpower.integration.ic2.IndustrialCraftRecipeManager;
import waterpower.util.Mods;

public class TileEntityMacerator extends TileEntityStandardWaterMachine {

    public TileEntityMacerator() {
        super(80, 10 * 20);

        this.inputSlot = new InventorySlotProcessableGeneric(this, "input", 1, MyRecipes.macerator);
    }
    
    @Override
    public IBlockState getBlockState() {
    	return super.getBlockState().withProperty(BlockMachines.MACHINE_TYPES, MachineType.MACERATOR);
    }

    public static void init() {
        MyRecipes.macerator = new MultiRecipeManager();
        if (Mods.IndustrialCraft2.isAvailable)
            ((MultiRecipeManager) MyRecipes.macerator).addRecipeManager(new IndustrialCraftRecipeManager(IndustrialCraftModule.getMaceratorMachineManager()));
    }

    @Override
    public String getName() {
        return "Macerator";
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityMacerator");
    }
}
