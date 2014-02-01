package org.jackhuang.compactwatermills;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.enums.Materials;
import gregtechmod.api.items.GT_Generic_Item;
import ic2.api.item.Items;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.jackhuang.compactwatermills.block.BlockBase;
import org.jackhuang.compactwatermills.block.GlobalBlocks;
import org.jackhuang.compactwatermills.block.reservoir.BlockReservoir;
import org.jackhuang.compactwatermills.block.reservoir.ReservoirType;
import org.jackhuang.compactwatermills.block.reservoir.TileEntityReservoir;
import org.jackhuang.compactwatermills.block.turbines.BlockTurbine;
import org.jackhuang.compactwatermills.block.turbines.TileEntityTurbine;
import org.jackhuang.compactwatermills.block.watermills.BlockCompactWatermill;
import org.jackhuang.compactwatermills.block.watermills.WaterType;
import org.jackhuang.compactwatermills.client.gui.CreativeTabCompactWatermills;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.item.GlobalItems;
import org.jackhuang.compactwatermills.item.ItemBase;
import org.jackhuang.compactwatermills.item.rotors.ItemRotor;
import org.jackhuang.compactwatermills.item.rotors.RotorType;
import org.jackhuang.compactwatermills.item.updates.ItemWatermillUpdater;
import org.jackhuang.compactwatermills.item.updates.UpdaterType;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.Property;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
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

	@SidedProxy(clientSide = "org.jackhuang.compactwatermills.client.gui.ClientProxy", serverSide = "org.jackhuang.compactwatermills.CommonProxy")
	public static CommonProxy proxy;

	public static final CreativeTabs creativeTabCompactWatermills = new CreativeTabCompactWatermills(
			"creativeTabCompactWatermills");

	public static final int updateTick = 20;

	public static Properties runtimeIdProperties;

	private Configuration config;

	static {
		runtimeIdProperties = new Properties();
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LogHelper.init();
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		for (RotorType typ : RotorType.values()) {
			typ.getConfig(config);
		}
		RotorType.initRotors();
		
		Property updater = config.getItem(Reference.configUpdaterName, DefaultIds.get(InternalName.itemUpdaters));
		updater.comment = "This is the id of watermill updater items.";

		config.save();

	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {
		
		RecipeHandler handler = new RecipeHandler(config);
		handler.registerAllRecipes();

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
		
		config.save();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.registerRenderer();
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

	/**
	 * isServer?
	 * @return
	 */
	public static boolean isSimulating() {
		return !FMLCommonHandler.instance().getEffectiveSide().isClient();
	}

}
