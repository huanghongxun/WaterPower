package org.jackhuang.watercraft.common.item.rotors;

import ic2.api.item.IC2Items;

import org.apache.logging.log4j.Level;
import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.common.recipe.IRecipeHandler;
import org.jackhuang.watercraft.util.WCLog;
import org.jackhuang.watercraft.util.mods.Mods;

import thaumcraft.api.ItemApi;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * http://zhidao.baidu.com/link?url=QrYBWorqVruUKU3H1ifnnLMKLbStwvqmt5j6XHgBSySIY7mf_EHYwljx5u7CfqvYGKR6Toj9MxjdgscPQXDEIK
 * x*200
 * @author jackhuang1998
 * 
 */
public enum RotorType {
	// 1 - IndustrialCraft 
	WOOD(0.125, 12000, "waterRotorWood"),
	STONE(0.2, 10000, "waterRotorStone"),
	LEAD(0.215, 10000, "waterRotorLead"),
	TIN(0.23, 60000, "waterRotorTin"),
	GOLD(0.7, 40000, "waterRotorGold"),
	COPPER(0.25, 80000, "waterRotorCopper"),
	SILVER(0.3, 50000, "waterRotorSilver"),
	IRON(0.35, 100000, "waterRotorIron"),
	REFINEDIRON(0.4, 120000, "waterRotorRefinedIron"),
	OBSIDIAN(0.45, 200000, "waterRotorObsidian"),
	BRONZE(0.5, 70000, "waterRotorBronze"),
	LAPIS(0.55, 30000, "waterRotorLapis"),
	QUARTZ(0.6, 180000, "waterRotorQuartz"),
	CARBON(0.65, 100000, "waterRotorCarbon"),
	ADVANCED(0.7, 60000, "waterRotorAdvanced"),
	EMERALD(0.75, 150000, "waterRotorEmerald"),
	DIAMOND(0.8, 250000, "waterRotorDiamond"),
	IRIDIUM(0.9, 340000, "waterRotorIridium"),
	IRIDIUMIRON(1, 107374182, "waterRotorIridiumIron"),
	// 2 - GregTech
	ZINC(0.23, 70000, "waterRotorZinc"),
	BRASS(0.5, 77500, "waterRotorBrass"),
	ALUMINUM(0.4, 50000, "waterRotorAluminum"),
	ELECTRUM(0.45, 45000, "waterRotorElectrum"),
	STEEL(0.5, 150000, "waterRotorSteel"),
	INVAR(0.55, 120000, "waterRotorInvar"),
	NICKEL(0.6, 160000, "waterRotorNickel"),
	TITANIUM(0.65, 230000, "waterRotorTitanium"),
	PLATINUM(0.7, 80000, "waterRotorPlatinum"),
	TUNGSTEN(0.75, 700000, "waterRotorTungsten"),
	CHROME(1, 1000000, "waterRotorChrome"),
	TUNGSTEN_STEEL(1, 1000000, "waterRotorTungstenSteel"),
	OSMIUM(0.9, 2000000, "waterRotorOsmium"),
	// 3 - Thaumcraft
	THAUMIUM(0.7, 200000, "waterRotorThaumium");
	
	public static void initRotors() {
		for (RotorType type : RotorType.values()) {
			type.initRotor();
		}
	}
	
	private double efficiency;
	
	public int maxDamage;
	
	public String unlocalizedName;
	
	public int id;
	
	private ItemRotor rotor;
	
	public boolean enable = true;
	
	private RotorType(double efficiency, int maxDamage, String unlocalizedName) {
		this.efficiency = efficiency;
		this.maxDamage = maxDamage * 20;
		this.unlocalizedName = unlocalizedName;
	}
	
	public String getShowedName() {
		return StatCollector.translateToLocal("cptwtrml.reservoir." + name()) + ' ' +
				StatCollector.translateToLocal("cptwtrml.rotor.ROTOR");
	}
	
	public double getEfficiency() {
		return 1;
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
			WCLog.err("Failed to Register Rotor: " + unlocalizedName);
		}
	}
	
	public static void registerRotor() {

		// Rotors recipes register
		addRotorRecipe(RotorType.WOOD, Items.stick, "logWood");
		addRotorRecipe(RotorType.STONE, Blocks.cobblestone, Blocks.stone);
		addRotorRecipe(RotorType.LEAD, "plateLead",
				"plateDenseLead");
		addRotorRecipe(RotorType.TIN, "plateTin",
				"plateDenseTin");
		addRotorRecipe(RotorType.GOLD, "plateGold",
				"plateDenseGold");
		addRotorRecipe(RotorType.COPPER, "plateCopper",
				"plateDenseCopper");
		addRotorRecipe(RotorType.IRON, "plateIron",
				"plateDenseIron");
		//addRotorRecipe(RotorType.REFINEDIRON,
		//		Items.getItem("refinedIronIngot"), Items.getItem("machine"));
		addRotorRecipe(RotorType.OBSIDIAN, "plateObsidian",
				"plateDenseObsidian");
		addRotorRecipe(RotorType.BRONZE, "plateBronze",
				"plateDenseBronze");
		addRotorRecipe(RotorType.LAPIS, "plateLapis",
				"plateDenseLapis");
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
		if (Mods.Thaumcraft.isAvailable) {
			/* Thaumium */
			addRotorRecipe(RotorType.THAUMIUM,
					ItemApi.getItem("itemNugget", 6),
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
