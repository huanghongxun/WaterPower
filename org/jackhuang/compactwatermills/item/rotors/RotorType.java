package org.jackhuang.compactwatermills.item.rotors;


import java.util.logging.Level;

import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/**
 * 
 * @author jackhuang1998
 * 
 */
public enum RotorType {
	// 1 - IndustrialCraft 
	WOOD(0.125, 1800, "木转子", "waterRotorWood"),
	STONE(0.2, 500, "石转子", "waterRotorStone"),
	LEAD(0.215, 500, "铅转子", "waterRotorLead"),
	TIN(0.23, 700, "锡转子", "waterRotorTin"),
	GOLD(0.7, 800, "金转子", "waterRotorGold"),
	COPPER(0.25, 800, "铜转子", "waterRotorCopper"),
	SILVER(0.3, 1000, "银转子", "waterRotorSilver"),
	IRON(0.35, 2000, "铁转子", "waterRotorIron"),
	REFINEDIRON(0.4, 2600, "精炼铁转子", "waterRotorRefinedIron"),
	OBSIDIAN(0.45, 2100, "黑曜石转子", "waterRotorObsidian"),
	BRONZE(0.5, 2200, "青铜转子", "waterRotorBronze"),
	LAPIS(0.55, 1800, "青金石转子", "waterRotorLapis"),
	QUARTZ(0.6, 3000, "石英转子", "waterRotorQuartz"),
	CARBON(0.65, 6000, "碳板转子", "waterRotorCarbon"),
	ADVANCED(0.7, 3000, "高级合金转子", "waterRotorAdvanced"),
	EMERALD(0.75, 7000, "绿宝石转子", "waterRotorEmerald"),
	DIAMOND(0.8, 10000, "钻石转子", "waterRotorDiamond"),
	IRIDIUM(0.9, 40000, "铱转子", "waterRotorIridium"),
	IRIDIUMIRON(1, 60000, "铱铁转子", "waterRotorIridiumIron"),
	// 2 - GregTech
	ZINC(0.23, 700, "锌转子", "waterRotorZinc"),
	BRASS(0.5, 2200, "黄铜转子", "waterRotorBrass"),
	ALUMINUM(0.4, 2600, "铝转子", "waterRotorAluminum"),
	ELECTRUM(0.45, 2600, "金银转子", "waterRotorElectrum"),
	STEEL(0.5, 3000, "钢转子", "waterRotorSteel"),
	INVAR(0.55, 2000, "殷钢转子", "waterRotorInvar"),
	NICKEL(0.6, 2500, "镍转子", "waterRotorNickel"),
	TITANIUM(0.65, 3000, "钛转子", "waterRotorTitanium"),
	PLATINUM(0.7, 10000, "铂转子", "waterRotorPlatinum"),
	TUNGSTEN(0.75, 15000, "钨转子", "waterRotorTungsten"),
	CHROME(0.8, 20000, "铬转子", "waterRotorChrome"),
	TUNGSTEN_STEEL(0.85, 30000, "钨钢转子", "waterRotorTungstenSteel"),
	OSMIUM(0.9, 50000, "锇转子", "waterRotorOsmium");
	
	public static void initRotors() {
		for (RotorType type : RotorType.values()) {
			type.initRotor();
		}
	}
	
	public double efficiency;
	
	public int maxDamage;
	
	public String showedName;
	
	public String unlocalizedName;
	
	public int id;
	
	private ItemRotor rotor;
	
	public boolean enable;
	
	private RotorType(double efficiency, int maxDamage, String showedName,
		String unlocalizedName) {
		this.efficiency = efficiency;
		this.maxDamage = maxDamage * 20;
		this.showedName = showedName;
		this.unlocalizedName = unlocalizedName;
	}
	
	public void getConfig(Configuration config) {
		Property rotorId = config.getItem(this.unlocalizedName, Reference.defaultRotorID + ordinal());
		rotorId.comment = "This is the id of " + showedName + " Item.";
		id = rotorId.getInt(Reference.defaultRotorID + ordinal());
		Property enableRotor = config.get("enable", this.unlocalizedName, true);
		enableRotor.comment = "这关系到是否启用" + showedName;
		enable = enableRotor.getBoolean(true);
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
			if(enable)
				rotor = (ItemRotor) ItemRotor.class.getConstructor(RotorType.class).newInstance(this)
					.setUnlocalizedName(unlocalizedName)
					.setMaxDamage(maxDamage);
		}
		catch (Exception e) {
			e.printStackTrace();
			LogHelper.log(Level.SEVERE, "Failed to Register Rotor: " + showedName);
		}
	}
	
}
