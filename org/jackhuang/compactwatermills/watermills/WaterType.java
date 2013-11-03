/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package org.jackhuang.compactwatermills.watermills;


import java.util.logging.Level;

import org.jackhuang.compactwatermills.helpers.LogHelper;

import com.google.common.base.Throwables;

/**
 * 
 * @author jackhuang1998
 * 
 */
public enum WaterType {
	ELV(8, "超低压水力发电机", TileEntityWatermill.class),
	LV(32, "低压水力发电机", TileEntityLV.class),
	MV(128, "中压水力发电机", TileEntityMV.class),
	HV(512, "高压水力发电机", TileEntityHV.class),
	EV(2048, "超高压水力发电机", TileEntityEV.class);
	
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
	
	public int output;
	
	public Class<? extends TileEntityWatermill> claSS;
	
	public String showedName;
	
	private WaterType(int output, String showedName,
		Class<? extends TileEntityWatermill> claSS) {
		this.output = output;
		this.showedName = showedName;
		this.claSS = claSS;
	}
	
	public String tileEntityName() {
		return "WaterType." + name();
	}
	
}
