/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.recipe;

import ic2.api.item.IC2Items;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.block.turbines.TurbineType;
import org.jackhuang.watercraft.common.block.watermills.WaterType;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class IRecipeRegistrar {

	protected static IRecipeRegistrar instance;
	
	public static IRecipeRegistrar getInstance() {
		return instance;
	}

	public static boolean gregtechRecipe = true,
			thaumcraftRecipe = true;
	public static ItemStack industrialDiamond;

	protected Configuration config;
	
	public static void initRecipeConfig(Configuration c) {

		Property p = c.get("recipe", "EnableGregTechRecipe", true);
		gregtechRecipe = p.getBoolean(true) && Mods.GregTech.isAvailable;
		p = c.get("recipe", "EnableThaumcraftRecipe", true);
		thaumcraftRecipe = p.getBoolean(true)
				&& Loader.isModLoaded("Thaumcraft");
		
		p = c.get("recipe", "UseIndustrialDiamond", true);
		industrialDiamond = p.getBoolean(true) ? IC2Items.getItem("industrialDiamond") : IC2Items.getItem("coalChunk");
	}

	public IRecipeRegistrar(Configuration c) {
		instance = this;
		
		config = c;
	}

	public void registerAllRecipes() {
		/*
		 * for (WaterType type : WaterType.values()) {
		 * GameRegistry.registerTileEntity(type.claSS, type.tileEntityName()); }
		 */
		Property p = config.get("enable", "EnableUpdaters", true);
		if (p.getBoolean(true)) {
			registerUpdater();
		}

		// -- Normal recipes registering

		addRecipeByOreDictionary(
				new ItemStack(GlobalBlocks.waterMill, 1, 0),
				" W ", // 1EU/t
				"WTW", " W ", 'W', IC2Items.getItem("waterMill"), 'T',
				IC2Items.getItem("transformerUpgrade"));

		for (int i = 1; i < 7; i++) {
			addRecipeByOreDictionary(
					new ItemStack(GlobalBlocks.waterMill, 1, i), " W ", "WTW",
					" W ", 'W',
					new ItemStack(GlobalBlocks.waterMill, 1, i - 1), 'T',
					IC2Items.getItem("transformerUpgrade"));
		}

		for (int i = 0; i < WaterType.values().length; i++) {
			GameRegistry.addShapelessRecipe(
					new ItemStack(WaterType.values()[i].trousers, 1, 0),
					new ItemStack(GlobalBlocks.waterMill, 1, i),
					Items.iron_leggings);
		}
		
		registerWatermills();
		registerPlugins();
		if(Reference.General.enableMachines)
			registerMachines();
		registerRange();

		p = config.get("enable", "EnableReservoir", true);
		if (p.getBoolean(true)) {

			for (int i = 1; i < TurbineType.values().length; i++) {
				addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, i),
						" W ", "WTW", " W ", 'W', new ItemStack(
								GlobalBlocks.turbine, 1, i - 1), 'T',
								IC2Items.getItem("transformerUpgrade"));
			}
			
			/*for(int i = 0; i < 12; i++) {
				GameRegistry.addShapelessRecipe(new ItemStack(GlobalBlocks.turbine, 1, i),
						new ItemStack(GlobalBlocks.waterMill, 1, i + 4));
				GameRegistry.addShapelessRecipe(new ItemStack(GlobalBlocks.waterMill, 1, i + 4),
						new ItemStack(GlobalBlocks.turbine, 1, i));
			}*/
			
			registerTurbine();
		}
	}

	public abstract void registerWatermills();
	public abstract void registerRange();
	public abstract void registerUpdater();
	public abstract void registerTurbine();
	public abstract void registerMachines();
	public abstract void registerPlugins();
	
	public static boolean isEnabledGregTechRecipe() {
		return gregtechRecipe;
	}

	public static ItemStack getUsualItemStack(ItemStack in) {
		return new ItemStack(in.getItem(), in.stackSize,
				OreDictionary.WILDCARD_VALUE);
	}

	public static void addRecipeByOreDictionary(ItemStack output,
			Object... params) {
		GameRegistry.addRecipe(new ShapedOreRecipe(output, params));
	}

	public static void addShapelessRecipeByOreDictionary(ItemStack output,
			Object... params) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(output, params));
	}
	
	public static void addSmelting(ItemStack input, ItemStack output) {
		FurnaceRecipes.smelting().func_151394_a(input, output, 0);
	}
	
	public static void registerOreDict(String name, ItemStack stack) {
		OreDictionary.registerOre(name, stack);
	}
	
}
