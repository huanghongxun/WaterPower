package waterpower.common.block.machines;

import net.minecraft.block.state.IBlockState;
import waterpower.client.gui.DefaultGuiIds;
import waterpower.common.block.inventory.InventorySlotProcessableGeneric;
import waterpower.common.recipe.MultiRecipeManager;
import waterpower.common.recipe.MyRecipes;
import waterpower.integration.ic2.IndustrialCraftModule;
import waterpower.integration.ic2.IndustrialCraftRecipeManager;
import waterpower.util.Mods;

public class TileEntityCompressor extends TileEntityStandardWaterMachine {

    public TileEntityCompressor() {
        super(2000, 2 * 20);

        this.inputSlot = new InventorySlotProcessableGeneric(this, "input", 1, MyRecipes.compressor);
    }
    
    @Override
    public IBlockState getBlockState() {
    	return super.getBlockState().withProperty(BlockMachines.MACHINE_TYPES, MachineType.COMPRESSOR);
    }

    public static void init() {
        MyRecipes.compressor = new MultiRecipeManager();
        if (Mods.IndustrialCraft2.isAvailable)
            ((MultiRecipeManager) MyRecipes.compressor).addRecipeManager(new IndustrialCraftRecipeManager(IndustrialCraftModule.getCompressorMachineManager()));
    }

	@Override
	public String getName() {
		return "Compressor";
	}

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityCompressor");
    }

}