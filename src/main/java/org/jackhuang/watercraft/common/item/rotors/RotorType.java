package org.jackhuang.watercraft.common.item.rotors;

import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.common.recipe.IRecipeRegistrar;
import org.jackhuang.watercraft.integration.ic2.ICItemFinder;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.WPLog;

import thaumcraft.api.ItemApi;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

/**
 * http://zhidao.baidu.com/link?url=QrYBWorqVruUKU3H1ifnnLMKLbStwvqmt5j6XHgBSySIY7mf_EHYwljx5u7CfqvYGKR6Toj9MxjdgscPQXDEIK
 * x*200
 *
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
	return StatCollector.translateToLocal("cptwtrml.reservoir." + name()) + ' '
		+ StatCollector.translateToLocal("cptwtrml.rotor.ROTOR");
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
	    if (enable) {
		rotor = (ItemRotor) new ItemRotor(this);
		GameRegistry.registerItem(rotor, unlocalizedName);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    WPLog.err("Failed to Register Rotor: " + unlocalizedName);
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
	addRotorRecipe(RotorType.CARBON, ICItemFinder.getIC2Item("carbonPlate"),
		ICItemFinder.getIC2Item("coalChunk"));
	addRotorRecipe(RotorType.ADVANCED, ICItemFinder.getIC2Item("advancedAlloy"),
		ICItemFinder.getIC2Item("reinforcedStone"));
	addRotorRecipe(RotorType.EMERALD, Items.emerald, Blocks.emerald_block);
	addRotorRecipe(RotorType.DIAMOND, Items.diamond, Blocks.diamond_block);
	addRotorRecipe(RotorType.IRIDIUM, ICItemFinder.getIC2Item("iridiumOre"),
		ICItemFinder.getIC2Item("iridiumPlate"));
	addRotorRecipe(RotorType.IRIDIUMIRON, ICItemFinder.getIC2Item("iridiumPlate"),
		ItemType.IR_FE.item());

	addRotorRecipe(RotorType.SILVER, "plateSilver",
		"blockSilver");
	addRotorRecipe(RotorType.ZINC, "plateZinc",
		"blockZinc");
	addRotorRecipe(RotorType.BRASS, "plateBrass",
		"blockBrass");
	addRotorRecipe(RotorType.ALUMINUM, "plateAluminium",
		"blockAluminium");
	addRotorRecipe(RotorType.STEEL, "plateSteel",
		"blockSteel");
	addRotorRecipe(RotorType.INVAR, "plateInvar",
		"blockInvar");
	addRotorRecipe(RotorType.ELECTRUM, "plateElectrum",
		"blockElectrum");
	addRotorRecipe(RotorType.NICKEL, "plateNickel",
		"blockNickel");
	addRotorRecipe(RotorType.OSMIUM, "plateOsmium",
		"blockOsmium");
	addRotorRecipe(RotorType.TITANIUM, "plateTitanium",
		"blockTitanium");
	addRotorRecipe(RotorType.PLATINUM, "platePlatinum",
		"blockPlatinum");
	addRotorRecipe(RotorType.TUNGSTEN, "plateTungsten",
		"blockTungsten");
	addRotorRecipe(RotorType.CHROME, "plateChrome",
		"blockChrome");
	addRotorRecipe(RotorType.TUNGSTEN_STEEL, "plateTungstenSteel",
		"blockTungstenSteel");
	if (Mods.Thaumcraft.isAvailable) {
	    addRotorRecipe(RotorType.THAUMIUM,
		    ItemApi.getItem("itemNugget", 6),
		    ItemApi.getBlock("blockCosmeticSolid", 4));
	}
    }

    static void addRotorRecipe(RotorType output, Object S, Object I) {
	if (S == null || I == null) {
	    return;
	}
	if (output.enable) {
	    IRecipeRegistrar.addRecipeByOreDictionary(new ItemStack(output.getItem()),
		    "S S", " I ", "S S", 'S', S, 'I', I);
	}
    }

}
