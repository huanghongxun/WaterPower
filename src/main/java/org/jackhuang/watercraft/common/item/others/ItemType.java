package org.jackhuang.watercraft.common.item.others;

import java.util.logging.Level;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.jackhuang.watercraft.InternalName;
import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.item.GlobalItems;

public enum ItemType {
	WaterUraniumIngot("水之铀锭", "watermillWaterUraniumIngot"),
	WaterUraniumPlateMK1("水之铀锭板MK1", "watermillWaterUraniumPlateMK1"),
	WaterUraniumPlateMK2("水之铀锭板MK2", "watermillWaterUraniumPlateMK2"),
	WaterUraniumPlateMK3("水之铀锭板MK3", "watermillWaterUraniumPlateMK3"),
	WaterUraniumPlateMK4("水之铀锭板MK4", "watermillWaterUraniumPlateMK4"),
	WaterUraniumPlateMK5("水之铀锭板MK5", "watermillWaterUraniumPlateMK5"),
	WaterUraniumPlateMK6("水之铀锭板MK6", "watermillWaterUraniumPlateMK6"),
	WaterUraniumPlateMK7("水之铀锭板MK7", "watermillWaterUraniumPlateMK7"),
	IR_FE("铱铁合金板", "watermillIrFePlate"),
	MK0("基本升级插件", "watermillUpdaterMK0"),
	MK1("升级插件MK1", "watermillUpdaterMK1"),
	MK2("升级插件MK2", "watermillUpdaterMK2"),
	MK3("升级插件MK3", "watermillUpdaterMK3"),
	MK4("升级插件MK4", "watermillUpdaterMK4"),
	MK5("升级插件MK5", "watermillUpdaterMK5"),
	MK6("升级插件MK6", "watermillUpdaterMK6"),
	MK7("升级插件MK7", "watermillUpdaterMK7"),
	ReservoirCore("水库方块核心", "watermillReservoirCore"),
	ReservoirCoreAdvanced("高级水库方块核心", "watermillReservoirCoreAdvanced"),
	PlasmaUraniumIngot("氦铀等离子锭", "plasmaUraniumIngot"),
	PlasmaUraniumAlloyPlate("氦铀等离子合金板", "plasmaUraniumAlloyPlate"),
	WaterUraniumAlloyPlate("水之铀锭合金板", "waterUraniumAlloyPlate"),
	BaseRotor("基础转子", "watermillBaseRotor"),
	StoneStruct("石制支柱", "watermillStoneStruct"),
	WoodenHammer("木棰", "watermillWoodenHammer"),
	WaterResistantRubber("防水橡胶", "watermillWaterResistantRubber"),
	WaterResistantRubberPlate("防水橡胶板", "watermillWaterResistantRubberPlate"),
	WaterResistantRubberDensePlate("厚防水橡胶板", "watermillWaterResistantRubberDensePlate"),
	DenseRedstonePlate("致密红石合金板", "watermillDenseRedstonePlate"),
	DenseCoil("致密线圈", "watermillDenseCoil"),
	OxygenEthanolFuel("氧-乙醇焰燃料", "watermillOxygenEthanolFuel"),
	SilverCoil("银线圈", "watermillSilverCoil"),
	DenseSilverCoil("厚实银线圈", "watermillDenseSilverCoil"),
	FerricOxideDust("氧化铁粉", "watermillFerricOxide"),
	FerroferricOxideDust("四氧化三铁粉", "watermillFerroferricOxide"),
	FerricOxideSmallDust("小堆氧化铁粉", "watermillFerricOxideSmallDust"),
	FerroferricOxideSmallDust("小堆四氧化三铁粉", "watermillFerroferricOxideSmallDust"),
	HighPurityCarbonDust("高纯碳粉", "watermillHighPurityCarbonDust"),
	TungstenCarbideIngot("碳化钨锭", "watermillTungstenCarbideIngot"),
	DiamondBlade("钻石锯片", "watermillDiamondBlade"),
	DiamondGlazingWheel("钻石研磨轮", "watermillDiamondGlazingWheel"),
	BrassCentrifugePot("黄铜离心锅", "watermillBrassCentrifugePot"),
	VSteelWaterPipe("钒钢水管", "watermillVSteelWaterPipe"),
	RubyWaterHole("红宝石喷水口", "watermillRubyWaterHole"),
	DataBall("数据球", "watermillDataBall"),
	DustDiamond("钻石粉", "watermillDustDiamond"),
	IndustrialSteelHydraulicCylinder("工业钢液压钢", "watermillIndustrialSteelHydraulicCylinder"),
	VSteelPistonCylinder("钒钢活塞缸", "watermillVSteelPistonCylinder"),
	DustCactus("仙人掌粉", "watermillDustCactus"),
	DustIron("铁粉", "watermillDustIron"),
	DustGold("金粉", "watermillDustGold"),
	DrawingWaterPart("汲水零件", "watermillDrawingWaterPart"),
	DrawingWaterComponent("汲水组件", "watermillDrawingWaterComponent")
	;
	
	private String showedName;
	
	public String unlocalizedName;
	
	private String information;
	
	private ItemOthers updater;
	
	private ItemType(String showedName, String unlocalizedName) {
		this(showedName, unlocalizedName, null);
	}
	
	private ItemType(String showedName, String unlocalizedName, String information) {
		this.showedName = showedName;
		this.unlocalizedName = unlocalizedName;
		this.information = information;
	}
	
	public String getShowedName() {
		String format = "cptwtrml.updater." + name();
		String s = StatCollector.translateToLocal(format);
		if(format.equals(s)) {
			return showedName;
		}
		return s;
	}
	
	public String getInformation() {
		String format = "cptwtrml.updater.info." + name();
		String s = StatCollector.translateToLocal(format);
		if(format.equals(s)) {
			return information;
		}
		return s;
	}
	
	public ItemStack item() {
		return item(1);
	}
	
	public ItemStack item(int i) {
		return new ItemStack(GlobalItems.updater, i, this.ordinal());
	}

}