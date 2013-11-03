/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package org.jackhuang.compactwatermills;

import ic2.api.item.Items;

import java.util.logging.Level;

import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.rotors.ItemRotor;
import org.jackhuang.compactwatermills.rotors.RotorType;
import org.jackhuang.compactwatermills.watermills.BlockCompactWatermill;
import org.jackhuang.compactwatermills.watermills.ItemCompactWaterMill;
import org.jackhuang.compactwatermills.watermills.WaterType;

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
 * @author Aroma1997
 *
 */
public class CompactWatermills {
	
	@Mod.Instance(Reference.ModID)
	public static CompactWatermills instance;
	
	@SidedProxy(clientSide = "org.jackhuang.compactwatermills.ClientProxy", serverSide = "org.jackhuang.compactwatermills.CommonProxy")
	public static CommonProxy proxy;
		
	private static int blockID;
	
	public static Block waterMill;
	public static ItemRotor waterRotor;
	
	public static final CreativeTabs creativeTabCompactWatermills = new CreativeTabCompactWatermills(
		"creativeTabCompactWatermills");
	
	public static final int updateTick = 20;
	
	public static boolean debugMode;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LogHelper.init();
		if (Reference.Version == "@VERSION@") {
			debugMode = true;
			LogHelper.log(Level.INFO, "Turning on debug mode.");
		}
		Configuration config = new Configuration(
			event.getSuggestedConfigurationFile());
		config.load();
		Property block = config.getBlock(Reference.configMillName, Reference.defaultMillID);
		block.comment = "This is the id of the Compact Watermill Blocks.";
		blockID = block.getInt(Reference.defaultMillID);
		Property rotor = config.getItem(Reference.configRotorName, Reference.defaultRotorID);
		rotor.comment = "This is the id of rotor Items.";
		
		for (RotorType typ : RotorType.values()) {
			typ.getConfig(config);
		}
		RotorType.initRotors();
		
		config.save();
		
		waterMill = new BlockCompactWatermill(blockID);
	}
	
	void addRotorRecipe(Item output, Item S, Block I) {
		GameRegistry.addRecipe(new ItemStack(output), "S S", " I ", "S S", 'S', new ItemStack(S), 'I', new ItemStack(I));
	}
	
	void addRotorRecipe(Item output, Block S, Block I) {
		GameRegistry.addRecipe(new ItemStack(output), "S S", " I ", "S S", 'S', new ItemStack(S), 'I', new ItemStack(I));
	}
	
	void addRotorRecipe(Item output, ItemStack S, ItemStack I) {
		GameRegistry.addRecipe(new ItemStack(output), "S S", " I ", "S S", 'S', S, 'I', I);
	}
	
	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {
		
		GameRegistry.registerBlock(waterMill, ItemCompactWaterMill.class, "blockCompactWatermill");
		for (WaterType typ : WaterType.values()) {
			GameRegistry.registerTileEntity(typ.claSS, typ.tileEntityName());
		}
		
		
		//Water mills recipes register
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 0), " W ", "WTW", " W ", 'W',
			Items.getItem("waterMill"), 'T', Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 1), " W ", "WTW", " W ", 'W',
			new ItemStack(waterMill, 1, 0), 'T', Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 2), " W ", "WTW", " W ", 'W',
			new ItemStack(waterMill, 1, 1), 'T', Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 3), " W ", "WTW", " W ", 'W',
			new ItemStack(waterMill, 1, 2), 'T', Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(waterMill, 1, 4), " W ", "WTW", " W ", 'W',
			new ItemStack(waterMill, 1, 3), 'T', Items.getItem("transformerUpgrade"));
		
		//Rotors recipes register
		addRotorRecipe(RotorType.WOOD.getItem(), Item.stick, Block.wood);
		addRotorRecipe(RotorType.STONE.getItem(), Block.cobblestone, Block.stone);
		addRotorRecipe(RotorType.LEAD.getItem(), Items.getItem("platelead"), Items.getItem("denseplatelead"));
		addRotorRecipe(RotorType.TIN.getItem(), Items.getItem("platetin"), Items.getItem("denseplatetin"));
		addRotorRecipe(RotorType.GOLD.getItem(), Items.getItem("plategold"), Items.getItem("denseplategold"));
		addRotorRecipe(RotorType.COPPER.getItem(), Items.getItem("platecopper"), Items.getItem("denseplatecopper"));
		//addRotorRecipe(RotorType.SLIVER.getItem(), Items.getItem("platecopper"), Items.getItem("copperBlock"));
		addRotorRecipe(RotorType.IRON.getItem(), Items.getItem("plateiron"), Items.getItem("denseplateiron"));
		addRotorRecipe(RotorType.REFINEDIRON.getItem(), Items.getItem("refinedIronIngot"), Items.getItem("machine"));
		addRotorRecipe(RotorType.OBSIDIAN.getItem(), Items.getItem("plateobsidian"), Items.getItem("denseplateobsidian"));
		addRotorRecipe(RotorType.BRONZE.getItem(), Items.getItem("platebronze"), Items.getItem("denseplatebronze"));
		addRotorRecipe(RotorType.LAPIS.getItem(), Items.getItem("platelapi"), Items.getItem("denseplatelapi"));
		addRotorRecipe(RotorType.QUARTZ.getItem(), Item.netherQuartz, Block.blockNetherQuartz);
		addRotorRecipe(RotorType.CARBON.getItem(), Items.getItem("carbonPlate"), Items.getItem("coalChunk"));
		addRotorRecipe(RotorType.ADVANCED.getItem(), Items.getItem("advancedAlloy"), Items.getItem("reinforcedStone"));
		addRotorRecipe(RotorType.EMERALD.getItem(), Item.emerald, Block.blockEmerald);
		addRotorRecipe(RotorType.DIAMOND.getItem(), Item.diamond, Block.blockDiamond);
		addRotorRecipe(RotorType.IRIDIUM.getItem(), Items.getItem("iridiumOre"), Items.getItem("iridiumPlate"));
		
		for (RotorType typ : RotorType.values()) {
			LanguageRegistry.addName(typ.getItem(), typ.showedName);
		}
		
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		LanguageRegistry.instance().addStringLocalization("itemGroup.creativeTabCompactWatermills", "en_US",
				"Compact Watermills");
		LanguageRegistry.instance().addStringLocalization("itemGroup.creativeTabCompactWatermills", "zh_CN",
				"高级水力发电机");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}
