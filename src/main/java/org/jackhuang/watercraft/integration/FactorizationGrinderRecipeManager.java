package org.jackhuang.watercraft.integration;

import factorization.oreprocessing.TileEntityGrinder;
import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.common.recipe.IMyRecipeInput;
import org.jackhuang.watercraft.common.recipe.IRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipeInputItemStack;
import org.jackhuang.watercraft.common.recipe.MyRecipeInputOreDictionary;
import org.jackhuang.watercraft.common.recipe.MyRecipeOutput;
import org.jackhuang.watercraft.util.StackUtil;

import java.util.HashMap;
import java.util.Map;

public class FactorizationGrinderRecipeManager implements IRecipeManager {

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
    public Map<IMyRecipeInput, MyRecipeOutput> getAllRecipes() {
    	HashMap<IMyRecipeInput, MyRecipeOutput> map = new HashMap<IMyRecipeInput, MyRecipeOutput>();
        for(TileEntityGrinder.GrinderRecipe recipe : TileEntityGrinder.recipes) {
        	Object o = recipe.getOreDictionaryInput();
        	IMyRecipeInput input;
        	if(o instanceof String)
        		input = new MyRecipeInputOreDictionary((String)o);
        	else if(o instanceof ItemStack)
        		input = new MyRecipeInputItemStack((ItemStack)o);
        	else input = null;
        	if(input != null)
        		map.put(input, new MyRecipeOutput(recipe.output));
        }
        return map;
    }
}
