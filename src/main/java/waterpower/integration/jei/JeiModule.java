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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import waterpower.client.Local;
import waterpower.common.block.GlobalBlocks;
import waterpower.common.block.machines.GuiCompressor;
import waterpower.common.block.machines.GuiCutter;
import waterpower.common.block.machines.GuiLathe;
import waterpower.common.block.machines.GuiMacerator;
import waterpower.common.block.machines.GuiSawmill;
import waterpower.common.recipe.IRecipeManager;
import waterpower.common.recipe.MyRecipes;

@JEIPlugin
public class JeiModule extends BlankModPlugin {

	@Override
	public void register(IModRegistry registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeHandlers(new MyRecipeHandler());
		addMachineRecipes(registry, "waterpower.compressor", GuiCompressor.TITLE_I18N, MyRecipes.compressor, guiHelper, GuiCompressor.GUI, GlobalBlocks.compressor, GuiCompressor.class);
		addMachineRecipes(registry, "waterpower.cutter",GuiCutter.TITLE_I18N,  MyRecipes.cutter, guiHelper, GuiCutter.GUI, GlobalBlocks.cutter, GuiCutter.class);
		addMachineRecipes(registry, "waterpower.lathe", GuiLathe.TITLE_I18N, MyRecipes.lathe, guiHelper, GuiLathe.GUI, GlobalBlocks.lathe, GuiLathe.class);
		addMachineRecipes(registry, "waterpower.macerator", GuiMacerator.TITLE_I18N, MyRecipes.macerator, guiHelper, GuiMacerator.GUI, GlobalBlocks.macerator, GuiMacerator.class);
		addMachineRecipes(registry, "waterpower.sawmill", GuiSawmill.TITLE_I18N, MyRecipes.sawmill, guiHelper, GuiSawmill.GUI, GlobalBlocks.sawmill, GuiSawmill.class);
	}

	private void addMachineRecipes(IModRegistry registry, String uid, String titleI18N, IRecipeManager recipeManager, IGuiHelper guiHelper, ResourceLocation image, ItemStack itemStack, Class guiClass) {
		MyRecipeCategory category = new DefaultMyRecipeCategory(uid, Local.get(titleI18N), recipeManager, guiHelper, image);
		registry.addRecipeCategories(category);
		registry.addRecipes(MyRecipeWrapper.createRecipes(category));
		registry.addRecipeClickArea(guiClass, 50, 34, 24, 14, uid);
		registry.addRecipeCategoryCraftingItem(itemStack, uid);
	}

}
