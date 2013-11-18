package org.jackhuang.compactwatermills;

import ic2.api.item.Items;

import java.util.Properties;
import java.util.logging.Level;

import org.jackhuang.compactwatermills.block.BlockBase;
import org.jackhuang.compactwatermills.block.turbines.BlockReservoir;
import org.jackhuang.compactwatermills.block.turbines.BlockTurbine;
import org.jackhuang.compactwatermills.block.turbines.TileEntityReservoir;
import org.jackhuang.compactwatermills.block.turbines.TileEntityTurbine;
import org.jackhuang.compactwatermills.block.watermills.BlockCompactWatermill;
import org.jackhuang.compactwatermills.block.watermills.WaterType;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.rotors.ItemRotor;
import org.jackhuang.compactwatermills.rotors.RotorType;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = Reference.ModID, name = Reference.ModName, version = Reference.Version, dependencies = "required-after:IC2")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
/**
 * 
 * @author jackhuang1998
 *
 */
public class CompactWatermills {

	@Mod.Instance(Reference.ModID)
	public static CompactWatermills instance;

	@SidedProxy(clientSide = "org.jackhuang.compactwatermills.ClientProxy", serverSide = "org.jackhuang.compactwatermills.CommonProxy")
	public static CommonProxy proxy;

	public static BlockBase waterMill, turbine, reservoir;
	public static ItemRotor waterRotor;

	public static final CreativeTabs creativeTabCompactWatermills = new CreativeTabCompactWatermills(
			"creativeTabCompactWatermills");

	public static final int updateTick = 20;

	public static Properties runtimeIdProperties;

	public static boolean debugMode;

	private Configuration config;

	static {
		runtimeIdProperties = new Properties();
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LogHelper.init();
		if (Reference.Version == "@VERSION@") {
			debugMode = true;
			LogHelper.log(Level.INFO, "Turning on debug mode.");
		}
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		/*
		 * Property block = config.getBlock(Reference.configMillName,
		 * Reference.defaultMillID); block.comment =
		 * "This is the id of the Compact Watermill Blocks."; blockID =
		 * block.getInt(Reference.defaultMillID);
		 */

		/*
		 * Property blockTurbine = config.getBlock(Reference.configTurbineName,
		 * Reference.defaultTurbineID); blockTurbine.comment =
		 * "This is the id of the Water Turbine Block."; turbineID =
		 * blockTurbine.getInt(Reference.defaultTurbineID);
		 */

		Property rotor = config.getItem(Reference.configRotorName,
				Reference.defaultRotorID);
		rotor.comment = "This is the id of rotor Items.";

		for (RotorType typ : RotorType.values()) {
			typ.getConfig(config);
		}
		RotorType.initRotors();

		config.save();

	}

	void addRotorRecipe(Item output, Item S, Block I) {
		GameRegistry.addShapedRecipe(new ItemStack(output), "S S", " I ",
				"S S", 'S', new ItemStack(S), 'I', new ItemStack(I));
	}

	void addRotorRecipe(Item output, Block S, Block I) {
		GameRegistry.addShapedRecipe(new ItemStack(output), "S S", " I ",
				"S S", 'S', new ItemStack(S), 'I', new ItemStack(I));
	}

	void addRotorRecipe(Item output, ItemStack S, ItemStack I) {
		GameRegistry.addShapedRecipe(new ItemStack(output), "S S", " I ",
				"S S", 'S', S, 'I', I);
	}

	void addReservoirRecipe(ItemStack output, Block S) {
		GameRegistry.addShapedRecipe(output, "SSS", "SIS", "SSS", 'S',
				new ItemStack(S), 'I', Block.glass);
	}

	void addReservoirRecipe(ItemStack output, Item S) {
		GameRegistry.addShapedRecipe(output, "SSS", "SIS", "SSS", 'S',
				new ItemStack(S), 'I', Block.glass);
	}

	void addReservoirRecipe(ItemStack output, ItemStack S) {
		GameRegistry.addShapedRecipe(output, "SSS", "SIS", "SSS", 'S', S, 'I',
				Block.glass);
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {

		waterMill = new BlockCompactWatermill(config,
				InternalName.blockCompactWatermill);
		turbine = new BlockTurbine(config, InternalName.blockTurbine);
		reservoir = new BlockReservoir(config, InternalName.blockReservoir);

		for (WaterType type : WaterType.values()) {
			GameRegistry.registerTileEntity(type.claSS, type.tileEntityName());
		}

		GameRegistry.registerTileEntity(TileEntityReservoir.class, "reservoir");
		GameRegistry.registerTileEntity(TileEntityTurbine.class, "turbine");

		// Water mills recipes register
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 0), " W ",
				"WTW", " W ", 'W', Items.getItem("waterMill"), 'T',
				Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 1), "WWW",
				"WTW", "WWW", 'W', new ItemStack(waterMill, 1, 0), 'T',
				Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 2), " W ",
				"WTW", " W ", 'W', new ItemStack(waterMill, 1, 1), 'T',
				Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 3), " W ",
				"WTW", " W ", 'W', new ItemStack(waterMill, 1, 2), 'T',
				Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 4), " W ",
				"WTW", " W ", 'W', new ItemStack(waterMill, 1, 3), 'T',
				Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 5), " W ",
				"WTW", " W ", 'W', new ItemStack(waterMill, 1, 4), 'T',
				Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 6), " W ",
				"WTW", " W ", 'W', new ItemStack(waterMill, 1, 5), 'T',
				Items.getItem("transformerUpgrade"));

		// Reservoir recipes register
		addReservoirRecipe(new ItemStack(reservoir, 8, 0), Block.wood);
		addReservoirRecipe(new ItemStack(reservoir, 8, 1), Block.stone);
		addReservoirRecipe(new ItemStack(reservoir, 8, 2), Block.blockLapis);
		addReservoirRecipe(new ItemStack(reservoir, 8, 3),
				Items.getItem("tinBlock"));
		addReservoirRecipe(new ItemStack(reservoir, 8, 4),
				Items.getItem("copperBlock"));
		addReservoirRecipe(new ItemStack(reservoir, 8, 5),
				Items.getItem("leadBlock"));
		addReservoirRecipe(new ItemStack(reservoir, 8, 6),
				Block.blockNetherQuartz);
		addReservoirRecipe(new ItemStack(reservoir, 8, 7),
				Items.getItem("bronzeBlock"));
		addReservoirRecipe(new ItemStack(reservoir, 8, 8), Block.blockIron);
		addReservoirRecipe(new ItemStack(reservoir, 8, 9), Block.netherBrick);
		addReservoirRecipe(new ItemStack(reservoir, 8, 10), Block.obsidian);
		// addReservoirRecipe(new ItemStack(reservoir, 8, 11), Block.sliver);
		addReservoirRecipe(new ItemStack(reservoir, 8, 12),
				Items.getItem("machine"));
		addReservoirRecipe(new ItemStack(reservoir, 8, 13), Block.blockGold);
		addReservoirRecipe(new ItemStack(reservoir, 8, 14),
				Items.getItem("carbonPlate"));
		addReservoirRecipe(new ItemStack(reservoir, 8, 15),
				Items.getItem("advancedAlloy"));
		addReservoirRecipe(new ItemStack(reservoir, 8, 16), Block.blockEmerald);
		addReservoirRecipe(new ItemStack(reservoir, 8, 17), Block.blockDiamond);
		GameRegistry.addShapedRecipe(new ItemStack(reservoir, 8, 18), "SSS",
				"SIS", "SSS", 'S', Items.getItem("iridiumOre"), 'I',
				Items.getItem("iridiumPlate"));
		GameRegistry.addShapedRecipe(new ItemStack(reservoir, 8, 19), "SSS",
				"SIS", "SSS", 'S', Items.getItem("iridiumPlate"), 'I',
				Items.getItem("industrialDiamond"));

		// Rotors recipes register
		addRotorRecipe(RotorType.WOOD.getItem(), Item.stick, Block.wood);
		addRotorRecipe(RotorType.STONE.getItem(), Block.cobblestone,
				Block.stone);
		addRotorRecipe(RotorType.LEAD.getItem(), Items.getItem("platelead"),
				Items.getItem("denseplatelead"));
		addRotorRecipe(RotorType.TIN.getItem(), Items.getItem("platetin"),
				Items.getItem("denseplatetin"));
		addRotorRecipe(RotorType.GOLD.getItem(), Items.getItem("plategold"),
				Items.getItem("denseplategold"));
		addRotorRecipe(RotorType.COPPER.getItem(),
				Items.getItem("platecopper"), Items.getItem("denseplatecopper"));
		// addRotorRecipe(RotorType.SLIVER.getItem(),
		// Items.getItem("platecopper"), Items.getItem("copperBlock"));
		addRotorRecipe(RotorType.IRON.getItem(), Items.getItem("plateiron"),
				Items.getItem("denseplateiron"));
		addRotorRecipe(RotorType.REFINEDIRON.getItem(),
				Items.getItem("refinedIronIngot"), Items.getItem("machine"));
		addRotorRecipe(RotorType.OBSIDIAN.getItem(),
				Items.getItem("plateobsidian"),
				Items.getItem("denseplateobsidian"));
		addRotorRecipe(RotorType.BRONZE.getItem(),
				Items.getItem("platebronze"), Items.getItem("denseplatebronze"));
		addRotorRecipe(RotorType.LAPIS.getItem(), Items.getItem("platelapi"),
				Items.getItem("denseplatelapi"));
		addRotorRecipe(RotorType.QUARTZ.getItem(), Item.netherQuartz,
				Block.blockNetherQuartz);
		addRotorRecipe(RotorType.CARBON.getItem(),
				Items.getItem("carbonPlate"), Items.getItem("coalChunk"));
		addRotorRecipe(RotorType.ADVANCED.getItem(),
				Items.getItem("advancedAlloy"),
				Items.getItem("reinforcedStone"));
		addRotorRecipe(RotorType.EMERALD.getItem(), Item.emerald,
				Block.blockEmerald);
		addRotorRecipe(RotorType.DIAMOND.getItem(), Item.diamond,
				Block.blockDiamond);
		addRotorRecipe(RotorType.IRIDIUM.getItem(),
				Items.getItem("iridiumOre"), Items.getItem("iridiumPlate"));
		
		GameRegistry.addShapedRecipe(new ItemStack(turbine), "SAS", "AGS", "SAS",
				'S', Block.fenceIron, 'A', new ItemStack(waterMill, 1, 5),
				'G', Items.getItem("generator"));

		for (RotorType typ : RotorType.values()) {
			LanguageRegistry.addName(typ.getItem(), typ.showedName);
		}

		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		LanguageRegistry.instance().addStringLocalization(
				"itemGroup.creativeTabCompactWatermills", "en_US",
				"Compact Watermills");
		LanguageRegistry.instance().addStringLocalization(
				"itemGroup.creativeTabCompactWatermills", "zh_CN", "高级水力发电机");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	public static int getBlockIdFor(Configuration config,
			InternalName internalName, int standardId) {
		String name = internalName.name();

		Property prop = null;
		Integer ret;
		if (config == null) {
			ret = Integer.valueOf(standardId);
		} else {
			prop = config.get("block", name, standardId);
			ret = Integer.valueOf(prop.getInt(standardId));
		}

		if ((ret.intValue() <= 0) || (ret.intValue() > Block.blocksList.length)) {
			displayError("An invalid block ID has been detected on your IndustrialCraft 2\nconfiguration file. Block IDs cannot be higher than "
					+ (Block.blocksList.length - 1)
					+ ".\n"
					+ "\n"
					+ "Block with invalid ID: "
					+ name
					+ "\n"
					+ "Invalid ID: "
					+ ret);
		}

		if ((Block.blocksList[ret.intValue()] != null)
				|| (Item.itemsList[ret.intValue()] != null)) {
			String occupiedBy;
			if (Block.blocksList[ret.intValue()] != null)
				occupiedBy = "block " + Block.blocksList[ret.intValue()] + " ("
						+ Block.blocksList[ret.intValue()].getUnlocalizedName()
						+ ")";
			else {
				occupiedBy = "item " + Item.itemsList[ret.intValue()] + " ("
						+ Item.itemsList[ret.intValue()].getUnlocalizedName()
						+ ")";
			}

			displayError("A conflicting block ID has been detected on your IndustrialCraft 2\nconfiguration file. Block IDs cannot be used more than once.\n\nBlock with invalid ID: "
					+ name
					+ "\n"
					+ "Invalid ID: "
					+ ret
					+ "\n"
					+ "Already occupied by: " + occupiedBy);
		}

		runtimeIdProperties.setProperty("block." + name, ret.toString());

		return ret.intValue();
	}

	public static void displayError(String msg) {
		throw new RuntimeException("\n=========Compact Watermills=========\n"
				+ msg);
	}

	//isServer?
	public static boolean isSimulating() {
		return !FMLCommonHandler.instance().getEffectiveSide().isClient();
	}

}
