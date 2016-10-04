package waterpower.common.block.inventory;

import net.minecraft.item.ItemStack;
import waterpower.common.block.tileentity.TileEntityInventory;
import waterpower.common.recipe.MyRecipeOutput;

public abstract class InventorySlotProcessable extends InventorySlotConsumable {
    public InventorySlotProcessable(TileEntityInventory base, String name, int count) {
        super(base, name, count);
    }

    @Override
	public abstract boolean accepts(ItemStack paramItemStack);

    public abstract MyRecipeOutput process();

    public abstract void consume();
}
