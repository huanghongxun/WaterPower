package org.jackhuang.watercraft.common.item.crafting;

import java.util.List;

import gregtech.api.GregTech_API;
import ic2.api.item.IC2Items;
import org.jackhuang.watercraft.InternalName;
import org.jackhuang.watercraft.client.render.IIconContainer;
import org.jackhuang.watercraft.client.render.RecolorableTextures;
import org.jackhuang.watercraft.common.item.ItemRecolorable;
import org.jackhuang.watercraft.common.recipe.IRecipeRegistrator;
import org.jackhuang.watercraft.common.recipe.RecipeAdder;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ItemMaterial extends ItemRecolorable {

    public static ItemMaterial instance;

    public ItemMaterial() {
	super(InternalName.cptItemMeterial);
	setHasSubtypes(true);

	instance = this;

	registerAllRecipes();
	registerOreDict();
    }

    @Override
    public String getTextureFolder() {
	return "material";
    }

    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack) {
	int meta = par1ItemStack.getItemDamage();
	int craftingType = meta / MaterialTypes.space;
	int levelType = meta % MaterialTypes.space;
	return MaterialTypes.values()[craftingType].getShowedName() + " "
		+ MaterialForms.values()[levelType].getShowedName();
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
	try {
	    int meta = itemstack.getItemDamage();
	    int meterialType = meta / MaterialTypes.space;
	    int meterialForm = meta % MaterialTypes.space;
	    return "item." + "watermill." + MaterialTypes.values()[meterialType].name()
		    + "." + MaterialForms.values()[meterialForm].name();
	} catch (Exception e) {
	    return null;
	}
    }

    @Override
    public boolean stopScanning(ItemStack stack) {
	return false;
    }

    public static ItemStack get(MaterialTypes craftingTypes, MaterialForms levelTypes) {
	return get(craftingTypes, levelTypes, 1);
    }

    public static ItemStack get(MaterialTypes craftingTypes, MaterialForms levelTypes, int mount) {
	return new ItemStack(instance, mount, craftingTypes.ind() + levelTypes.ordinal());
    }

    public void registerOreDict() {
	for (MaterialForms forms : MaterialForms.values()) {
	    for (MaterialTypes types : MaterialTypes.values()) {
		IRecipeRegistrator.registerOreDict(forms.name() + types.name(), get(types, forms));
	    }
	}
    }

    public void registerAllRecipes() {
	if (Mods.ExNihilo.isAvailable) {

	}
	if (IRecipeRegistrator.isEnabledGregTechRecipe()) {
	    GregTech_API.sRecipeAdder.addAlloySmelterRecipe(get(MaterialTypes.IndustrialSteel, MaterialForms.ingot), get(MaterialTypes.Neodymium, MaterialForms.ingot), get(MaterialTypes.NeodymiumMagnet, MaterialForms.ingot), 240 * 20, 128);
	    GregTech_API.sRecipeAdder.addBlastRecipe(ItemMaterial.get(MaterialTypes.Vanadium, MaterialForms.dust), ItemMaterial.get(MaterialTypes.Steel, MaterialForms.ingot, 2), ItemMaterial.get(MaterialTypes.VanadiumSteel, MaterialForms.ingot, 3), null, 240 * 20, 512, 2000);
	    GregTech_API.sRecipeAdder.addBlastRecipe(ItemMaterial.get(MaterialTypes.Manganese, MaterialForms.dust), ItemMaterial.get(MaterialTypes.Steel, MaterialForms.ingot, 2), ItemMaterial.get(MaterialTypes.ManganeseSteel, MaterialForms.ingot, 3), null, 240 * 20, 512, 2000);
	    IRecipeRegistrator.addShapelessRecipeByOreDictionary(ItemMaterial.get(MaterialTypes.ZincAlloy, MaterialForms.dust, 5),
		    "dustSmallMagnesium", "dustSmallAluminium", "dustTinyTitanium", "dustSmallCopper", ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.dust, 4));
	} else {
	    IRecipeRegistrator.addShapelessRecipeByOreDictionary(ItemMaterial.get(MaterialTypes.ZincAlloy, MaterialForms.dust, 5),
		    ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.dust, 4), "dustCopper");
	    IRecipeRegistrator.addShapelessRecipeByOreDictionary(ItemMaterial.get(MaterialTypes.VanadiumSteel, MaterialForms.dust, 3),
		    ItemMaterial.get(MaterialTypes.Vanadium, MaterialForms.dust), ItemMaterial.get(MaterialTypes.Steel, MaterialForms.dust, 2));
	    if (Mods.IndustrialCraft2.isAvailable) {
		IRecipeRegistrator.addShapelessRecipeByOreDictionary(ItemMaterial.get(MaterialTypes.ManganeseSteel, MaterialForms.dust, 4),
			ItemMaterial.get(MaterialTypes.Manganese, MaterialForms.dust), ItemMaterial.get(MaterialTypes.Steel, MaterialForms.dust, 2),
			IC2Items.getItem("coalDust"));
	    } else {
		IRecipeRegistrator.addShapelessRecipeByOreDictionary(ItemMaterial.get(MaterialTypes.ManganeseSteel, MaterialForms.dust, 3),
			ItemMaterial.get(MaterialTypes.Manganese, MaterialForms.dust), ItemMaterial.get(MaterialTypes.Steel, MaterialForms.dust, 2));
	    }
	}

	for (MaterialTypes types : MaterialTypes.values()) {
	    GameRegistry.addShapedRecipe(get(types, MaterialForms.dust), // 4 small dusts -> 1 dust
		    "AA", "AA", 'A', get(types, MaterialForms.dustSmall));

	    GameRegistry.addShapedRecipe(get(types, MaterialForms.dustSmall, 4), // 1 dust -> 4 small dusts
		    "A", 'A', get(types, MaterialForms.dust));

	    GameRegistry.addShapedRecipe(get(types, MaterialForms.dust), // 9 tiny dusts -> 1 dust
		    "AAA", "AAA", "AAA", 'A', get(types, MaterialForms.dustTiny));

	    GameRegistry.addShapedRecipe(get(types, MaterialForms.dustTiny, 9), // 1 dust -> 9 tiny dust
		    "A", 'A', get(types, MaterialForms.dust));

	    GameRegistry.addShapedRecipe(get(types, MaterialForms.block), // 9 ingots -> 1 block 
		    "AAA", "AAA", "AAA", 'A', get(types, MaterialForms.ingot));
	    RecipeAdder.compressor(get(types, MaterialForms.ingot, 9), get(types, MaterialForms.block));

	    GameRegistry.addShapedRecipe(get(types, MaterialForms.ingot), // 9 nuggets -> 1 ingot
		    "AAA", "AAA", "AAA", 'A', get(types, MaterialForms.nugget));
	    RecipeAdder.compressor(get(types, MaterialForms.nugget, 9), get(types, MaterialForms.ingot));

	    GameRegistry.addShapedRecipe(get(types, MaterialForms.nugget, 9), // 1 ingot -> 9 nuggets
		    "A", 'A', get(types, MaterialForms.ingot));

	    GameRegistry.addShapedRecipe(get(types, MaterialForms.stick, 2), // 2 ingots -> 2 stick
		    "A", "A", 'A', get(types, MaterialForms.ingot));

	    GameRegistry.addShapedRecipe(get(types, MaterialForms.gear), // some sticks & plates -> 1 gear
		    "SPS", "P P", "SPS", 'S', get(types, MaterialForms.stick),
		    'P', get(types, MaterialForms.plate));

	    GameRegistry.addShapedRecipe(get(types, MaterialForms.ring), // 4 sticks -> 1 ring
		    " S ", "S S", " S ", 'S', get(types, MaterialForms.stick));

	    ItemStack dust = get(types, MaterialForms.dust);
	    FurnaceRecipes.smelting().func_151394_a(dust, get(types, MaterialForms.ingot), 0);
	    dust = get(types, MaterialForms.dustTiny);
	    FurnaceRecipes.smelting().func_151394_a(dust, get(types, MaterialForms.nugget), 0);

	    RecipeAdder.bender(get(types, MaterialForms.ingot), get(types, MaterialForms.plate));
	    RecipeAdder.bender(get(types, MaterialForms.plate, 9), get(types, MaterialForms.plateDense));
	    RecipeAdder.macerator(get(types, MaterialForms.plateDense), get(types, MaterialForms.dust, 9));
	    RecipeAdder.macerator(get(types, MaterialForms.screw), get(types, MaterialForms.dustSmall));
	    RecipeAdder.cutter(get(types, MaterialForms.block), get(types, MaterialForms.plate, 9));
	    RecipeAdder.lathe(get(types, MaterialForms.stick), get(types, MaterialForms.screw, 4));
	}
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs,
	    List par3List) {
	for (MaterialTypes c : MaterialTypes.values()) {
	    for (MaterialForms l : MaterialForms.values()) {
		par3List.add(get(c, l));
	    }
	}
    }

    public IIconContainer getIconContainer(int meta, MaterialTypes type) {
	return getIconContainers()[meta % MaterialTypes.space];
    }

    @Override
    public IIconContainer getIconContainer(int meta) {
	return getIconContainer(meta, MaterialTypes.values()[meta / MaterialTypes.space]);
    }

    @Override
    public short[] getRGBA(ItemStack stack) {
	int meta = stack.getItemDamage();
	int craftingType = meta / MaterialTypes.space;
	MaterialTypes type = MaterialTypes.values()[craftingType];
	return new short[]{type.R, type.G, type.B, type.A};
    }

    @Override
    public IIconContainer[] getIconContainers() {
	return RecolorableTextures.METAL;
    }
}
