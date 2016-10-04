package waterpower.common.block.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import waterpower.common.block.tileentity.TileEntityInventory;
import waterpower.common.recipe.IRecipeManager;
import waterpower.common.recipe.MyRecipeOutput;

public class InventorySlotProcessableGeneric extends InventorySlotProcessable {
    public IRecipeManager recipeManager;

    public InventorySlotProcessableGeneric(TileEntityInventory base, String name, int count, IRecipeManager recipeManager) {
        super(base, name, count);

        this.recipeManager = recipeManager;
    }

    @Override
	public boolean accepts(ItemStack itemStack) {
        ItemStack tmp = itemStack.copy();
        tmp.stackSize = 2147483647;

        return getOutput(tmp, false, true) != null;
    }

    @Override
	public MyRecipeOutput process() {
        ItemStack input = get();
        if ((input == null) && (!allowEmptyInput()))
            return null;

        MyRecipeOutput output = getOutput(input, false, false);
        if (output == null)
            return null;

        List itemsCopy = new ArrayList(output.items.size());

        for (ItemStack itemStack : output.items) {
            itemsCopy.add(itemStack.copy());
        }

        return new MyRecipeOutput(itemsCopy);
    }

    @Override
	public void consume() {
        ItemStack input = get();
        if ((input == null) && (!allowEmptyInput()))
            throw new IllegalStateException("consume from empty slot");

        MyRecipeOutput output = getOutput(input, true, false);
        if (output == null)
            throw new IllegalStateException("consume without a processing result");

        if ((input != null) && (input.stackSize <= 0))
            put(null);
    }

    public void setRecipeManager(IRecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }

    protected MyRecipeOutput getOutput(ItemStack input, boolean adjustInput, boolean forAccept) {
        return this.recipeManager.getOutput(input, adjustInput);
    }

    public MyRecipeOutput getRecipeOutput() {
        ItemStack input = get();
        if (input == null)
            return null;
        return getOutput(input, false, false);
    }

    protected boolean allowEmptyInput() {
        return false;
    }
}
