package org.jackhuang.watercraft.common.block.ore;

import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.item.GlobalItems;
import org.jackhuang.watercraft.common.item.crafting.ItemMaterial;
import org.jackhuang.watercraft.common.item.crafting.MaterialForms;
import org.jackhuang.watercraft.common.item.crafting.MaterialTypes;

import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public enum OreType {
	Monazite(MaterialTypes.Neodymium),
	Vanadium(MaterialTypes.Vanadium),
	Manganese(MaterialTypes.Manganese),
	Magnet(MaterialTypes.Magnet),
	Zinc(MaterialTypes.Zinc);
	
	public short R, G, B, A;
	
	private OreType(MaterialTypes t) {
		R = t.R;
		G = t.G;
		B = t.B;
		A = t.A;
	}
	
	public String getShowedName() {
		return StatCollector.translateToLocal(getUnlocalizedName());
	}
	
	public String getUnlocalizedName() {
		return "cptwtrml.ore." + name().toLowerCase();
	}
	
	public static void registerRecipes() {
		for(OreType o : OreType.values()) {
			Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(GlobalBlocks.ore, 1, o.ordinal())), null,
					new ItemStack(GlobalItems.oreDust, 1, o.ordinal()));
		}
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalBlocks.ore, 1, Monazite.ordinal()),
				ItemMaterial.get(MaterialTypes.Neodymium, MaterialForms.ingot), 0f);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalBlocks.ore, 1, Vanadium.ordinal()),
				ItemMaterial.get(MaterialTypes.Vanadium, MaterialForms.ingot), 0f);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalBlocks.ore, 1, Manganese.ordinal()),
				ItemMaterial.get(MaterialTypes.Manganese, MaterialForms.ingot), 0f);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalBlocks.ore, 1, Magnet.ordinal()),
				ItemMaterial.get(MaterialTypes.Magnet, MaterialForms.ingot), 0f);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalBlocks.ore, 1, Zinc.ordinal()),
				ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.ingot), 0f);

		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalItems.oreDust, 1, Monazite.ordinal()),
				ItemMaterial.get(MaterialTypes.Neodymium, MaterialForms.ingot), 0f);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalItems.oreDust, 1, Vanadium.ordinal()),
				ItemMaterial.get(MaterialTypes.Vanadium, MaterialForms.ingot), 0f);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalItems.oreDust, 1, Manganese.ordinal()),
				ItemMaterial.get(MaterialTypes.Manganese, MaterialForms.ingot), 0f);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalItems.oreDust, 1, Magnet.ordinal()),
				ItemMaterial.get(MaterialTypes.Magnet, MaterialForms.ingot), 0f);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalItems.oreDust, 1, Zinc.ordinal()),
				ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.ingot), 0f);

		NBTTagCompound metadata = new NBTTagCompound();
		metadata.setInteger("amount", 1000);
		ItemStack item = IC2Items.getItem("stoneDust");
		ItemStack iron = IC2Items.getItem("smallIronDust");
		
		Recipes.oreWashing.addRecipe(new RecipeInputItemStack(new ItemStack(GlobalItems.oreDust, 1, Monazite.ordinal())), metadata,
				ItemMaterial.get(MaterialTypes.Neodymium, MaterialForms.dust), item);
		Recipes.oreWashing.addRecipe(new RecipeInputItemStack(new ItemStack(GlobalItems.oreDust, 1, Vanadium.ordinal())), metadata,
				ItemMaterial.get(MaterialTypes.Vanadium, MaterialForms.dust), iron, item);
		Recipes.oreWashing.addRecipe(new RecipeInputItemStack(new ItemStack(GlobalItems.oreDust, 1, Manganese.ordinal())), metadata,
				ItemMaterial.get(MaterialTypes.Manganese, MaterialForms.dust), iron, item);
		Recipes.oreWashing.addRecipe(new RecipeInputItemStack(new ItemStack(GlobalItems.oreDust, 1, Magnet.ordinal())), metadata,
				ItemMaterial.get(MaterialTypes.Magnet, MaterialForms.dust), iron, item);
		Recipes.oreWashing.addRecipe(new RecipeInputItemStack(new ItemStack(GlobalItems.oreDust, 1, Zinc.ordinal())), metadata,
				ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.dust), iron, item);
	}
}
