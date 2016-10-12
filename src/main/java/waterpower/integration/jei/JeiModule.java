package waterpower.integration.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import waterpower.common.recipe.MyRecipes;

public class JeiModule extends BlankModPlugin {

	@Override
	public void register(IModRegistry registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		addMachineRecipes(registry, new DefaultMyRecipeCategory("compressor", MyRecipes.compressor, guiHelper));
		addMachineRecipes(registry, new DefaultMyRecipeCategory("cutter", MyRecipes.cutter, guiHelper));
		addMachineRecipes(registry, new DefaultMyRecipeCategory("lathe", MyRecipes.lathe, guiHelper));
		addMachineRecipes(registry, new DefaultMyRecipeCategory("macerator", MyRecipes.macerator, guiHelper));
		addMachineRecipes(registry, new DefaultMyRecipeCategory("sawmill", MyRecipes.sawmill, guiHelper));
	}

	private void addMachineRecipes(IModRegistry registry, MyRecipeCategory category) {
		registry.addRecipeCategories(new IRecipeCategory[] { category });
		registry.addRecipeHandlers(new IRecipeHandler[] { new MyRecipeHandler(category) });
		registry.addRecipes(MyRecipeWrapper.createRecipes(category));
	}

}
