package org.jackhuang.compactwatermills.common.recipe;

import thaumcraft.api.ItemApi;
import gregtech.api.GregTech_API;
import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.api.MyRecipes;
import org.jackhuang.compactwatermills.common.block.GlobalBlocks;
import org.jackhuang.compactwatermills.common.block.reservoir.BlockReservoir;
import org.jackhuang.compactwatermills.common.block.reservoir.ReservoirType;
import org.jackhuang.compactwatermills.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.compactwatermills.common.block.turbines.BlockTurbine;
import org.jackhuang.compactwatermills.common.block.turbines.TileEntityTurbine;
import org.jackhuang.compactwatermills.common.block.turbines.TurbineType;
import org.jackhuang.compactwatermills.common.block.watermills.BlockCompactWatermill;
import org.jackhuang.compactwatermills.common.block.watermills.TileEntityWatermill;
import org.jackhuang.compactwatermills.common.block.watermills.WaterType;
import org.jackhuang.compactwatermills.common.item.GlobalItems;
import org.jackhuang.compactwatermills.common.item.crafting.CraftingTypes;
import org.jackhuang.compactwatermills.common.item.crafting.ItemCrafting;
import org.jackhuang.compactwatermills.common.item.crafting.ItemMaterial;
import org.jackhuang.compactwatermills.common.item.crafting.LevelTypes;
import org.jackhuang.compactwatermills.common.item.crafting.MaterialForms;
import org.jackhuang.compactwatermills.common.item.crafting.MaterialTypes;
import org.jackhuang.compactwatermills.common.item.others.ItemOthers;
import org.jackhuang.compactwatermills.common.item.others.ItemType;
import org.jackhuang.compactwatermills.common.item.range.ItemRange;
import org.jackhuang.compactwatermills.common.item.range.PluginType;
import org.jackhuang.compactwatermills.common.item.rotors.RotorType;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class EasyRecipeHandler extends IRecipeHandler {

	public EasyRecipeHandler(Configuration c) {
		super(c);
	}

	public void registerWatermills() {
		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 0),
				"CUC", "SAS", "PMP", 'C', IC2Items.getItem("FluidCell"), 'A',
				IC2Items.getItem("waterMill"), 'S',
				"plateIron"/* IC2Items.getItem("plateiron") */, 'P',
				IC2Items.getItem("electronicCircuit"), 'M',
				ItemType.MK0.item(), 'U',
				ItemType.WaterUraniumIngot.item());

		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 1),
				"CUC", "SAS", "PMP", 'C', IC2Items.getItem("FluidCell"), 'A',
				new ItemStack(GlobalBlocks.waterMill, 1, 0), 'S', /*
																 * IC2Items.getItem(
																 * "plateiron")
																 */
				"plateIron", 'P', IC2Items.getItem("advancedCircuit"), 'M',
				ItemType.MK0.item(), 'U',
				ItemType.WaterUraniumIngot.item());

		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 3),
				"CUC", "SAS", "PMP", 'C', IC2Items.getItem("FluidCell"), 'A',
				new ItemStack(GlobalBlocks.waterMill, 1, 1), 'S',
				IC2Items.getItem("carbonPlate"), 'P',
				IC2Items.getItem("advancedCircuit"), 'M', ItemType.MK1.item(),
				'U', ItemType.WaterUraniumPlateMK1.item());

		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 4),
				"CUC", "SAS", "PMP", 'C', IC2Items.getItem("FluidCell"), 'A',
				new ItemStack(GlobalBlocks.waterMill, 1, 3), 'S',
				IC2Items.getItem("advancedAlloy"), 'P',
				IC2Items.getItem("advancedCircuit"), 'M', ItemType.MK2.item(),
				'U', ItemType.WaterUraniumPlateMK2.item());

		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 5),
				"CUC", "SAS", "PMP", 'C', IC2Items.getItem("FluidCell"), 'A',
				new ItemStack(GlobalBlocks.waterMill, 1, 4), 'S',
				IC2Items.getItem("elemotor"), 'P',
				IC2Items.getItem("advancedCircuit"), 'M', ItemType.MK2.item(),
				'U', ItemType.WaterUraniumPlateMK2.item());

		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 6),
				"CUC", "SAS", "PMP", 'C', IC2Items.getItem("FluidCell"), 'A',
				new ItemStack(GlobalBlocks.waterMill, 1, 5), 'S',
				getUsualItemStack(IC2Items.getItem("lapotronCrystal")), 'P',
				IC2Items.getItem("advancedCircuit"), 'M', ItemType.MK3.item(),
				'U', ItemType.WaterUraniumPlateMK3.item());

		/*if (!GregTech_API.isGregTechLoaded())
			return;

		// GregTech_API.getGregTechItem(aIndex, aAmount, aMeta)

		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 7),
				"CUC", "SAS", "PMP", 'C', IC2Items.getItem("FluidCell"), 'A',
				new ItemStack(GlobalBlocks.waterMill, 1, 6), 'S',
				IC2Items.getItem("iridiumPlate"), 'P',
				IC2Items.getItem("advancedCircuit"), 'M', UpdaterType.MK3.item(),
				'U', UpdaterType.WaterUraniumPlateMK3.item());

		// 数据流
		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 8),
				"CUC", "SAS", "PMP", 'C',
				GregTech_API.getGregTechItem(34, 1, 0), 'A', new ItemStack(
						GlobalBlocks.waterMill, 1, 7), 'S',
				GregTech_API.getGregTechComponent(1, 1), 'P', Items // Turn
																	// 数据流电路
						.getItem("advancedCircuit"), 'M',
				UpdaterType.MK4.item(), 'U',
				UpdaterType.WaterUraniumPlateMK4.item());

		// 数据流
		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 9),
				"CUC", "SAS", "PMP", 'C',
				GregTech_API.getGregTechItem(35, 1, 0), 'A', new ItemStack(
						GlobalBlocks.waterMill, 1, 8), 'S',
				GregTech_API.getGregTechItem(43, 1, 0), 'P', Items // Turn 数据流电路
						.getItem("advancedCircuit"), 'M',
				UpdaterType.MK4.item(), 'U',
				UpdaterType.WaterUraniumPlateMK4.item());

		// He180k冷却
		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 10),
				"CUC", "SAS", "PMP", 'C',
				GregTech_API.getGregTechItem(36, 1, 0), 'A', new ItemStack(
						GlobalBlocks.waterMill, 1, 9), 'S',
				GregTech_API.getGregTechItem(36, 1, 0), 'P', Items // Turn 数据流电路
						.getItem("advancedCircuit"), 'M',
				UpdaterType.MK5.item(), 'U',
				UpdaterType.WaterUraniumPlateMK5.item());

		// Nak180K冷却
		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 11),
				"CUC", "SAS", "PMP", 'C',
				GregTech_API.getGregTechItem(60, 1, 0), 'A', new ItemStack(
						GlobalBlocks.waterMill, 1, 10), 'S',
				GregTech_API.getGregTechItem(62, 1, 0), 'P',
				IC2Items.getItem("advancedCircuit"), 'M', UpdaterType.MK5.item(),
				'U', UpdaterType.WaterUraniumPlateMK5.item());

		// 能量
		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 12),
				"CUC", "SAS", "PMP", 'C',
				GregTech_API.getGregTechItem(61, 1, 0), 'A', new ItemStack(
						GlobalBlocks.waterMill, 1, 11), 'S',
				GregTech_API.getGregTechComponent(1, 0), 'P',
				IC2Items.getItem("advancedCircuit"), 'M', UpdaterType.MK6.item(),
				'U', UpdaterType.WaterUraniumPlateMK6.item());

		// 超导体
		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 13),
				"CUC", "SAS", "PMP", 'C',
				GregTech_API.getGregTechItem(62, 1, 0), 'A', new ItemStack(
						GlobalBlocks.waterMill, 1, 12), 'S',
				GregTech_API.getGregTechComponent(1, 2), 'P',
				IC2Items.getItem("advancedCircuit"), 'M', UpdaterType.MK6.item(),
				'U', UpdaterType.WaterUraniumPlateMK6.item());

		// 铱中子板
		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 14),
				"CUC", "SAS", "PMP", 'C', IC2Items.getItem("FluidCell"), 'A',
				new ItemStack(GlobalBlocks.waterMill, 1, 13), 'S',
				GregTech_API.getGregTechItem(40, 1, 0), 'P',
				IC2Items.getItem("advancedCircuit"), 'M', UpdaterType.MK7.item(),
				'U', UpdaterType.WaterUraniumPlateMK7.item());

		// 铱
		addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 15),
				"CUC", "SAS", "PMP", 'C', IC2Items.getItem("FluidCell"), 'A',
				new ItemStack(GlobalBlocks.waterMill, 1, 14), 'S',
				GregTech_API.getGregTechBlock(1, 1, 12), 'P',
				IC2Items.getItem("advancedCircuit"), 'M', UpdaterType.MK7.item(),
				'U', UpdaterType.WaterUraniumPlateMK7.item());*/

	}

	public void registerRange() {
		if (gregtechRecipe) {
			addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 0),
					"WSW", "SAS", "WSW", 'W',
					IC2Items.getItem("electronicCircuit"), 'S',
					getUsualItemStack(IC2Items.getItem("energyCrystal")), 'A',
					IC2Items.getItem("machine"));
			addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 1),
					"WSW", "SAS", "WSW", 'W', IC2Items.getItem("advancedCircuit"),
					'S', getUsualItemStack(IC2Items.getItem("lapotronCrystal")),
					'A', new ItemStack(GlobalItems.range, 1, 0));
			addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 2),
					"WSW", "SAS", "WSW", 'W',
					getUsualItemStack(IC2Items.getItem("energyCrystal")), 'S',
					IC2Items.getItem("mfeUnit"), 'A', new ItemStack(
							GlobalItems.range, 1, 1));
			addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 3),
					"WSW", "SAS", "WSW", 'W',
					getUsualItemStack(IC2Items.getItem("lapotronCrystal")), 'S',
					IC2Items.getItem("mfsUnit"), 'A', new ItemStack(
							GlobalItems.range, 1, 2));
		} else {
			addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 0),
					"WSW", "SAS", "WSW", 'W',
					IC2Items.getItem("electronicCircuit"), 'S',
					IC2Items.getItem("batBox"), 'A', IC2Items.getItem("machine"));
			addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 1),
					"WSW", "SAS", "WSW", 'W', IC2Items.getItem("advancedCircuit"),
					'S', IC2Items.getItem("cesuUnit"), 'A', new ItemStack(
							GlobalItems.range, 1, 0));
			addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 2),
					"WSW", "SAS", "WSW", 'W',
					getUsualItemStack(IC2Items.getItem("energyCrystal")), 'S',
					IC2Items.getItem("mfeUnit"), 'A', new ItemStack(
							GlobalItems.range, 1, 1));
			addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 3),
					"WSW", "SAS", "WSW", 'W',
					getUsualItemStack(IC2Items.getItem("lapotronCrystal")), 'S',
					IC2Items.getItem("mfsUnit"), 'A', new ItemStack(
							GlobalItems.range, 1, 2));
		}
	}

	public void registerUpdater() {
		ItemStack is = ItemType.WaterUraniumIngot.item().copy();
		is.stackSize = 36;
		ItemStack is2 = ItemType.WaterUraniumIngot.item().copy();
		is2.stackSize = 4;
		/*
		 * if(buildcraftRecipe) { ItemStack uraniumBlock =
		 * IC2Items.getItem("uraniumBlock").copy(); ItemStack reactorCollantSix =
		 * IC2Items.getItem("reactorCoolantSix").copy(); uraniumBlock.stackSize =
		 * 4; reactorCollantSix.stackSize = 4;
		 * AssemblyRecipe.assemblyRecipes.add(new AssemblyRecipe( new
		 * ItemStack[] {uraniumBlock, reactorCollantSix }, 1000, is));
		 * 
		 * if (GregTech_API.isGregTechLoaded()) {
		 * AssemblyRecipe.assemblyRecipes.add(new AssemblyRecipe( new
		 * ItemStack[] {GregTech_API.getGregTechMaterial(43, 1),
		 * IC2Items.getItem("electrolyzedWaterCell") }, 250, is)); } } else {
		 */
		addRecipeByOreDictionary(is, "SAS", "ASA", "SAS", 'A',
		// IC2Items.getItem("uraniumBlock"),
				"blockUranium", 'S', IC2Items.getItem("reactorCoolantSix"));

			/*
			 * if(railcraftRecipe) {
			 * RailcraftCraftingManager.rollingMachine.addRecipe(is2, "SAS",
			 * "ASA", "SAS", 'A', GregTech_API.getGregTechMaterial(43, 1), 'S',
			 * IC2Items.getItem("electrolyzedWaterCell")); } else {
			 */
			addRecipeByOreDictionary(is2, "SAS", "ASA", "SAS", 'A',
					"ingotUranium",
					// GregTech_API.getGregTechMaterial(43, 1),
					'S', IC2Items.getItem("electrolyzedWaterCell"));
			// }
		// }
		addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK1.item(),
				"SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumIngot.item(),
				'S', "plateBronze");
		// IC2Items.getItem("platebronze"));
		addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK2.item(),
				"SSS", "SAS", "SSS", 'A',
				ItemType.WaterUraniumPlateMK1.item(), 'S',
				IC2Items.getItem("carbonPlate"));
		addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK3.item(),
				"SSS", "SAS", "SSS", 'A',
				ItemType.WaterUraniumPlateMK2.item(), 'S',
				IC2Items.getItem("advancedAlloy"));
			addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK4.item(),
					"SSS", "SAS", "SSS", 'A',
					ItemType.WaterUraniumPlateMK3.item(), 'S',
					"plateTungsten"/*GregTech_API.getGregTechMaterial(80, 1)*/);
			addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK5.item(),
					"SSS", "SAS", "SSS", 'A',
					ItemType.WaterUraniumPlateMK4.item(), 'S',
					"plateStainlessSteel"/*GregTech_API.getGregTechMaterial(66, 1)*/);
			addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK6.item(),
					"SSS", "SAS", "SSS", 'A',
					ItemType.WaterUraniumPlateMK5.item(), 'S',
					"plateTungstenSteel");
			addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK7.item(),
					"SSS", "SAS", "SSS", 'A',
					ItemType.WaterUraniumPlateMK6.item(), 'S',
					IC2Items.getItem("iridiumPlate"));
		Object iron = "plateIron";
		ItemStack machine = IC2Items.getItem("machine");
		if (gregtechRecipe) {
			iron = "plateStainlessSteel";
		}
		addRecipeByOreDictionary(ItemType.IR_FE.item(), "SSS", "SGS", "SSS",
				'S', iron, 'G', IC2Items.getItem("iridiumPlate"));
		addRecipeByOreDictionary(ItemType.MK0.item(), "SAS", "AGA", "SAS",
				'S', IC2Items.getItem("advancedAlloy"), 'A',
				ItemType.WaterUraniumPlateMK1.item(), 'G',
				IC2Items.getItem("transformerUpgrade"));
		addRecipeByOreDictionary(ItemType.MK1.item(), "SMS", "UGU", "SAS",
				'S', Items.redstone, 'U',
				ItemType.WaterUraniumPlateMK1.item(), 'A',
				industrialDiamond, 'M',
				ItemType.IR_FE.item(), 'G', ItemType.MK0.item());
		addRecipeByOreDictionary(ItemType.MK2.item(), "SMS", "UGU", "SAS",
				'S', IC2Items.getItem("advancedAlloy"), 'U',
				ItemType.WaterUraniumPlateMK2.item(), 'A',
				IC2Items.getItem("carbonPlate"), 'M', Blocks.lapis_block, 'G',
				ItemType.MK1.item());
		GameRegistry
				.addShapedRecipe(ItemType.MK3.item(), "SMS", "UGU", "SAS",
						'S', IC2Items.getItem("advancedAlloy"), 'U',
						ItemType.WaterUraniumPlateMK3.item(), 'A',
						IC2Items.getItem("coalChunk"), 'M',
						industrialDiamond, 'G',
						ItemType.MK2.item());

			addRecipeByOreDictionary(ItemType.MK4.item(), "SMS", "UGU",
					"SAS", 'S', IC2Items.getItem("advancedAlloy"), 'U',
					ItemType.WaterUraniumPlateMK4.item(), 'A',
					getUsualItemStack(IC2Items.getItem("advBattery")), 'M',
					Blocks.emerald_block, 'G',
					ItemType.MK3.item());
			addRecipeByOreDictionary(ItemType.MK5.item(), "SMS", "UGU",
					"SAS", 'S', IC2Items.getItem("advancedAlloy"), 'U',
					ItemType.WaterUraniumPlateMK5.item(), 'A',
					getUsualItemStack(IC2Items.getItem("energyCrystal")), 'M',
					"blockRuby", 'G',
					ItemType.MK4.item());
			addRecipeByOreDictionary(ItemType.MK6.item(), "SMS", "UGU",
					"SAS", 'S', IC2Items.getItem("advancedAlloy"), 'U',
					ItemType.WaterUraniumPlateMK6.item(), 'A',
					getUsualItemStack(IC2Items.getItem("lapotronCrystal")), 'M',
					"blockSapphire", 'G',
					ItemType.MK5.item());
			addRecipeByOreDictionary(ItemType.MK7.item(), "SMS", "UGU",
					"SAS", 'S', IC2Items.getItem("advancedAlloy"), 'U',
					ItemType.WaterUraniumPlateMK7.item(), 'A',
					getUsualItemStack(IC2Items.getItem("suBattery")), 'M',
					"blockChrome", 'G',
					ItemType.MK6.item());

		addRecipeByOreDictionary(ItemType.ReservoirCore.item(), "ASA", "SMS",
				"CSC", 'A', IC2Items.getItem("electronicCircuit"), 'S',
				IC2Items.getItem("advancedAlloy"), 'M', machine, 'C',
				ItemType.WaterUraniumPlateMK1.item());
		addRecipeByOreDictionary(ItemType.ReservoirCoreAdvanced.item(), "IDI",
				"AMA", "IDI", 'I', IC2Items.getItem("iridiumPlate"), 'A',
				IC2Items.getItem("advancedCircuit"), 'M',
				ItemType.ReservoirCore.item(), 'D',
				industrialDiamond);

			addRecipeByOreDictionary(ItemType.WaterUraniumAlloyPlate.item(), "UIU",
					"IDI", "UIU", 'U', ItemType.WaterUraniumIngot.item(),
					'I', IC2Items.getItem("iridiumPlate"), 'D',
					industrialDiamond);

		/*if (GregTech_API.isGregTechLoaded()) {
			ItemStack is3 = UpdaterType.PlasmaUraniumIngot.item().copy();
			is3.stackSize = 4;
			addRecipeByOreDictionary(is3, "CUC", "UCU", "CUC", 'U',
					"ingotUranium", 'C',
					GregTech_API.getGregTechItem(2, 1, 131));
		}*/

		addRecipeByOreDictionary(ItemType.PlasmaUraniumAlloyPlate.item(),
				"OPO", "PUP", "OPO", 'O',
				"plateOsmium"/*GregTech_API.getGregTechMaterial(84, 1)*/, 'P',
				ItemType.PlasmaUraniumIngot.item(), 'U',
				ItemType.WaterUraniumAlloyPlate.item());

		addRecipeByOreDictionary(ItemType.DiamondBlade.item(),
				"ITI", "TDT", "ITI",
				'I', "plateIron",
				'D', Items.diamond,
				'T', "dustDiamond");
		addRecipeByOreDictionary(ItemType.DiamondGlazingWheel.item(),
				"SSS", "SAS", "SSS", 'S', Items.flint, 'A', ItemType.DiamondBlade.item());
		addRecipeByOreDictionary(ItemType.IndustrialSteelHydraulicCylinder.item(),
				"PCP", "CBC", " I ",
				'P', "plateIndustrialSteel",
				'C', ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK4),
				'B', "blockIndustrialSteel",
				'I', "ingotIndustrialSteel");
		addRecipeByOreDictionary(ItemType.BrassCentrifugePot.item(),
				"P P", "P P", "PDP",
				'P', "plateZincAlloy",
				'D', "plateDenseZincAlloy");
		addRecipeByOreDictionary(ItemType.BrassCentrifugePot.item(),
				"P P", "P P", "PDP",
				'P', "plateZincAlloy",
				'D', "plateDenseZincAlloy");
		addRecipeByOreDictionary(ItemType.VSteelPistonCylinder.item(),
				"PCP", "DBD", "PPP",
				'P', "plateVanadiumSteel",
				'C', GlobalBlocks.compressor,
				'D', "plateDenseVanadiumSteel",
				'B', "blockVanadiumSteel");
		addRecipeByOreDictionary(ItemType.VSteelWaterPipe.item(),
				"DDD", "D D", "DDD", 'D', "plateDenseVanadiumSteel");
		addRecipeByOreDictionary(ItemType.RubyWaterHole.item(),
				"IBI", "B B", "IBI",
				'B', "blockRuby",
				'I', "plateIndustrialSteel");
		OreDictionary.registerOre("dustCactus", ItemType.DustCactus.item());
		Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.cactus, 1, 16)), null,
				ItemType.DustCactus.item());
		if(gregtechRecipe) {
			GregTech_API.sRecipeAdder.addAssemblerRecipe(ItemType.DustCactus.item(4),
					getUsualItemStack(new ItemStack(Blocks.tallgrass)), ItemType.DrawingWaterPart.item(4), 20*20, 120);
			GregTech_API.sRecipeAdder.addAssemblerRecipe(ItemType.DrawingWaterPart.item(4),
					ItemMaterial.get(MaterialTypes.VanadiumSteel, MaterialForms.ring),
					ItemType.DrawingWaterComponent.item(), 20*20, 120);
		} else {
			addShapelessRecipeByOreDictionary(ItemType.DrawingWaterPart.item(),
					ItemType.DustCactus.item(), ItemType.DustCactus.item(),
					ItemType.DustCactus.item(), ItemType.DustCactus.item(),
					new ItemStack(Blocks.tallgrass, 1, OreDictionary.WILDCARD_VALUE));
			addShapelessRecipeByOreDictionary(ItemType.DrawingWaterComponent.item(),
					ItemMaterial.get(MaterialTypes.VanadiumSteel, MaterialForms.ring),
					ItemType.DrawingWaterPart.item(),
					ItemType.DrawingWaterPart.item(),
					ItemType.DrawingWaterPart.item(),
					ItemType.DrawingWaterPart.item());
		}
		
		OreDictionary.registerOre("dustIron", ItemType.DustIron.item());
		OreDictionary.registerOre("dustGold", ItemType.DustGold.item());
		OreDictionary.registerOre("dustDiamond", ItemType.DustDiamond.item());
		// If IC2 isn't loaded, I'll add crafting recipes for diamond dust.
		if(!CompactWatermills.isIndustrialCraftLoaded) {
			Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(Items.diamond)), null, ItemType.DustDiamond.item());
			Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.iron_ore)), null, ItemType.DustIron.item(2));
			Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.gold_ore)), null, ItemType.DustGold.item(2));
			Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(Items.iron_ingot)), null, ItemType.DustIron.item());
			Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(Items.gold_ingot)), null, ItemType.DustGold.item());
			FurnaceRecipes.smelting().func_151394_a(ItemType.DustIron.item(), new ItemStack(Items.iron_ingot), 0f);
			FurnaceRecipes.smelting().func_151394_a(ItemType.DustGold.item(), new ItemStack(Items.gold_ingot), 0f);
		}
		
		OreDictionary.registerOre("plateDenseRedstone", ItemType.DenseRedstonePlate.item());
		Recipes.compressor.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.redstone_block)), null, ItemType.DenseRedstonePlate.item());
	}

	@Override
	public void registerTurbine() {
		addRecipeByOreDictionary(ItemType.BaseRotor.item(), "ILI", "LCL", "ILI",
				'I', IC2Items.getItem("denseplateiron"), 'L',
				IC2Items.getItem("denseplatelead"), 'C',
				IC2Items.getItem("carbonPlate"));

		// Turbine recipe registering
		/*if (gregtechRecipe) {
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 0),
					"SAU", "CGA", "SAU", 'S', Blocks.iron_bars, 'A',
					new ItemStack(GlobalBlocks.waterMill, 1, 4), 'G',
					UpdaterType.ReservoirCore.item(), 'U', UpdaterType.MK1.item(),
					'C', GregTech_API.getGregTechComponent(4, 1));
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 1),
					"IUI", "TCT", "IUI", 'T', new ItemStack(
							GlobalBlocks.turbine, 1, 0), 'I',
					"plateInvar", 'U',
					IC2Items.getItem("advancedMachine"), 'C',
					IC2Items.getItem("advancedCircuit"));
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 2),
					"IUI", "TCT", "IRI", 'T', new ItemStack(
							GlobalBlocks.turbine, 1, 1), 'I',
					"plateAluminium", 'U',
					GregTech_API.getGregTechComponent(9, 1), 'C',
					IC2Items.getItem("advancedMachine"), 'R',
					GregTech_API.getGregTechComponent(27, 1));
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 3),
					"SSS", "TRT", "WWW", 'T', new ItemStack(
							GlobalBlocks.turbine, 1, 2), 'S',
					"gearStainlessSteel", 'W',
					GregTech_API.getGregTechComponent(9, 1), 'R',
					GregTech_API.getGregTechComponent(27, 1));
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 4),
					"IUI", "TCT", "IUI", 'T', new ItemStack(
							GlobalBlocks.turbine, 1, 3), 'I',
					"plateTitanium", 'U',
					IC2Items.getItem("teleporter"), 'C',
					GregTech_API.getGregTechComponent(27, 1));
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 5),
					"IUI", "TCT", "IUI", 'T', new ItemStack(
							GlobalBlocks.turbine, 1, 4), 'I',
					"plateChrome", 'U',
					"gearIridium", 'C',
					GregTech_API.getGregTechBlock(1, 1, 109));
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 6),
					"IUI", "TCT", "IRI", 'T', new ItemStack(
							GlobalBlocks.turbine, 1, 5), 'I',
					"plateTungstenSteel", 'U',
					GregTech_API.getGregTechBlock(1, 1, 109), 'C',
					UpdaterType.WaterUraniumIngot.item(), 'R',
					GregTech_API.getGregTechComponent(0, 1));
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 7),
					"IUI", "TCT", "IUI", 'T', new ItemStack(
							GlobalBlocks.turbine, 1, 6), 'I',
					"plateIridium", 'U',
					GregTech_API.getGregTechComponent(9, 1), 'C',
					"gearIridium");
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 8),
					"IUI", "TCT", "IRI", 'T', new ItemStack(
							GlobalBlocks.turbine, 1, 7), 'I',
					IC2Items.getItem("iridiumPlate"), 'U',
					"gearIridium", 'C',
					GregTech_API.getGregTechBlock(1, 1, 4), 'R',
					GregTech_API.getGregTechComponent(2, 1));
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 9),
					"IUI", "TCT", "IUI", 'T', new ItemStack(
							GlobalBlocks.turbine, 1, 8), 'I',
					"plateOsmium", 'U',
					"gearIridium", 'C',
					GregTech_API.getGregTechBlock(1, 1, 47));
			addRecipeByOreDictionary(
					new ItemStack(GlobalBlocks.turbine, 1, 10), "WCW", "TUT",
					"IGI", 'T', new ItemStack(GlobalBlocks.turbine, 1, 9), 'W',
					UpdaterType.WaterUraniumAlloyPlate.item(), 'I',
					GregTech_API.getGregTechComponent(0, 1), 'U',
					GregTech_API.getGregTechBlock(0, 1, 10), 'G',
					GregTech_API.getGregTechBlock(1, 1, 90), 'C',
					"gearOsmium");
			addRecipeByOreDictionary(
					new ItemStack(GlobalBlocks.turbine, 1, 11), "IUI", "TCT",
					"IUI", 'T', new ItemStack(GlobalBlocks.turbine, 1, 10),
					'I', UpdaterType.PlasmaUraniumAlloyPlate.item(), 'U',
					GregTech_API.getGregTechBlock(1, 1, 90), 'C',
					Items.nether_star);
		} else {*/
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 0),
					"SAU", "CGA", "SAU", 'S', Blocks.iron_bars, 'A',
					new ItemStack(GlobalBlocks.waterMill, 1, 4), 'G',
					ItemType.ReservoirCore.item(), 'U', ItemType.MK1.item(),
					'C', Blocks.glass_pane);
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 1),
					"IUI", "TCT", "IRI", 'T', new ItemStack(
							GlobalBlocks.turbine, 1, 0), 'I',
					IC2Items.getItem("denseplateiron"), 'U',
					IC2Items.getItem("transformerUpgrade"), 'C',
					IC2Items.getItem("advancedCircuit"), 'R',
					ItemType.BaseRotor.item());
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 2),
					"IUI", "TCT", "IRI", 'T', new ItemStack(
							GlobalBlocks.turbine, 1, 1), 'I',
					IC2Items.getItem("carbonPlate"), 'U',
					IC2Items.getItem("advancedCircuit"), 'C',
					IC2Items.getItem("advancedMachine"), 'R',
					ItemType.BaseRotor.item());
			addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 3),
					"IRI", "TCT", "IRI", 'T', new ItemStack(
							GlobalBlocks.turbine, 1, 2), 'I',
					IC2Items.getItem("reactorPlating"), 'C',
					IC2Items.getItem("teleporter"), 'R', ItemType.BaseRotor.item());
		//}

	}

	@Override
	public void registerMachines() {
		addRecipeByOreDictionary(GlobalBlocks.sawmill,
				" H ", "CPC", "KKK",
				'H', ItemType.WoodenHammer.item(),
				'C', ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK1),
				'K', Blocks.stonebrick,
				'P', ItemType.DiamondBlade.item());
		addRecipeByOreDictionary(GlobalBlocks.macerator,
				" H ", "CPC", "KRK",
				'H', ItemType.WoodenHammer.item(),
				'C', ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK1),
				'K', Blocks.stonebrick,
				'P', ItemType.DiamondGlazingWheel.item(),
				'R', Blocks.hopper);
		addRecipeByOreDictionary(GlobalBlocks.compressor,
				"CDC", "DGD", "ICA",
				'C', ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK4),
				'D', "plateDenseIndustrialSteel",
				'G', ItemType.IndustrialSteelHydraulicCylinder.item(),
				'I', ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK1),
				'A', ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK3));
		addRecipeByOreDictionary(GlobalBlocks.centrifuge,
				"CBC", "BRB", "XCX",
				'C', ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK4),
				'B', ItemType.BrassCentrifugePot.item(),
				'R', ItemCrafting.get(CraftingTypes.rotationAxle, LevelTypes.MK4),
				'X', ItemType.DataBall.item());
		addRecipeByOreDictionary(GlobalBlocks.cutter,
				"CPC", "LML", "SSS",
				'C', ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK4),
				'P', ItemType.VSteelWaterPipe.item(),
				'L', "plateIndustrialSteel",
				'M', ItemType.RubyWaterHole.item(),
				'S', "plateManganeseSteel");
		addRecipeByOreDictionary(GlobalBlocks.cutter, "A", 'A', GlobalBlocks.lathe);
		addRecipeByOreDictionary(GlobalBlocks.lathe, "A", 'A', GlobalBlocks.cutter);
		addRecipeByOreDictionary(GlobalBlocks.advancedCompressor,
				"CDC", "CPC", "CIC",
				'C', ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK5),
				'D', "plateDenseVanadiumSteel",
				'P', ItemType.VSteelPistonCylinder.item(),
				'I', ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK4));
	}

	@Override
	public void registerPlugins() {
		addShapelessRecipeByOreDictionary(PluginType.StorageMK4.item(),
				PluginType.StorageMK3.item(), PluginType.StorageMK3.item(), PluginType.StorageMK3.item(), PluginType.StorageMK3.item());
		addShapelessRecipeByOreDictionary(PluginType.StorageMK3.item(),
				PluginType.StorageMK2.item(), PluginType.StorageMK2.item(), PluginType.StorageMK2.item(), PluginType.StorageMK2.item());
		addShapelessRecipeByOreDictionary(PluginType.StorageMK2.item(),
				PluginType.StorageMK1.item(), PluginType.StorageMK1.item(), PluginType.StorageMK1.item(), PluginType.StorageMK1.item());
		addShapelessRecipeByOreDictionary(PluginType.AllRoundMK1.item(),
				PluginType.StorageMK1.item(), PluginType.RainMK1.item(), PluginType.OverMK1.item(), PluginType.UnderMK1.item());
		addShapelessRecipeByOreDictionary(PluginType.StorageMK1.item(),
				new ItemStack(GlobalBlocks.reservoir, 1, 8),
				Items.iron_ingot, Items.iron_ingot, Items.iron_ingot);
		addShapelessRecipeByOreDictionary(PluginType.StorageMK2.item(),
				new ItemStack(GlobalBlocks.reservoir, 1, 17),
				PluginType.StorageMK1.item(),
				Items.iron_ingot, Items.iron_ingot, Items.iron_ingot);
		addShapelessRecipeByOreDictionary(PluginType.SpeedMK1.item(),
				ItemType.DataBall.item(),
				Items.iron_ingot, Items.iron_ingot, Items.iron_ingot);
		addRecipeByOreDictionary(PluginType.OverMK1.item(),
				"CBC", "EPE", "CBC",
				'C', ItemType.DrawingWaterComponent.item(),
				'B', IC2Items.getItem("pump"),
				'E', IC2Items.getItem("extractor"),
				'P', ItemType.VSteelWaterPipe.item());
		addShapelessRecipeByOreDictionary(PluginType.UnderMK1.item(), PluginType.OverMK1.item());
		addShapelessRecipeByOreDictionary(PluginType.OverMK1.item(), PluginType.UnderMK1.item());
		addRecipeByOreDictionary(PluginType.RainMK1.item(),
				"C C", "EPE", "C C",
				'C', ItemType.DrawingWaterComponent.item(),
				'E', IC2Items.getItem("extractor"),
				'P', ItemType.VSteelWaterPipe.item());
	}

	
}
