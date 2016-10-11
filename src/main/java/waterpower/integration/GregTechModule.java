package waterpower.integration;

import gregtech.api.GregTech_API;
import net.minecraft.item.ItemStack;

public class GregTechModule {
    public static boolean blastFurnace(ItemStack input, ItemStack output, int cookTime) {
        try {
        	GregTech_API.sRecipeAdder.addBlastRecipe(input, null, output, null, cookTime, 128, 2000);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }
}
