package org.jackhuang.watercraft.integration;

import factorization.oreprocessing.TileEntityGrinder;
import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.common.recipe.IRecipeInput;
import org.jackhuang.watercraft.common.recipe.IRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipeOutput;
import org.jackhuang.watercraft.util.StackUtil;

import java.util.Map;

public class FactorizationGrinder implements IRecipeManager {

    @Override
    public boolean addRecipe(ItemStack input, ItemStack... outputs) {
        TileEntityGrinder.addRecipe(input, outputs[0], 1);
        return false;
    }

    @Override
    public MyRecipeOutput getOutput(ItemStack input, boolean adjustInput) {
       for(TileEntityGrinder.GrinderRecipe recipe : TileEntityGrinder.recipes) {
    	   if(StackUtil.isStackEqual(recipe.getInput().get(0), input)) {
    		   if(adjustInput)
    			   input.stackSize -= recipe.getInput().get(0).stackSize;
    		   return new MyRecipeOutput(recipe.output);
    	   }
       }
       return null;
    }

    @Override
    public Map<IRecipeInput, MyRecipeOutput> getAllRecipes() {
        return null;
    }
}
