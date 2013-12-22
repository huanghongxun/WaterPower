package org.jackhuang.compactwatermills;

import ic2.api.item.Items;

import java.util.Properties;
import java.util.logging.Level;

import org.jackhuang.compactwatermills.block.BlockBase;
import org.jackhuang.compactwatermills.block.reservoir.BlockReservoir;
import org.jackhuang.compactwatermills.block.reservoir.ReservoirType;
import org.jackhuang.compactwatermills.block.reservoir.TileEntityReservoir;
import org.jackhuang.compactwatermills.block.turbines.BlockTurbine;
import org.jackhuang.compactwatermills.block.turbines.TileEntityTurbine;
import org.jackhuang.compactwatermills.block.watermills.BlockCompactWatermill;
import org.jackhuang.compactwatermills.block.watermills.WaterType;
import org.jackhuang.compactwatermills.gui.CreativeTabCompactWatermills;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.item.rotors.ItemRotor;
import org.jackhuang.compactwatermills.item.rotors.RotorType;
import org.jackhuang.compactwatermills.item.updates.UpdaterType;

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

	public static final CreativeTabs creativeTabCompactWatermills = new CreativeTabCompactWatermills(
			"creativeTabCompactWatermills");

	public static final int updateTick = 20;

	public static Properties runtimeIdProperties;

	public static boolean debugMode;

	private Configuration config;

	static {
		runtimeIdProperties = new Properties();
	}

	@SuppressWarnings("unused")
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LogHelper.init();
		if (Reference.Version == "@VERSION@") {
			debugMode = true;
			LogHelper.log(Level.INFO, "Turning on debug mode.");
		} else {
			debugMode = false;
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
		
		Property updater = config.getItem(Reference.configUpdaterName, DefaultIds.get(InternalName.itemUpdaters));
		updater.comment = "This is the id of watermill updater items.";
		
		UpdaterType.initUpdaters(config);

		config.save();

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

		for (WaterType type : WaterType.values()) {
			GameRegistry.registerTileEntity(type.claSS, type.tileEntityName());
		}

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
		//GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 6), " W ",
		//		"WTW", " W ", 'W', new ItemStack(waterMill, 1, 5), 'T',
		//		Items.getItem("transformerUpgrade"));


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
		// addRotorRecipe(RotorType.SLIVER,
		// Items.getItem("platecopper"), Items.getItem("copperBlock"));
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

		for (RotorType typ : RotorType.values()) {
			if(typ.enable)
			LanguageRegistry.addName(typ.getItem(), typ.showedName);
		}

		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		LanguageRegistry.instance().addStringLocalization(
				"itemGroup.creativeTabCompactWatermills", "en_US",
				"Compact Watermills");
		LanguageRegistry.instance().addStringLocalization(
				"itemGroup.creativeTabCompactWatermills", "zh_CN", "高级水力发电机");
		
		Property p = config.get("enable", "EnableReservoir", true);
		if(p.getBoolean(true))
			registerReservoir();
		
		config.save();
	}
	
	private void registerUpdater() {
		GameRegistry.addShapedRecipe(new ItemStack(UpdaterType.MK1.), "SAS", "AGA", "SAS",
				'S', Items.getItem("advancedAlloy"),
				'A', Items.getItem("carbonPlate"),
				'G', Items.getItem("transformerUpgrade"));
	}
	
	private void registerReservoir() {
		
		// Blocks registering		
		turbine = new BlockTurbine(config, InternalName.blockTurbine);
		reservoir = new BlockReservoir(config, InternalName.blockReservoir);
		
		// TileEntities registering
		for (ReservoirType type : ReservoirType.values()) {
			GameRegistry.registerTileEntity(type.claSS, type.tileEntityName());
		}

		GameRegistry.registerTileEntity(TileEntityTurbine.class, "turbine");
		
		// Turbine recipe registering
		GameRegistry.addShapedRecipe(new ItemStack(turbine), "SAS", "AGS", "SAS",
				'S', Block.fenceIron, 'A', new ItemStack(waterMill, 1, 5),
				'G', Items.getItem("generator"));
		
		// Reservoir recipes registering
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
			displayError("An invalid block ID has been detected on your Compact Watermills\nconfiguration file. Block IDs cannot be higher than "
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

			displayError("A conflicting block ID has been detected on your Compact Watermills\nconfiguration file. Block IDs cannot be used more than once.\n\nBlock with invalid ID: "
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

	public static int getItemIdFor(Configuration config,
			InternalName internalName, int standardId) {
		String name = internalName.name();

		Property prop = null;
		Integer ret;
		if (config == null) {
			ret = Integer.valueOf(standardId);
		} else {
			prop = config.get("item", name, standardId);
			ret = Integer.valueOf(prop.getInt(standardId));
		}

		if ((ret.intValue() <= 0) || (ret.intValue() > Item.itemsList.length)) {
			displayError("An invalid item ID has been detected on your Compact Watermills\nconfiguration file. Item IDs cannot be higher than "
					+ (Block.blocksList.length - 1)
					+ ".\n"
					+ "\n"
					+ "Item with invalid ID: "
					+ name
					+ "\n"
					+ "Invalid ID: "
					+ ret);
		}

		if (Item.itemsList[ret.intValue()] != null) {
			String occupiedBy;
			occupiedBy = "item " + Item.itemsList[ret.intValue()] + " ("
					+ Item.itemsList[ret.intValue()].getUnlocalizedName()
					+ ")";

			displayError("A conflicting item ID has been detected on your Compact Watermills\nconfiguration file. Item IDs cannot be used more than once.\n\nItem with invalid ID: "
					+ name
					+ "\n"
					+ "Invalid ID: "
					+ ret
					+ "\n"
					+ "Already occupied by: " + occupiedBy);
		}

		runtimeIdProperties.setProperty("item." + name, ret.toString());

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
