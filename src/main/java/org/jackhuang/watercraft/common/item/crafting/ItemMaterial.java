package org.jackhuang.watercraft.common.item.crafting;

import java.util.List;

import gregtech.api.GregTech_API;
import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.InternalName;
import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.client.render.IIconContainer;
import org.jackhuang.watercraft.client.render.RecolorableTextures;
import org.jackhuang.watercraft.common.item.ItemBase;
import org.jackhuang.watercraft.common.item.ItemRecolorable;
import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.common.recipe.IRecipeRegistrator;
import org.jackhuang.watercraft.common.recipe.MyRecipeInputItemStack;
import org.jackhuang.watercraft.common.recipe.RecipeAdder;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

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
		return MaterialTypes.values()[craftingType].getShowedName() + " " +
				MaterialForms.values()[levelType].getShowedName();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		try {
			int meta = itemstack.getItemDamage();
			int meterialType = meta / MaterialTypes.space;
			int meterialForm = meta % MaterialTypes.space;
			return "item." + "watermill." + MaterialTypes.values()[meterialType].name() +
					"." + MaterialForms.values()[meterialForm].name();
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
		for(MaterialForms forms : MaterialForms.values()) {
			for(MaterialTypes types : MaterialTypes.values()) {
				IRecipeRegistrator.registerOreDict(forms.name() + types.name(), get(types, forms));
			}
		}
	}
	
	public void registerAllRecipes() {
		if(IRecipeRegistrator.isEnabledGregTechRecipe()) {
			GregTech_API.sRecipeAdder.addAlloySmelterRecipe(get(MaterialTypes.IndustrialSteel, MaterialForms.ingot), get(MaterialTypes.Neodymium, MaterialForms.ingot), get(MaterialTypes.NeodymiumMagnet, MaterialForms.ingot), 240*20, 128);
			GregTech_API.sRecipeAdder.addBlastRecipe(ItemMaterial.get(MaterialTypes.Vanadium, MaterialForms.dust), ItemMaterial.get(MaterialTypes.Steel, MaterialForms.ingot, 2), ItemMaterial.get(MaterialTypes.VanadiumSteel, MaterialForms.ingot, 3), null, 240*20, 512, 2000);
			GregTech_API.sRecipeAdder.addBlastRecipe(ItemMaterial.get(MaterialTypes.Manganese, MaterialForms.dust), ItemMaterial.get(MaterialTypes.Steel, MaterialForms.ingot, 2), ItemMaterial.get(MaterialTypes.ManganeseSteel, MaterialForms.ingot, 3), null, 240*20, 512, 2000);

			IRecipeRegistrator.addShapelessRecipeByOreDictionary(ItemMaterial.get(MaterialTypes.ZincAlloy, MaterialForms.dust, 5),
				"dustSmallMagnesium", "dustSmallAluminium", "dustTinyTitanium", "dustSmallCopper", ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.dust, 4));
		} else {
			IRecipeRegistrator.addShapelessRecipeByOreDictionary(ItemMaterial.get(MaterialTypes.ZincAlloy, MaterialForms.dust, 5),
					ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.dust, 4), "dustCopper");
			IRecipeRegistrator.addShapelessRecipeByOreDictionary(ItemMaterial.get(MaterialTypes.VanadiumSteel, MaterialForms.dust, 3),
					ItemMaterial.get(MaterialTypes.Vanadium, MaterialForms.dust), ItemMaterial.get(MaterialTypes.Steel, MaterialForms.dust, 2));
			if(Mods.IndustrialCraft2.isAvailable)
				IRecipeRegistrator.addShapelessRecipeByOreDictionary(ItemMaterial.get(MaterialTypes.ManganeseSteel, MaterialForms.dust, 4),
						ItemMaterial.get(MaterialTypes.Manganese, MaterialForms.dust), ItemMaterial.get(MaterialTypes.Steel, MaterialForms.dust, 2),
						IC2Items.getItem("coalDust"));
			else
				IRecipeRegistrator.addShapelessRecipeByOreDictionary(ItemMaterial.get(MaterialTypes.ManganeseSteel, MaterialForms.dust, 3),
						ItemMaterial.get(MaterialTypes.Manganese, MaterialForms.dust), ItemMaterial.get(MaterialTypes.Steel, MaterialForms.dust, 2));
		}
		
		for(MaterialTypes types : MaterialTypes.values()) {
			GameRegistry.addShapedRecipe(get(types, MaterialForms.dust),
					"AA", "AA", 'A', get(types, MaterialForms.dustSmall));
			GameRegistry.addShapedRecipe(get(types, MaterialForms.dustSmall, 4),
					"A", 'A', get(types, MaterialForms.dust));
			GameRegistry.addShapedRecipe(get(types, MaterialForms.dust),
					"AAA", "AAA", "AAA", 'A', get(types, MaterialForms.dustTiny));
			GameRegistry.addShapedRecipe(get(types, MaterialForms.dustTiny, 9),
					"A", 'A', get(types, MaterialForms.dust));
			GameRegistry.addShapedRecipe(get(types, MaterialForms.block),
					"AAA", "AAA", "AAA", 'A', get(types, MaterialForms.ingot));
			GameRegistry.addShapedRecipe(get(types, MaterialForms.ingot),
					"AAA", "AAA", "AAA", 'A', get(types, MaterialForms.nugget));
			GameRegistry.addShapedRecipe(get(types, MaterialForms.nugget, 9),
					"A", 'A', get(types, MaterialForms.ingot));
			GameRegistry.addShapedRecipe(get(types, MaterialForms.stick, 2),
					"A", "A", 'A', get(types, MaterialForms.ingot));
			GameRegistry.addShapedRecipe(get(types, MaterialForms.gear), 
					"SPS", "P P", "SPS", 'S', get(types, MaterialForms.stick),
					'P', get(types, MaterialForms.plate));
			GameRegistry.addShapedRecipe(get(types, MaterialForms.ring),
					" S ", "S S", " S ", 'S', get(types, MaterialForms.stick));
			ItemStack aItemStack = get(types, MaterialForms.dust);
			FurnaceRecipes.smelting().func_151394_a(aItemStack, get(types, MaterialForms.ingot), 0);
			aItemStack = get(types, MaterialForms.dustTiny);
			FurnaceRecipes.smelting().func_151394_a(aItemStack, get(types, MaterialForms.nugget), 0);

			RecipeAdder.bender(get(types, MaterialForms.ingot),get(types, MaterialForms.plate));
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
		for(MaterialTypes c : MaterialTypes.values())
			for(MaterialForms l : MaterialForms.values())
				par3List.add(get(c, l));
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
		return new short[] { type.R, type.G, type.B, type.A };
	}

	@Override
	public IIconContainer[] getIconContainers() {
		return RecolorableTextures.METAL;
	}
}
