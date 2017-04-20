package waterpower.integration.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import waterpower.common.block.machines.GuiCompressor;
import waterpower.common.block.machines.GuiCutter;
import waterpower.common.block.machines.GuiLathe;
import waterpower.common.block.machines.GuiMacerator;
import waterpower.common.block.machines.GuiSawmill;
import waterpower.common.recipe.MyRecipes;

@JEIPlugin
public class JeiModule extends BlankModPlugin {

	@Override
	public void register(IModRegistry registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		addMachineRecipes(registry, new DefaultMyRecipeCategory("waterpower.compressor", MyRecipes.compressor, guiHelper, GuiCompressor.GUI), GuiCompressor.class, GuiCompressor.TITLE_I18N);
		addMachineRecipes(registry, new DefaultMyRecipeCategory("waterpower.cutter", MyRecipes.cutter, guiHelper, GuiCutter.GUI), GuiCutter.class, GuiCutter.TITLE_I18N);
		addMachineRecipes(registry, new DefaultMyRecipeCategory("waterpower.lathe", MyRecipes.lathe, guiHelper, GuiLathe.GUI), GuiLathe.class, GuiLathe.TITLE_I18N);
		addMachineRecipes(registry, new DefaultMyRecipeCategory("waterpower.macerator", MyRecipes.macerator, guiHelper, GuiMacerator.GUI), GuiMacerator.class, GuiMacerator.TITLE_I18N);
		addMachineRecipes(registry, new DefaultMyRecipeCategory("waterpower.sawmill", MyRecipes.sawmill, guiHelper, GuiSawmill.GUI), GuiSawmill.class, GuiSawmill.TITLE_I18N);
	}

	private void addMachineRecipes(IModRegistry registry, MyRecipeCategory category, Class guiClass, String title) {
		registry.addRecipeCategories(new IRecipeCategory[] { category });
		registry.addRecipeHandlers(new IRecipeHandler[] { new MyRecipeHandler(category) });
		registry.addRecipes(MyRecipeWrapper.createRecipes(category));
		registry.addRecipeClickArea(guiClass, 50, 34, 24, 14, title);
	}

}
