package waterpower.common.item.rotor;

import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import waterpower.client.Local;
import waterpower.common.recipe.IRecipeRegistrar;
import waterpower.util.WPLog;

/**
 * http://zhidao.baidu.com/link?url=
 * QrYBWorqVruUKU3H1ifnnLMKLbStwvqmt5j6XHgBSySIY7mf_EHYwljx5u7CfqvYGKR6Toj9MxjdgscPQXDEIK
 * x*200
 * 
 * @author jackhuang1998
 * 
 */
public enum RotorType {
	WOOD     (0.125, 12000,   "waterRotorWood"),
	STONE    (0.2,   10000,   "waterRotorStone"),
	LAPIS    (0.55,  30000,   "waterRotorLapis"),
	TIN      (0.23,  60000,   "waterRotorTin"),
	COPPER   (0.25,  80000,   "waterRotorCopper"),
	QUARTZ   (0.6,   180000,  "waterRotorQuartz"),
	ZINC     (0.23,  70000,   "waterRotorZinc"),
	BRONZE   (0.5,   70000,   "waterRotorBronze"),
	IRON     (0.35,  100000,  "waterRotorIron"),
	OBSIDIAN (0.45,  200000,  "waterRotorObsidian"),
	STEEL    (0.5,   150000,  "waterRotorSteel"),
	GOLD     (0.7,   40000,   "waterRotorGold"),
	MANGANESE(0.65,  100000,  "waterRotorManganeseSteel"),
	DIAMOND  (0.8,   250000,  "waterRotorDiamond"),
	VANADIUM (1,     1000000, "waterRotorVanadiumSteel");

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
		return Local.get("cptwtrml.reservoir." + name()) + ' '
				+ Local.get("cptwtrml.rotor.ROTOR");
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
				rotor = new ItemRotor(this);
				GameRegistry.registerItem(rotor, unlocalizedName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			WPLog.err("Failed to Register waterRotor: " + unlocalizedName);
		}
	}

	public static void registerRotor() {
		addwaterRotorRecipe(RotorType.WOOD, Items.STICK, "logWood");
		addwaterRotorRecipe(RotorType.STONE, Blocks.COBBLESTONE, Blocks.STONE);
		addwaterRotorRecipe(RotorType.LAPIS, "plateLapis", "blockLapis");
		addwaterRotorRecipe(RotorType.TIN, "plateTin", "blockTin");
		addwaterRotorRecipe(RotorType.COPPER, "plateCopper", "blockCopper");
		addwaterRotorRecipe(RotorType.QUARTZ, Items.QUARTZ, Blocks.QUARTZ_BLOCK);
		addwaterRotorRecipe(RotorType.ZINC, "plateZinc", "blockZinc");
		addwaterRotorRecipe(RotorType.BRONZE, "plateBronze", "blockBronze");
		addwaterRotorRecipe(RotorType.IRON, "plateIron", "blockIron");
		addwaterRotorRecipe(RotorType.OBSIDIAN, "plateObsidian", "blockObsidian");
		addwaterRotorRecipe(RotorType.STEEL, "plateSteel", "blockSteel");
		addwaterRotorRecipe(RotorType.GOLD, "plateGold", "blockGold");
		addwaterRotorRecipe(RotorType.MANGANESE, "plateManganeseSteel", "blockManganeseSteel");
		addwaterRotorRecipe(RotorType.DIAMOND, Items.DIAMOND, Blocks.DIAMOND_BLOCK);
		addwaterRotorRecipe(RotorType.VANADIUM, "plateVanadiumSteel", "blockVanadiumSteel");
	}

	static void addwaterRotorRecipe(RotorType output, Object S, Object I) {
		if (S == null || I == null)
			return;
		if (output.enable)
			IRecipeRegistrar.addRecipeByOreDictionary(new ItemStack(output.getItem()),
					"S S", " I ", "S S", 'S', S, 'I', I);
	}

}
