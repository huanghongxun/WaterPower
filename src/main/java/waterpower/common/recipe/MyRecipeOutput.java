package waterpower.common.recipe;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

public class MyRecipeOutput {
    public MyRecipeOutput(int power, List<ItemStack> items1) {
        this.items = items1;

        this.power = power;
    }

    public MyRecipeOutput(List<ItemStack> items1) {
        this(-1, items1);
    }

    public MyRecipeOutput(int power, ItemStack... items1) {
        this(power, Arrays.asList(items1));
    }

    public MyRecipeOutput(ItemStack... items1) {
        this(-1, Arrays.asList(items1));
    }

    public final List<ItemStack> items;
    public final int power;
}
