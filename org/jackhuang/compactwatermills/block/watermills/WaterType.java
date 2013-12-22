/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package org.jackhuang.compactwatermills.block.watermills;


import java.util.logging.Level;

import org.jackhuang.compactwatermills.helpers.LogHelper;

import com.google.common.base.Throwables;

/**
 * 
 * @author jackhuang1998
 * 
 */
public enum WaterType {
	UELV(1, "水力发电机MK1", TileEntityUELV.class, 5),
	ELV(8, "水力发电机MK2", TileEntityWatermill.class, 5),
	LV(32, "水力发电机MK3", TileEntityLV.class, 17),
	MV(128, "水力发电机MK4", TileEntityMV.class, 17),
	HV(512, "水力发电机MK5", TileEntityHV.class, 33),
	EV(2048, "水力发电机MK6", TileEntityEV.class, 33),
	UEV(8192, "水力发电机MK7", TileEntityUEV.class, 65);
	
	public static TileEntityWatermill makeTileEntity(int metadata) {
		try {
			TileEntityWatermill tileEntity = values()[metadata].claSS.newInstance();
			return tileEntity;
		}
		catch (Exception e) {
			LogHelper.log(Level.WARNING, "Failed to Register Watermill: "
				+ WaterType.values()[metadata].showedName);
			throw Throwables.propagate(e);
		}
	}
	
	public int output, length;
	
	public Class<? extends TileEntityWatermill> claSS;
	
	public String showedName;
	
	private WaterType(int output, String showedName,
		Class<? extends TileEntityWatermill> claSS, int length) {
		this.output = output;
		this.showedName = showedName;
		this.claSS = claSS;
		this.length = length;
	}
	
	public String tileEntityName() {
		return "WaterType." + name();
	}
	
}
