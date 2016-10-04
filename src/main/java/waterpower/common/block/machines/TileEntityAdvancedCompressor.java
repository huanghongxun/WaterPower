package waterpower.common.block.machines;

import net.minecraft.block.state.IBlockState;
import waterpower.client.gui.DefaultGuiIds;
import waterpower.common.block.inventory.InventorySlotProcessableGeneric;
import waterpower.common.recipe.MultiRecipeManager;
import waterpower.common.recipe.MyRecipes;

public class TileEntityAdvancedCompressor extends TileEntityStandardWaterMachine {

    public TileEntityAdvancedCompressor() {
        super(5000, 64 * 20);

        /*
         * if(Mods.GregTech.isAvailable) { this.inputSlot = new
         * InventorySlotProcessableGreg(this, "input", 2,
         * MyRecipes.implosion_gt, GT_Recipe_Map.sImplosionRecipes); } else
         */
        this.inputSlot = new InventorySlotProcessableGeneric(this, "input", 1, MyRecipes.implosion);
    }
    
    @Override
    public IBlockState getBlockState() {
    	return super.getBlockState().withProperty(BlockMachines.MACHINE_TYPES, MachineType.ADVCOMPRESSOR);
    }

    public static void init() {
        MyRecipes.implosion = new MultiRecipeManager();
    }

    @Override
    public String getName() {
        return "AdvancedCompressor";
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityAdvancedCompressor");
    }

    @Override
    public void markDirty() {
        if (!inputSlot.isEmpty()) {
            InventorySlotProcessableGeneric generic = (InventorySlotProcessableGeneric) inputSlot;
            /*
             * if(generic instanceof InventorySlotProcessableGreg) { GT_Recipe
             * recipe =
             * ((InventorySlotProcessableGreg)generic).getGTRecipeOutput();
             * if(recipe == null) operationLength = 1; else
             * defaultOperationLength = recipe.mInputs[1].stackSize * 20; }
             */
        }

        super.markDirty();
    }

    @Override
    public void update() {
        super.update();

        /*
         * if(Mods.GregTech.isAvailable) { InventorySlotProcessableGreg greg =
         * ((InventorySlotProcessableGreg)inputSlot); if(greg.get(1) == null ||
         * greg.get(1).stackSize < 64) greg.put(1,
         * StackUtil.copyWithSize(IC2Items.getItem("industrialTnt"), 64)); }
         */
    }

}