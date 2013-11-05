package org.jackhuang.compactwatermills;

import ic2.api.item.Items;

import java.util.Properties;
import java.util.logging.Level;

import org.jackhuang.compactwatermills.block.turbines.BlockReservoir;
import org.jackhuang.compactwatermills.block.turbines.BlockTurbine;
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
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
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

	public static Block waterMill, turbine, reservoir;
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
		GameRegistry.addRecipe(new ItemStack(output), "S S", " I ", "S S", 'S',
				new ItemStack(S), 'I', new ItemStack(I));
	}

	void addRotorRecipe(Item output, Block S, Block I) {
		GameRegistry.addRecipe(new ItemStack(output), "S S", " I ", "S S", 'S',
				new ItemStack(S), 'I', new ItemStack(I));
	}

	void addRotorRecipe(Item output, ItemStack S, ItemStack I) {
		GameRegistry.addRecipe(new ItemStack(output), "S S", " I ", "S S", 'S',
				S, 'I', I);
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {

		waterMill = new BlockCompactWatermill(config,
				InternalName.blockCompactWatermill);
		turbine = new BlockTurbine(config, InternalName.blockTurbine);
		reservoir = new BlockReservoir(config, InternalName.blockReservoir);
		
		for(WaterType type : WaterType.values()) {
			GameRegistry.registerTileEntity(type.claSS, type.tileEntityName());
		}
		
		GameRegistry.registerTileEntity(TileEntityTurbine.class,
				"turbine");

		// Water mills recipes register
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 0), "WWW",
				"WTW", "WWW", 'W', Items.getItem("waterMill"), 'T',
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
		throw new RuntimeException("=========Compact Watermills=========\n"
				+ msg);
	}
}
