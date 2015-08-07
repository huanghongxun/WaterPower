package org.jackhuang.watercraft.common.item.others;

import java.util.logging.Level;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.item.GlobalItems;

public enum ItemType {

    WaterUraniumIngot(0, "watermillWaterUraniumIngot"),
    WaterUraniumPlateMK1(1, "watermillWaterUraniumPlateMK1"),
    WaterUraniumPlateMK2(2, "watermillWaterUraniumPlateMK2"),
    WaterUraniumPlateMK3(3, "watermillWaterUraniumPlateMK3"),
    WaterUraniumPlateMK4(4, "watermillWaterUraniumPlateMK4"),
    WaterUraniumPlateMK5(5, "watermillWaterUraniumPlateMK5"),
    WaterUraniumPlateMK6(6, "watermillWaterUraniumPlateMK6"),
    WaterUraniumPlateMK7(7, "watermillWaterUraniumPlateMK7"),
    IR_FE(8, "watermillIrFePlate"),
    MK0(9, "watermillUpdaterMK0"),
    MK1(10, "watermillUpdaterMK1"),
    MK2(11, "watermillUpdaterMK2"),
    MK3(12, "watermillUpdaterMK3"),
    MK4(13, "watermillUpdaterMK4"),
    MK5(14, "watermillUpdaterMK5"),
    MK6(15, "watermillUpdaterMK6"),
    MK7(16, "watermillUpdaterMK7"),
    ReservoirCore(17, "watermillReservoirCore"),
    ReservoirCoreAdvanced(18, "watermillReservoirCoreAdvanced"),
    PlasmaUraniumIngot(19, "plasmaUraniumIngot"),
    PlasmaUraniumAlloyPlate(20, "plasmaUraniumAlloyPlate"),
    WaterUraniumAlloyPlate(21, "waterUraniumAlloyPlate"),
    BaseRotor(22, "watermillBaseRotor"),
    StoneStruct(23, "watermillStoneStruct"),
    WoodenHammer(24, "watermillWoodenHammer"),
    WaterResistantRubber(25, "watermillWaterResistantRubber"),
    WaterResistantRubberPlate(26, "watermillWaterResistantRubberPlate"),
    WaterResistantRubberDensePlate(27, "watermillWaterResistantRubberDensePlate"),
    DenseRedstonePlate(28, "watermillDenseRedstonePlate"),
    DenseCoil(29, "watermillDenseCoil"),
    OxygenEthanolFuel(30, "watermillOxygenEthanolFuel"),
    SilverCoil(31, "watermillSilverCoil"),
    DenseSilverCoil(32, "watermillDenseSilverCoil"),
    //FerricOxideDust(33, "watermillFerricOxide"),
    //FerroferricOxideDust(34, "watermillFerroferricOxide"),
    //FerricOxideSmallDust(35, "watermillFerricOxideSmallDust"),
    //FerroferricOxideSmallDust(36, "watermillFerroferricOxideSmallDust"),
    HighPurityCarbonDust(37, "watermillHighPurityCarbonDust"),
    TungstenCarbideIngot(38, "watermillTungstenCarbideIngot"),
    DiamondBlade(39, "watermillDiamondBlade"),
    DiamondGlazingWheel(40, "watermillDiamondGlazingWheel"),
    BrassCentrifugePot(41, "watermillBrassCentrifugePot"),
    VSteelWaterPipe(42, "watermillVSteelWaterPipe"),
    RubyWaterHole(43, "watermillRubyWaterHole"),
    DataBall(44, "watermillDataBall"),
    DustDiamond(45, "watermillDustDiamond"),
    IndustrialSteelHydraulicCylinder(46, "watermillIndustrialSteelHydraulicCylinder"),
    VSteelPistonCylinder(47, "watermillVSteelPistonCylinder"),
    DustCactus(48, "watermillDustCactus"),
    DustIron(49, "watermillDustIron"),
    DustGold(50, "watermillDustGold"),
    DrawingWaterPart(51, "watermillDrawingWaterPart"),
    DrawingWaterComponent(52, "watermillDrawingWaterComponent");

    public String unlocalizedName;

    private String information;
    //private int ord;
    private ItemOthers updater;

    public static ItemType[] stackPool = new ItemType[100];

    private ItemType(int ord, String unlocalizedName) {
	this.unlocalizedName = unlocalizedName;
	//this.ord = ord;
	//stackPool[ord] = this;
    }

    public String getShowedName() {
	String format = "cptwtrml.updater." + name();
	String s = StatCollector.translateToLocal(format);
	return s;
    }

    public String getInformation() {
	String format = "cptwtrml.updater.info." + name();
	String s = StatCollector.translateToLocal(format);
	if (format.equals(s)) {
	    return StatCollector.translateToLocal("cptwtrml.updater.info");
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
