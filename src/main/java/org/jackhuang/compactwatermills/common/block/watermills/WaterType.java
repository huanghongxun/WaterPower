package org.jackhuang.compactwatermills.common.block.watermills;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.apache.logging.log4j.Level;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.common.item.others.ItemWatermillTrousers;
import org.jackhuang.compactwatermills.common.item.rotors.ItemRotor;
import org.jackhuang.compactwatermills.common.item.rotors.RotorType;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import com.google.common.base.Throwables;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * 
 * @author jackhuang1998
 * 
 */
public enum WaterType {
	MK1(1, "水力发电机MK1", 5),
	MK2(8, "水力发电机MK2", 5),
	MK3(32, "水力发电机MK3", 17),
	MK4(128, "水力发电机MK4", 17),
	MK5(512, "水力发电机MK5", 33),
	MK6(2048, "水力发电机MK6", 33),
	MK7(8192, "水力发电机MK7", 65),
	MK8(32768, "水力发电机MK8", 65),
	MK9(131072, "水力发电机MK9", 129),
	MK10(524288, "水力发电机MK10", 129),
	MK11(2097152, "水力发电机MK11", 255),
	MK12(8388608, "水力发电机MK12", 255),
	MK13(33554432, "水力发电机MK13", 513),
	MK14(134217728, "水力发电机MK14", 513),
	MK15(536870912, "水力发电机MK15", 1025),
	MK16(2147483647, "水力发电机MK16", 1025);
	
	public static TileEntityWatermill makeTileEntity(int metadata) {
		try {
			TileEntityWatermill tileEntity = new TileEntityWatermill();
			return tileEntity;
		}
		catch (Exception e) {
			LogHelper.log(Level.WARN, "Failed to Register Watermill: "
				+ WaterType.values()[metadata].showedName);
			throw Throwables.propagate(e);
		}
	}
	
	public int output, length, total;
	
	private String showedName;
	
	public ItemWatermillTrousers trousers;
	public int trousersId;
	
	private WaterType(int output, String showedName, int length) {
		this.output = output;
		this.showedName = showedName;
		this.length = length;
		
		this.total = length * length * length - 1;
	}
	
	public static void initTrousers() {
		for (WaterType type : WaterType.values()) {
			type.initTrouser();
		}
	}
	
	public String getTrousersUnlocalizedName() {
		return "item.watermill.trousers." + name();
	}
	
	public void getConfig(Configuration config) {
		Property rotorId = config.get("item", getTrousersUnlocalizedName(), Reference.defaultTrousersID + ordinal());
		rotorId.comment = "This is the id of " + showedName + " Item.";
		trousersId = rotorId.getInt(Reference.defaultTrousersID + ordinal());
	}
	
	private void initTrouser() {
		try {
			trousers = (ItemWatermillTrousers) new ItemWatermillTrousers(this);
		}
		catch (Exception e) {
			e.printStackTrace();
			LogHelper.log(Level.ERROR, "Failed to Register Trousers: " + showedName);
		}
	}
	
	public String tileEntityName() {
		return "WaterType." + name();
	}
	
	public String getShowedName() {
		return StatCollector.translateToLocal("cptwtrml.watermill.WATERMILL") + ' ' + name();
	}
	
}
