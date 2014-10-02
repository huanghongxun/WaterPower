package org.jackhuang.watercraft.common.recipe;

import gregtech.api.GregTech_API;
import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.block.simple.ItemOreDust;
import org.jackhuang.watercraft.common.block.simple.OreType;
import org.jackhuang.watercraft.common.block.turbines.TurbineType;
import org.jackhuang.watercraft.common.item.GlobalItems;
import org.jackhuang.watercraft.common.item.crafting.CraftingTypes;
import org.jackhuang.watercraft.common.item.crafting.ItemCrafting;
import org.jackhuang.watercraft.common.item.crafting.ItemMaterial;
import org.jackhuang.watercraft.common.item.crafting.LevelTypes;
import org.jackhuang.watercraft.common.item.crafting.MaterialForms;
import org.jackhuang.watercraft.common.item.crafting.MaterialTypes;
import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.util.mods.Mods;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class NormalRecipeHandler extends EasyRecipeHandler {

	public NormalRecipeHandler(Configuration c) {
		super(c);
	}

	@Override
	public void registerWatermills() {
		// MK1
		addWatermillRecipe(0, LevelTypes.MK1);
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.stator, LevelTypes.MK1), "M",
				"M", 'M', "dustMagnet");
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK1), "WSW",
				"WSW", "WSW", 'W', ItemType.StoneStruct.item(), 'S',
				Blocks.stonebrick);
		this.addRecipeByOreDictionary(ItemType.StoneStruct.item(), "WW ",
				"WWS", "WW ", 'W', Blocks.stonebrick, 'S',
				ItemType.WoodenHammer.item());
		addPaddleBaseRecipe(LevelTypes.MK1);
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.drainagePlate, LevelTypes.MK1),
				"WW ", "WWS", "WW ", 'W', "plankWood", 'S',
				ItemType.WoodenHammer.item());
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.fixedFrame, LevelTypes.MK1),
				"WWW", "WSW", "WWW", 'W', "logWood", 'S', "plankWood");
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.fixedTool, LevelTypes.MK1),
				"WW", "AW", 'W', "logWood", 'A', Items.string);
		this.addRecipeByOreDictionary(ItemType.WoodenHammer.item(), "WW ",
				"WWI", "WW ", 'W', "logWood", 'I', Items.stick);
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.outputInterface, LevelTypes.MK1),
				"GW", " G", "GW", 'G', IC2Items.getItem("goldCableItem"), 'W',
				"plankWood");
		this.addShapelessRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK1),
				ItemType.WaterResistantRubber.item(),
				IC2Items.getItem("electronicCircuit"));
		this.addShapelessRecipeByOreDictionary(
				ItemType.WaterResistantRubber.item(), "itemRubber",
				"itemRubber", "itemRubber", "itemRubber");
		if (gregtechRecipe) {
			// GregTech_API.sRecipeAdder.addBenderRecipe(ItemType.WaterResistantRubber.item(),
			// ItemType.WaterResistantRubberPlate.item(), 2*20, 2);
		} else {
			Recipes.compressor.addRecipe(new RecipeInputItemStack(
					ItemType.WaterResistantRubber.item()), null,
					ItemType.WaterResistantRubberPlate.item());
		}
		OreDictionary.registerOre("plateRubber",
				ItemType.WaterResistantRubberPlate.item());
		OreDictionary.registerOre("plateDenseRubber",
				ItemType.WaterResistantRubberDensePlate.item());
		Recipes.compressor.addRecipe(new RecipeInputItemStack(
				ItemType.WaterResistantRubberPlate.item(9)), null,
				ItemType.WaterResistantRubberDensePlate.item());
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.rotor, LevelTypes.MK1), "CCC",
				"CAC", "CCC", 'C', IC2Items.getItem("copperCableItem"), 'A',
				"dustMagnet");
		Recipes.compressor.addRecipe(new RecipeInputOreDict("dustMagnet"),
				null,
				ItemMaterial.get(MaterialTypes.Magnet, MaterialForms.nugget));
		Recipes.compressor.addRecipe(new RecipeInputOreDict("nuggetMagnet", 9),
				null,
				ItemMaterial.get(MaterialTypes.Magnet, MaterialForms.ingot));
		// this.addSmelting(Items.getItem(""), output);

		// MK3
		addWatermillRecipe(2, LevelTypes.MK3);
		addCasingRecipe(LevelTypes.MK3, "stickZincAlloy", "plateZincAlloy",
				"blockZinc");
		addPaddleBaseRecipe(LevelTypes.MK3);
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.drainagePlate, LevelTypes.MK3),
				"WW", "AW", "KW", 'W', "plateZincAlloy", 'A',
				IC2Items.getItem("ironScaffold"), 'K', "screwZinc");
		if (gregtechRecipe) {
			// GregTech_API.sRecipeAdder.addAssemblerRecipe(ItemMaterial.get(MaterialTypes.Zinc,
			// MaterialForms.plate, 4), ItemMaterial.get(MaterialTypes.Zinc,
			// MaterialForms.screw, 4),
			// ItemCrafting.get(CraftingTypes.fixedFrame, LevelTypes.MK3), 4000,
			// 5);
			// GregTech_API.sRecipeAdder.addAssemblerRecipe(ItemMaterial.get(MaterialTypes.Zinc,
			// MaterialForms.ingot, 2), ItemMaterial.get(MaterialTypes.Zinc,
			// MaterialForms.screw, 4),
			// ItemCrafting.get(CraftingTypes.fixedTool, LevelTypes.MK3), 4000,
			// 5);
		} else {
			addRecipeByOreDictionary(
					ItemCrafting.get(CraftingTypes.fixedFrame, LevelTypes.MK3),
					"PSP", "S S", "PSP", 'P',
					ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.plate),
					'S',
					ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.screw));
			addRecipeByOreDictionary(
					ItemCrafting.get(CraftingTypes.fixedTool, LevelTypes.MK3),
					"PSP", "   ", "PSP", 'P',
					ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.ingot),
					'P',
					ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.screw));
		}
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.outputInterface, LevelTypes.MK3),
				"PRB", "RAI", "PRB", 'P', "plateZincAlloy", 'R', "plateRubber",
				'I', "ingotZincAlloy", 'A', IC2Items.getItem("lvTransformer"),
				'B', IC2Items.getItem("reBattery"));
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.rotationAxle, LevelTypes.MK3),
				"GPG", "ICI", "GPG", 'G', "gearZincAlloy", 'P',
				"plateZincAlloy", 'I', "plateIron", 'C',
				ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK3));
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK3), "PPP",
				"CDC", "PPP", 'P',
				ItemType.WaterResistantRubberDensePlate.item(), 'C',
				IC2Items.getItem("electronicCircuit"), 'P',
				ItemType.WaterResistantRubberPlate.item(), 'D',
				ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK3));
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.stator, LevelTypes.MK3), "PIS",
				"PI ", "PIS", 'P', "plateZincAlloy", 'I', "dustMagnet", 'S',
				"stickZincAlloy");
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.rotor, LevelTypes.MK3), "CIC",
				"GIG", "G G", 'G', IC2Items.getItem("goldCableItem"), 'C',
				ItemType.DenseCoil.item(), 'I', "ingotIron");

		// MK4
		addWatermillRecipe(3, LevelTypes.MK4);
		addCasingRecipe(LevelTypes.MK4, "stickIndustrialSteel",
				"plateIndustrialSteel", "blockIndustrialSteel");
		addPaddleBaseRecipe(LevelTypes.MK4);
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.drainagePlate, LevelTypes.MK4),
				"WW", "KW", "KW", 'W', "plateIndustrialSteel", 'K',
				"blockIndustrialSteel");
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.fixedFrame, LevelTypes.MK4),
				"P  ", "DP ", "SDP", 'P', "plateIndustrialSteel", 'D',
				"plateDenseIndustrialSteel", 'S', "screwIndustrialSteel");
		if (gregtechRecipe) {
			// GregTech_API.sRecipeAdder.addAssemblerRecipe(ItemMaterial.get(MaterialTypes.IndustrialSteel,
			// MaterialForms.plateDense),
			// ItemMaterial.get(MaterialTypes.IndustrialSteel,
			// MaterialForms.ingot),
			// ItemCrafting.get(CraftingTypes.fixedTool, LevelTypes.MK4), 6000,
			// 5);
		} else {
			this.addShapelessRecipeByOreDictionary(
					ItemCrafting.get(CraftingTypes.fixedTool, LevelTypes.MK4),
					"plateDenseIndustrialSteel", "ingotIndustrialSteel");
		}
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.outputInterface, LevelTypes.MK4),
				"SSP", "PTB", "SSP", 'S', "blockSilver", 'P',
				"plateIndustrialSteel", 'T', IC2Items.getItem("mvTransformer"),
				'B', IC2Items.getItem("suBattery"));
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK4), "PPP",
				"CDC", "BPB", 'P', ItemType.DenseRedstonePlate.item(), 'C',
				IC2Items.getItem("advancedCircuit"), 'P',
				ItemType.WaterResistantRubberPlate.item(), 'D',
				ItemType.DataBall.item(), 'B', "platePlatinum");
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK4), "PPP",
				"CDC", "BPB", 'P', ItemType.DenseRedstonePlate.item(), 'C',
				IC2Items.getItem("advancedCircuit"), 'P',
				ItemType.WaterResistantRubberPlate.item(), 'D',
				ItemType.DataBall.item(), 'B', "plateVanadiumSteel");
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.stator, LevelTypes.MK4), "PP",
				"PB", "PP", 'P', "plateIndustrialSteel", 'B',
				"blockNeodymiumMangnet");
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.rotor, LevelTypes.MK4), "SPS",
				"PBP", "SPS", 'S', ItemType.DenseSilverCoil.item(), 'P',
				"plateIndustrialSteel", 'B', Blocks.diamond_block);
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.rotationAxle, LevelTypes.MK4),
				"GPG", "ICI", "GPG", 'G', "gearIndustrialSteel", 'P',
				"plateIndustrialSteel", 'I', "plateIron", 'C',
				ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK4));

		// MK5
		addWatermillRecipe(4, LevelTypes.MK5);
		addCasingRecipe(LevelTypes.MK5, "plateVanadiumSteel",
				"plateVanadiumSteel", "blockVanadiumSteel");
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK5), "PDP",
				"DCD", "PDP", 'P',
				ItemType.WaterResistantRubberDensePlate.item(), 'C',
				IC2Items.getItem("energyCrystal"), 'D',
				ItemType.DataBall.item());
	}

	private void addWatermillRecipe(int meta, LevelTypes level) {
		this.addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1,
				meta), "CBC", "IAT", "SRS", 'C', ItemCrafting.get(
				CraftingTypes.casing, level), 'I', ItemCrafting.get(
				CraftingTypes.outputInterface, level), 'A', ItemCrafting.get(
				CraftingTypes.rotationAxle, level), 'B', ItemCrafting.get(
				CraftingTypes.paddleBase, level), 'S', ItemCrafting.get(
				CraftingTypes.stator, level), 'R', ItemCrafting.get(
				CraftingTypes.rotor, level), 'T', ItemCrafting.get(
				CraftingTypes.circuit, level));
	}

	private void addCasingRecipe(LevelTypes level, Object stick, Object plate,
			Object casing) {
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.casing, level), "WSW", "WAW",
				"WSW", 'W', stick, 'S', plate, 'A', casing);
	}

	private void addPaddleBaseRecipe(LevelTypes level) {
		this.addRecipeByOreDictionary(
				ItemCrafting.get(CraftingTypes.paddleBase, level), "W W",
				"SAS", 'W',
				ItemCrafting.get(CraftingTypes.drainagePlate, level), 'S',
				ItemCrafting.get(CraftingTypes.fixedTool, level), 'A',
				ItemCrafting.get(CraftingTypes.fixedFrame, level));
	}

	private ItemStack changeMount(ItemStack base, int newMount) {
		ItemStack iStack = base.copy();
		iStack.stackSize = newMount;
		return iStack;
	}

	@Override
	public void registerUpdater() {
		super.registerUpdater();

		if (Mods.GregTech.isAvailable) {
			GregTech_API.sRecipeAdder.addChemicalRecipe(
					changeMount(IC2Items.getItem("airCell"), 3),
					IC2Items.getItem("biofuelCell"),
					ItemType.OxygenEthanolFuel.item(4), 20);
			GregTech_API.sRecipeAdder.addAssemblerRecipe(
					IC2Items.getItem("coil"),
					IC2Items.getItem("copperCableItem"),
					ItemType.DenseCoil.item(), 120 * 20, 2);

			GregTech_API.sRecipeAdder.addAssemblerRecipe(
					ItemType.SilverCoil.item(), ItemType.SilverCoil.item(),
					ItemType.DenseSilverCoil.item(), 240 * 20, 4);
			addRecipeByOreDictionary(ItemType.SilverCoil.item(), "SSS", "SIS",
					"SSS", 'S', "ingotSilver", 'I', "ingotIndustrialSteel");

			GregTech_API.sRecipeAdder.addBlastRecipe(
					IC2Items.getItem("carbonMesh"), null,
					ItemType.HighPurityCarbonDust.item(), null, 240 * 20, 512,
					3000);
		}
	}

}
