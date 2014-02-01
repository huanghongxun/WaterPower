package org.jackhuang.compactwatermills;

import gregtechmod.api.GregTech_API;
import ic2.api.item.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import org.jackhuang.compactwatermills.block.GlobalBlocks;
import org.jackhuang.compactwatermills.block.reservoir.BlockReservoir;
import org.jackhuang.compactwatermills.block.reservoir.ReservoirType;
import org.jackhuang.compactwatermills.block.turbines.BlockTurbine;
import org.jackhuang.compactwatermills.block.turbines.TileEntityTurbine;
import org.jackhuang.compactwatermills.block.watermills.BlockCompactWatermill;
import org.jackhuang.compactwatermills.block.watermills.WaterType;
import org.jackhuang.compactwatermills.item.GlobalItems;
import org.jackhuang.compactwatermills.item.rotors.RotorType;
import org.jackhuang.compactwatermills.item.updates.ItemWatermillUpdater;

import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeHandler {
	
	private boolean gregtechRecipe = true;
	
	private Configuration config;
	public RecipeHandler(Configuration c) {
		config = c;
		
		Property p = c.get("enable", "EnableGregTechRecipe", true);
		gregtechRecipe = p.getBoolean(true) && GregTech_API.isGregTechLoaded();
	}
	
	public void registerAllRecipes() {
		GlobalBlocks.waterMill = new BlockCompactWatermill(config,
				InternalName.blockCompactWatermill);

		for (WaterType type : WaterType.values()) {
			GameRegistry.registerTileEntity(type.claSS, type.tileEntityName());
		}
		Property p = config.get("enable", "EnableUpdaters", true);
		if(p.getBoolean(true))
			registerUpdater();
		
		registerWatermills();
		registerRotor();
		
		p = config.get("enable", "EnableReservoir", true);
		if(p.getBoolean(true))
			registerReservoir();
	}

	void addRotorRecipe(RotorType output, Item S, Block I) {
		if(output.enable)
		GameRegistry.addShapedRecipe(new ItemStack(output.getItem()), "S S", " I ",
				"S S", 'S', new ItemStack(S), 'I', new ItemStack(I));
	}

	void addRotorRecipe(RotorType output, Block S, Block I) {
		if(output.enable)
		GameRegistry.addShapedRecipe(new ItemStack(output.getItem()), "S S", " I ",
				"S S", 'S', new ItemStack(S), 'I', new ItemStack(I));
	}

	void addRotorRecipe(RotorType output, ItemStack S, ItemStack I) {
		if(output.enable)
		GameRegistry.addShapedRecipe(new ItemStack(output.getItem()), "S S", " I ",
				"S S", 'S', S, 'I', I);
	}

	void addReservoirRecipe(ItemStack output, Block S) {
		GameRegistry.addShapedRecipe(output, "SSS", "SIS", "SSS", 'S',
				new ItemStack(S), 'I', GlobalItems.ReservoirCore);
	}

	void addReservoirRecipe(ItemStack output, Item S) {
		GameRegistry.addShapedRecipe(output, "SSS", "SIS", "SSS", 'S',
				new ItemStack(S), 'I', GlobalItems.ReservoirCore);
	}

	void addReservoirRecipe(ItemStack output, ItemStack S) {
		GameRegistry.addShapedRecipe(output, "SSS", "SIS", "SSS", 'S', S, 'I',
				GlobalItems.ReservoirCore);
	}

	void addReservoirAdvancedRecipe(ItemStack output, Block S) {
		GameRegistry.addShapedRecipe(output, "SSS", "SIS", "SSS", 'S',
				new ItemStack(S), 'I', GlobalItems.ReservoirCoreAdvanced);
	}

	void addReservoirAdvancedRecipe(ItemStack output, Item S) {
		GameRegistry.addShapedRecipe(output, "SSS", "SIS", "SSS", 'S',
				new ItemStack(S), 'I', GlobalItems.ReservoirCoreAdvanced);
	}

	void addReservoirAdvancedRecipe(ItemStack output, ItemStack S) {
		GameRegistry.addShapedRecipe(output, "SSS", "SIS", "SSS", 'S', S, 'I',
				GlobalItems.ReservoirCoreAdvanced);
	}
	
	public void registerWatermills() {
		// Water mills recipes register
		GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.waterMill, 1, 0), " W ", //1EU/t
				"WTW", " W ", 'W', Items.getItem("waterMill"), 'T',
				Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.waterMill, 1, 1), "WWW", //8EU/t
				"WTW", "WWW", 'W', new ItemStack(GlobalBlocks.waterMill, 1, 0), 'T',
				Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.waterMill, 1, 1), "CCC", "SAS", "PMP", 
				'C', Items.getItem("FluidCell"),
				'A', new ItemStack(GlobalBlocks.waterMill, 1, 0),
				'S', Items.getItem("plateiron"),
				'P', Items.getItem("advancedCircuit"),
				'M', GlobalItems.UpdaterMK0);
		GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.waterMill, 1, 2), " W ", //32EU/t
				"WTW", " W ", 'W', new ItemStack(GlobalBlocks.waterMill, 1, 1), 'T',
				Items.getItem("transformerUpgrade"));
		
		GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.waterMill, 1, 3), " W ", //128EU/t
				"WTW", " W ", 'W', new ItemStack(GlobalBlocks.waterMill, 1, 2), 'T',
				Items.getItem("transformerUpgrade"));

		GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.waterMill, 1, 3), "CCC", "SAS", "PMP", 
				'C', Items.getItem("FluidCell"),
				'A', new ItemStack(GlobalBlocks.waterMill, 1, 1),
				'S', Items.getItem("carbonPlate"),
				'P', Items.getItem("advancedCircuit"),
				'M', GlobalItems.UpdaterMK1);
		
		GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.waterMill, 1, 4), " W ", //512EU/t
				"WTW", " W ", 'W', new ItemStack(GlobalBlocks.waterMill, 1, 3), 'T',
				Items.getItem("transformerUpgrade"));

		GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.waterMill, 1, 4), "CCC", "SAS", "PMP", 
				'C', Items.getItem("FluidCell"),
				'A', new ItemStack(GlobalBlocks.waterMill, 1, 3),
				'S', Items.getItem("advancedAlloy"),
				'P', Items.getItem("advancedCircuit"),
				'M', GlobalItems.UpdaterMK2);
		
		GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.waterMill, 1, 5), " W ", //2048EU/t
				"WTW", " W ", 'W', new ItemStack(GlobalBlocks.waterMill, 1, 4), 'T',
				Items.getItem("transformerUpgrade"));

		GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.waterMill, 1, 5), "CCC", "SAS", "PMP", 
				'C', Items.getItem("FluidCell"),
				'A', new ItemStack(GlobalBlocks.waterMill, 1, 4),
				'S', Items.getItem("iridiumPlate"),
				'P', Items.getItem("advancedCircuit"),
				'M', GlobalItems.UpdaterMK3);
		
		//GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 6), " W ",
		//		"WTW", " W ", 'W', new ItemStack(waterMill, 1, 5), 'T',
		//		Items.getItem("transformerUpgrade"));
	}
	
	public void registerUpdater() {

		GlobalItems.updater = (ItemWatermillUpdater) new ItemWatermillUpdater(config, InternalName.itemUpdaters);

		ItemStack is = GlobalItems.UpdaterWaterUraniumIngot.copy();
		is.stackSize = 36;
		GameRegistry.addShapedRecipe(is,
				"SAS", "ASA", "SAS",
				'A', Items.getItem("uraniumBlock"),
				'S', Items.getItem("reactorCoolantSix"));
		is = GlobalItems.UpdaterWaterUraniumIngot.copy();
		is.stackSize = 4;
		if(gregtechRecipe)
			GameRegistry.addShapedRecipe(is,
					"SAS", "ASA", "SAS",
					'A', GregTech_API.getGregTechMaterial(43, 1),
					'S', Items.getItem("waterCell"));
		GameRegistry.addShapedRecipe(GlobalItems.UpdaterWaterUraniumPlateMK1,
				"SSS", "SAS", "SSS",
				'A', GlobalItems.UpdaterWaterUraniumIngot,
				'S', Items.getItem("carbonPlate"));
		GameRegistry.addShapedRecipe(GlobalItems.UpdaterWaterUraniumPlateMK2,
				"SSS", "SAS", "SSS",
				'A', GlobalItems.UpdaterWaterUraniumPlateMK1,
				'S', Items.getItem("advancedAlloy"));
		GameRegistry.addShapedRecipe(GlobalItems.UpdaterWaterUraniumPlateMK3,
				"SSS", "SAS", "SSS",
				'A', GlobalItems.UpdaterWaterUraniumPlateMK2,
				'S', Items.getItem("iridiumPlate"));
		ItemStack iron = Items.getItem("plateiron");
		ItemStack machine = Items.getItem("machine");
		if(gregtechRecipe) {
			iron = GregTech_API.getGregTechMaterial(66, 1);
			machine = GregTech_API.getGregTechComponent(22, 1);
		}
		GameRegistry.addShapedRecipe(GlobalItems.UpdaterIR_FE, "SSS", "SGS", "SSS",
				'S', iron,
				'G', Items.getItem("iridiumPlate"));
		GameRegistry.addShapedRecipe(GlobalItems.UpdaterMK0, "SAS", "AGA", "SAS",
				'S', Items.getItem("advancedAlloy"),
				'A', GlobalItems.UpdaterWaterUraniumPlateMK1,
				'G', Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(GlobalItems.UpdaterMK1, "SMS", "UGU", "SAS",
				'S', Item.redstone,
				'U', GlobalItems.UpdaterWaterUraniumPlateMK1,
				'A', Items.getItem("industrialDiamond"),
				'M', GlobalItems.UpdaterIR_FE,
				'G', GlobalItems.UpdaterMK0);
		GameRegistry.addShapedRecipe(GlobalItems.UpdaterMK2, "SMS", "UGU", "SAS",
				'S', Items.getItem("advancedAlloy"),
				'U', GlobalItems.UpdaterWaterUraniumPlateMK2,
				'A', Items.getItem("carbonPlate"),
				'M', Block.blockLapis,
				'G', GlobalItems.UpdaterMK1);
		GameRegistry.addShapedRecipe(GlobalItems.UpdaterMK3, "SMS", "UGU", "SAS",
				'S', Items.getItem("advancedAlloy"),
				'U', GlobalItems.UpdaterWaterUraniumPlateMK3,
				'A', Items.getItem("coalChunk"),
				'M', Items.getItem("industrialDiamond"),
				'G', GlobalItems.UpdaterMK2);

		GameRegistry.addShapedRecipe(GlobalItems.ReservoirCore, "ASA", "SMS", "CSC",
				'A', Items.getItem("electronicCircuit"),
				'S', Items.getItem("advancedAlloy"),
				'M', machine,
				'C', GlobalItems.UpdaterWaterUraniumPlateMK1);
		GameRegistry.addShapedRecipe(GlobalItems.ReservoirCoreAdvanced, "IDI", "AMA", "IDI",
				'I', Items.getItem("iridiumPlate"),
				'A', Items.getItem("advancedCircuit"),
				'M', GlobalItems.ReservoirCore,
				'D', Items.getItem("industrialDiamond"));
	}
	
	public void registerRotor() {

		// Rotors recipes register
		addRotorRecipe(RotorType.WOOD, Item.stick, Block.wood);
		addRotorRecipe(RotorType.STONE, Block.cobblestone,
				Block.stone);
		addRotorRecipe(RotorType.LEAD, Items.getItem("platelead"),
				Items.getItem("denseplatelead"));
		addRotorRecipe(RotorType.TIN, Items.getItem("platetin"),
				Items.getItem("denseplatetin"));
		addRotorRecipe(RotorType.GOLD, Items.getItem("plategold"),
				Items.getItem("denseplategold"));
		addRotorRecipe(RotorType.COPPER,
				Items.getItem("platecopper"), Items.getItem("denseplatecopper"));
		addRotorRecipe(RotorType.IRON, Items.getItem("plateiron"),
				Items.getItem("denseplateiron"));
		addRotorRecipe(RotorType.REFINEDIRON,
				Items.getItem("refinedIronIngot"), Items.getItem("machine"));
		addRotorRecipe(RotorType.OBSIDIAN,
				Items.getItem("plateobsidian"),
				Items.getItem("denseplateobsidian"));
		addRotorRecipe(RotorType.BRONZE,
				Items.getItem("platebronze"), Items.getItem("denseplatebronze"));
		addRotorRecipe(RotorType.LAPIS, Items.getItem("platelapi"),
				Items.getItem("denseplatelapi"));
		addRotorRecipe(RotorType.QUARTZ, Item.netherQuartz,
				Block.blockNetherQuartz);
		addRotorRecipe(RotorType.CARBON,
				Items.getItem("carbonPlate"), Items.getItem("coalChunk"));
		addRotorRecipe(RotorType.ADVANCED,
				Items.getItem("advancedAlloy"),
				Items.getItem("reinforcedStone"));
		addRotorRecipe(RotorType.EMERALD, Item.emerald,
				Block.blockEmerald);
		addRotorRecipe(RotorType.DIAMOND, Item.diamond,
				Block.blockDiamond);
		addRotorRecipe(RotorType.IRIDIUM,
				Items.getItem("iridiumOre"), Items.getItem("iridiumPlate"));
		addRotorRecipe(RotorType.IRIDIUMIRON,
				Items.getItem("iridiumPlate"), GlobalItems.UpdaterIR_FE);
		
		if(GregTech_API.isGregTechLoaded()) {
			addRotorRecipe(RotorType.SILVER, GregTech_API.getGregTechMaterial(69, 1), GregTech_API.getGregTechBlock(0, 1, 3));
			addRotorRecipe(RotorType.ZINC, GregTech_API.getGregTechMaterial(82, 1), GregTech_API.getGregTechBlock(4, 1, 2));
			addRotorRecipe(RotorType.BRASS, GregTech_API.getGregTechMaterial(81, 1), GregTech_API.getGregTechBlock(0, 1, 12));
			addRotorRecipe(RotorType.ALUMINUM, GregTech_API.getGregTechMaterial(75, 1), GregTech_API.getGregTechBlock(0, 1, 7));
			addRotorRecipe(RotorType.STEEL, GregTech_API.getGregTechMaterial(78, 1), GregTech_API.getGregTechBlock(0, 1, 11));
			addRotorRecipe(RotorType.INVAR, GregTech_API.getGregTechMaterial(73, 1), GregTech_API.getGregTechBlock(4, 1, 10));
			addRotorRecipe(RotorType.ELECTRUM, GregTech_API.getGregTechMaterial(71, 1), GregTech_API.getGregTechBlock(4, 1, 1));
			addRotorRecipe(RotorType.NICKEL, GregTech_API.getGregTechMaterial(72, 1), GregTech_API.getGregTechBlock(4, 1, 7));
			addRotorRecipe(RotorType.OSMIUM, GregTech_API.getGregTechMaterial(84, 1), GregTech_API.getGregTechBlock(4, 1, 11));
			addRotorRecipe(RotorType.TITANIUM, GregTech_API.getGregTechMaterial(77, 1), GregTech_API.getGregTechBlock(0, 1, 8));
			addRotorRecipe(RotorType.PLATINUM, GregTech_API.getGregTechMaterial(79, 1), GregTech_API.getGregTechBlock(4, 1, 5));
			addRotorRecipe(RotorType.TUNGSTEN, GregTech_API.getGregTechMaterial(80, 1), GregTech_API.getGregTechBlock(4, 1, 6));
			addRotorRecipe(RotorType.CHROME, GregTech_API.getGregTechMaterial(76, 1), GregTech_API.getGregTechBlock(0, 1, 9));
			addRotorRecipe(RotorType.TUNGSTEN_STEEL, GregTech_API.getGregTechMaterial(83, 1), GregTech_API.getGregTechBlock(4, 1, 8));
		}
	}
	
	public void registerReservoir() {
		
		// Blocks registering		
		GlobalBlocks.turbine = new BlockTurbine(config, InternalName.blockTurbine);
		GlobalBlocks.reservoir = new BlockReservoir(config, InternalName.blockReservoir);
		
		// TileEntities registering
		for (ReservoirType type : ReservoirType.values()) {
			GameRegistry.registerTileEntity(type.claSS, type.tileEntityName());
		}

		GameRegistry.registerTileEntity(TileEntityTurbine.class, "turbine");
		
		
		// Turbine recipe registering
		if(gregtechRecipe) {
			GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.turbine, 1, 0), "SAU", "CGA", "SAU",
					'S', Block.fenceIron, 
					'A', new ItemStack(GlobalBlocks.waterMill, 1, 4),
					'G', GlobalItems.ReservoirCore,
					'U', GlobalItems.UpdaterMK1,
					'C', GregTech_API.getGregTechComponent(4, 1));
			GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.turbine, 1, 1), "SAU", "CGA", "SAU",
					'S', Block.fenceIron, 
					'A', new ItemStack(GlobalBlocks.waterMill, 1, 5),
					'G', GlobalItems.ReservoirCoreAdvanced,
					'U', GlobalItems.UpdaterMK2,
					'C', GregTech_API.getGregTechComponent(4, 1));
			GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.turbine, 1, 2), "SAU", "CGA", "SAU",
					'S', Block.fenceIron, 
					'A', new ItemStack(GlobalBlocks.waterMill, 1, 6),
					'G', GlobalItems.ReservoirCoreAdvanced,
					'U', GlobalItems.UpdaterMK3,
					'C', GregTech_API.getGregTechComponent(4, 1));
		}
		else {
			GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.turbine, 1, 0), "SAU", "CGA", "SAU",
					'S', Block.fenceIron,
					'A', new ItemStack(GlobalBlocks.waterMill, 1, 4),
					'G', GlobalItems.ReservoirCore,
					'U', GlobalItems.UpdaterMK1,
					'C', Block.thinGlass);
			GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.turbine, 1, 1), "SAU", "CGA", "SAU",
					'S', Block.fenceIron,
					'A', new ItemStack(GlobalBlocks.waterMill, 1, 5),
					'G', GlobalItems.ReservoirCoreAdvanced,
					'U', GlobalItems.UpdaterMK2,
					'C', Block.thinGlass);
			GameRegistry.addShapedRecipe(new ItemStack(GlobalBlocks.turbine, 1, 2), "SAU", "CGA", "SAU",
					'S', Block.fenceIron,
					'A', new ItemStack(GlobalBlocks.waterMill, 1, 5),
					'G', GlobalItems.ReservoirCoreAdvanced,
					'U', GlobalItems.UpdaterMK3,
					'C', Block.thinGlass);
		}
		
		// Reservoir recipes registering
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 0), Block.wood);
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 1), Block.stone);
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 2), Block.blockLapis);
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 3), Items.getItem("tinBlock"));
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 4), Items.getItem("copperBlock"));
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 5), Items.getItem("leadBlock"));
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 6), Block.blockNetherQuartz);
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 7), Items.getItem("bronzeBlock"));
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 8), Block.blockIron);
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 9), Block.netherBrick);
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 10), Block.obsidian);
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 12), Items.getItem("machine"));
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 13), Block.blockGold);
		addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 14), Items.getItem("carbonPlate"));
		addReservoirAdvancedRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 15), Items.getItem("advancedAlloy"));
		addReservoirAdvancedRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 16), Block.blockEmerald);
		addReservoirAdvancedRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 17), Block.blockDiamond);
		addReservoirAdvancedRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 18), Items.getItem("iridiumOre"));
		addReservoirAdvancedRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 19), Items.getItem("iridiumPlate"));
		if(GregTech_API.isGregTechLoaded()) {
			/* Silver */   addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 11), GregTech_API.getGregTechBlock(0, 1, 3));
			/* Zinc */     addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 20), GregTech_API.getGregTechBlock(4, 1, 2));
			/* Brass */    addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 21), GregTech_API.getGregTechBlock(0, 1, 12));
			/* Aluminum */ addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 22), GregTech_API.getGregTechBlock(0, 1, 7));
			/* Steel */    addReservoirAdvancedRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 23), GregTech_API.getGregTechBlock(0, 1, 11));
			/* Invar */    addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 24), GregTech_API.getGregTechBlock(4, 1, 10));
			/* Electrum */ addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 25), GregTech_API.getGregTechBlock(4, 1, 1));
			/* Nickel */   addReservoirRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 26), GregTech_API.getGregTechBlock(4, 1, 7));
			/* Osmium */   addReservoirAdvancedRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 27), GregTech_API.getGregTechBlock(4, 1, 11));
			/* Titanium */ addReservoirAdvancedRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 28), GregTech_API.getGregTechBlock(0, 1, 8));
			/* Platinum */ addReservoirAdvancedRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 29), GregTech_API.getGregTechBlock(4, 1, 5));
			/* Tungsten */ addReservoirAdvancedRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 30), GregTech_API.getGregTechBlock(4, 1, 6));
			/* Chrome */   addReservoirAdvancedRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 31), GregTech_API.getGregTechBlock(0, 1, 9));
			/* Tungsten Steel */ addReservoirAdvancedRecipe(new ItemStack(GlobalBlocks.reservoir, 8, 32), GregTech_API.getGregTechBlock(4, 1, 8));
		}
	}
}
