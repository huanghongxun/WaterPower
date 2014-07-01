package org.jackhuang.compactwatermills.common.item.rotors;

import ic2.api.item.IC2Items;

import org.apache.logging.log4j.Level;
import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.common.item.others.ItemType;
import org.jackhuang.compactwatermills.common.recipe.IRecipeHandler;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import thaumcraft.api.ItemApi;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * 按照布氏硬度：http://zhidao.baidu.com/link?url=QrYBWorqVruUKU3H1ifnnLMKLbStwvqmt5j6XHgBSySIY7mf_EHYwljx5u7CfqvYGKR6Toj9MxjdgscPQXDEIK
 * x*200
 * @author jackhuang1998
 * 
 */
public enum RotorType {
	// 1 - IndustrialCraft 
	WOOD(0.125, 1200, "木转子", "waterRotorWood"),
	STONE(0.2, 1000, "石转子", "waterRotorStone"),
	LEAD(0.215, 1000, "铅转子", "waterRotorLead"),
	TIN(0.23, 6000, "锡转子", "waterRotorTin"),
	GOLD(0.7, 4000, "金转子", "waterRotorGold"),
	COPPER(0.25, 8000, "铜转子", "waterRotorCopper"),
	SILVER(0.3, 5000, "银转子", "waterRotorSilver"),
	IRON(0.35, 10000, "铁转子", "waterRotorIron"),
	REFINEDIRON(0.4, 12000, "精炼铁转子", "waterRotorRefinedIron"),
	OBSIDIAN(0.45, 20000, "黑曜石转子", "waterRotorObsidian"),
	BRONZE(0.5, 7000, "青铜转子", "waterRotorBronze"),
	LAPIS(0.55, 3000, "青金石转子", "waterRotorLapis"),
	QUARTZ(0.6, 18000, "石英转子", "waterRotorQuartz"),
	CARBON(0.65, 10000, "碳板转子", "waterRotorCarbon"),
	ADVANCED(0.7, 6000, "高级合金转子", "waterRotorAdvanced"),
	EMERALD(0.75, 15000, "绿宝石转子", "waterRotorEmerald"),
	DIAMOND(0.8, 25000, "钻石转子", "waterRotorDiamond"),
	IRIDIUM(0.9, 34000, "铱转子", "waterRotorIridium"),
	IRIDIUMIRON(1, 107374182, "铱铁转子", "waterRotorIridiumIron"),
	// 2 - GregTech
	ZINC(0.23, 7000, "锌转子", "waterRotorZinc"),
	BRASS(0.5, 7750, "黄铜转子", "waterRotorBrass"),
	ALUMINUM(0.4, 5000, "铝转子", "waterRotorAluminum"),
	ELECTRUM(0.45, 4500, "金银转子", "waterRotorElectrum"),
	STEEL(0.5, 15000, "钢转子", "waterRotorSteel"),
	INVAR(0.55, 12000, "殷钢转子", "waterRotorInvar"),
	NICKEL(0.6, 16000, "镍转子", "waterRotorNickel"),
	TITANIUM(0.65, 23000, "钛转子", "waterRotorTitanium"),
	PLATINUM(0.7, 8000, "铂转子", "waterRotorPlatinum"),
	TUNGSTEN(0.75, 70000, "钨转子", "waterRotorTungsten"),
	CHROME(1, 100000, "铬转子", "waterRotorChrome"),
	TUNGSTEN_STEEL(1, 100000, "钨钢转子", "waterRotorTungstenSteel"),
	OSMIUM(0.9, 200000, "锇转子", "waterRotorOsmium"),
	// 3 - Thaumcraft
	THAUMIUM(0.7, 20000, "神秘转子", "waterRotorThaumium");
	
	public static void initRotors() {
		for (RotorType type : RotorType.values()) {
			type.initRotor();
		}
	}
	
	public double efficiency;
	
	public int maxDamage;
	
	private String showedName;
	
	public String unlocalizedName;
	
	public int id;
	
	private ItemRotor rotor;
	
	public boolean enable = true;
	
	private RotorType(double efficiency, int maxDamage, String showedName,
		String unlocalizedName) {
		this.efficiency = efficiency;
		this.maxDamage = maxDamage * 20;
		this.showedName = showedName;
		this.unlocalizedName = unlocalizedName;
	}
	
	public void getConfig(Configuration config) {
		//Property rotorId = config.get("item", this.unlocalizedName, Reference.defaultRotorID + ordinal());
		//rotorId.comment = "This is the id of " + showedName + " Item.";
		//id = rotorId.getInt(Reference.defaultRotorID + ordinal());
		//Property enableRotor = config.get("enable", this.unlocalizedName, true);
		//enableRotor.comment = "这关系到是否启用" + showedName;
		//enable = enableRotor.getBoolean(true);
	}
	
	public String getShowedName() {
		return StatCollector.translateToLocal("cptwtrml.reservoir." + name()) + ' ' +
				StatCollector.translateToLocal("cptwtrml.rotor.ROTOR");
	}
	
	public Item getItem() {
		return rotor;
	}
	
	public ItemRotor getItemRotor() {
		return rotor;
	}
	
	public boolean isInfinite() {
		return maxDamage == 0;
	}
	
	private void initRotor() {
		try {
			if(enable) {
				rotor = (ItemRotor) new ItemRotor(this);
				GameRegistry.registerItem(rotor, unlocalizedName);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			LogHelper.log(Level.ERROR, "Failed to Register Rotor: " + showedName);
		}
	}
	
	public static void registerRotor() {

		// Rotors recipes register
		addRotorRecipe(RotorType.WOOD, Items.stick, "logWood");
		addRotorRecipe(RotorType.STONE, Blocks.cobblestone, Blocks.stone);
		addRotorRecipe(RotorType.LEAD, "plateLead", //Items.getItem("platelead"),
				IC2Items.getItem("denseplatelead"));
		addRotorRecipe(RotorType.TIN, "plateTin",// Items.getItem("platetin"),
				IC2Items.getItem("denseplatetin"));
		addRotorRecipe(RotorType.GOLD, "plateGold", // Items.getItem("plategold"),
				IC2Items.getItem("denseplategold"));
		addRotorRecipe(RotorType.COPPER, "plateCopper", // Items.getItem("platecopper"),
				IC2Items.getItem("denseplatecopper"));
		addRotorRecipe(RotorType.IRON, "plateIron", // Items.getItem("plateiron"),
				IC2Items.getItem("denseplateiron"));
		addRotorRecipe(RotorType.REFINEDIRON,
				IC2Items.getItem("refinedIronIngot"), IC2Items.getItem("machine"));
		addRotorRecipe(RotorType.OBSIDIAN, "plateObsidian", // Items.getItem("plateobsidian"),
				IC2Items.getItem("denseplateobsidian"));
		addRotorRecipe(RotorType.BRONZE, "plateBronze", // Items.getItem("platebronze"),
				IC2Items.getItem("denseplatebronze"));
		addRotorRecipe(RotorType.LAPIS, "plateLapis", // Items.getItem("platelapi"),
				IC2Items.getItem("denseplatelapi"));
		addRotorRecipe(RotorType.QUARTZ, Items.quartz,
				Blocks.quartz_block);
		addRotorRecipe(RotorType.CARBON, IC2Items.getItem("carbonPlate"),
				IC2Items.getItem("coalChunk"));
		addRotorRecipe(RotorType.ADVANCED, IC2Items.getItem("advancedAlloy"),
				IC2Items.getItem("reinforcedStone"));
		addRotorRecipe(RotorType.EMERALD, Items.emerald, Blocks.emerald_block);
		addRotorRecipe(RotorType.DIAMOND, Items.diamond, Blocks.diamond_block);
		addRotorRecipe(RotorType.IRIDIUM, IC2Items.getItem("iridiumOre"),
				IC2Items.getItem("iridiumPlate"));
		addRotorRecipe(RotorType.IRIDIUMIRON, IC2Items.getItem("iridiumPlate"),
				ItemType.IR_FE.item());

		addRotorRecipe(RotorType.SILVER, "plateSilver", // GregTech_API.getGregTechMaterial(69,
														// 1),
				"blockSilver"); // GregTech_API.getGregTechBlock(0, 1, 3));
		addRotorRecipe(RotorType.ZINC, "plateZinc", // GregTech_API.getGregTechMaterial(82,
													// 1),
				"blockZinc"); // GregTech_API.getGregTechBlock(4, 1, 2));
		addRotorRecipe(RotorType.BRASS, "plateBrass", // GregTech_API.getGregTechMaterial(81,
														// 1),
				"blockBrass"); // GregTech_API.getGregTechBlock(0, 1, 12));
		addRotorRecipe(RotorType.ALUMINUM, "plateAluminium", // GregTech_API.getGregTechMaterial(75,
																// 1),
				"blockAluminium"); // GregTech_API.getGregTechBlock(0, 1, 7));
		addRotorRecipe(RotorType.STEEL, "plateSteel", // GregTech_API.getGregTechMaterial(78,
														// 1),
				"blockSteel"); // GregTech_API.getGregTechBlock(0, 1, 11));
		addRotorRecipe(RotorType.INVAR, "plateInvar", // GregTech_API.getGregTechMaterial(73,
														// 1),
				"blockInvar"); // GregTech_API.getGregTechBlock(4, 1, 10));
		addRotorRecipe(RotorType.ELECTRUM, "plateElectrum", // GregTech_API.getGregTechMaterial(71,
															// 1),
				"blockElectrum"); // GregTech_API.getGregTechBlock(4, 1, 1));
		addRotorRecipe(RotorType.NICKEL, "plateNickel", // GregTech_API.getGregTechMaterial(72,
														// 1),
				"blockNickel"); // GregTech_API.getGregTechBlock(4, 1, 7));
		addRotorRecipe(RotorType.OSMIUM, "plateOsmium", // GregTech_API.getGregTechMaterial(84,
														// 1),
				"blockOsmium"); // GregTech_API.getGregTechBlock(4, 1, 11));
		addRotorRecipe(RotorType.TITANIUM, "plateTitanium", // GregTech_API.getGregTechMaterial(77,
															// 1),
				"blockTitanium"); // GregTech_API.getGregTechBlock(0, 1, 8));
		addRotorRecipe(RotorType.PLATINUM, "platePlatinum", // GregTech_API.getGregTechMaterial(79,
															// 1),
				"blockPlatinum"); // GregTech_API.getGregTechBlock(4, 1, 5));
		addRotorRecipe(RotorType.TUNGSTEN, "plateTungsten", // GregTech_API.getGregTechMaterial(80,
															// 1),
				"blockTungsten");// GregTech_API.getGregTechBlock(4, 1, 6));
		addRotorRecipe(RotorType.CHROME, "plateChrome", // GregTech_API.getGregTechMaterial(76,
														// 1),
				"blockChrome"); // GregTech_API.getGregTechBlock(0, 1, 9));
		addRotorRecipe(RotorType.TUNGSTEN_STEEL, "plateTungstenSteel", // GregTech_API.getGregTechMaterial(83,
																		// 1),
				"blockTungstenSteel");//GregTech_API.getGregTechBlock(4, 1, 8));
		if (CompactWatermills.isThaumcraftLoaded) {
			/* Thaumium */
			addRotorRecipe(RotorType.THAUMIUM,
					ItemApi.getItem("itemNugget", 6),
					// GregTech_API.getGregTechMaterial(17330, 1),
					ItemApi.getBlock("blockCosmeticSolid", 4));
		}
	}

	static void addRotorRecipe(RotorType output, Object S, Object I) {
		if (output.enable) {
				IRecipeHandler.addRecipeByOreDictionary(new ItemStack(output.getItem()),
						"S S", " I ", "S S", 'S', S, 'I', I);
		}
	}
	
}
