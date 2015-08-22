package org.jackhuang.watercraft.common.block.watermills;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.common.item.others.ItemTrouser;
import org.jackhuang.watercraft.common.item.rotors.ItemRotor;
import org.jackhuang.watercraft.common.item.rotors.RotorType;
import org.jackhuang.watercraft.util.WPLog;

import com.google.common.base.Throwables;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * 
 * @author jackhuang1998
 * 
 */
public enum WaterType {
	MK1(1, 5), MK2(8, 5), MK3(32, 17), MK4(128, 17), MK5(512, 33), MK6(2048, 33), MK7(
			8192, 65), MK8(32768, 65), MK9(131072, 129), MK10(524288, 129), MK11(
			2097152, 255), MK12(8388608, 255), MK13(33554432, 513), MK14(
			134217728, 513), MK15(536870912, 1025), MK16(2147483647, 1025);

	public static TileEntityWatermill makeTileEntity(int metadata) {
		try {
			TileEntityWatermill tileEntity = new TileEntityWatermill();
			return tileEntity;
		} catch (Exception e) {
			WPLog.warn("Failed to Register Watermill: "
					+ WaterType.values()[metadata].name());
			throw Throwables.propagate(e);
		}
	}

	public int output, length, total;

	public ItemTrouser trousers;

	public boolean enable = true;

	int id;

	private WaterType(int output, int length) {
		this.output = output;
		this.length = length;
		id = -1;
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

	private void initTrouser() {
		try {
			if (id == -1) {
				Property trouserId = WaterPower.instance.config.getItem(
						getTrousersUnlocalizedName(), 31600 + ordinal());
				this.id = trouserId.getInt(31800 + ordinal());
				Property enableTrouser = WaterPower.instance.config.get(
						"enable", getTrousersUnlocalizedName(), true);
				enable = enableTrouser.getBoolean(true);
			}
			if (enable)
				trousers = new ItemTrouser(id, this);
		} catch (Exception e) {
			e.printStackTrace();
			WPLog.err("Failed to Register Trousers: " + name());
		}
	}

	public String tileEntityName() {
		return "WaterType." + name();
	}

	public String getShowedName() {
		return StatCollector.translateToLocal("cptwtrml.watermill.WATERMILL")
				+ ' ' + name();
	}

}
