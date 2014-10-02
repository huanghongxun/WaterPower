package org.jackhuang.watercraft.common;

import org.jackhuang.watercraft.Reference;

public enum EnergyType {
	EU("IC2/EU"),
	KU("IC2/KU"),
	HU("IC2/HU"),
	MJ("BC6/MJ"),
	RF("TE4/RF"),
	FZ("FZ/Charge"),
	Steam("Steam/mb"),
	Water("Water/mb");
	
	public String showedName;
	
	EnergyType(String showedName) {
		this.showedName = showedName;
	}
	
	public static double EU2MJ(double eu) {
		return eu / Reference.Energy.mj;
	}
	
	public static double MJ2EU(double mj) {
		return mj * Reference.Energy.mj;
	}
	
	public static double EU2RF(double eu) {
		return eu / Reference.Energy.rf;
	}
	
	public static double RF2EU(double rf) {
		return rf * Reference.Energy.rf;
	}
	
	public static double EU2FZ(double eu) {
		return eu / Reference.Energy.charge;
	}
	
	public static double EU2KU(double eu) {
		return eu / Reference.Energy.ku;
	}
	
	public static double EU2HU(double eu) {
		return eu / Reference.Energy.hu;
	}
	
	public static double EU2Steam(double eu) {
		return eu / Reference.Energy.steam;
	}
	
	public static double EU2Water(double eu) {
		return eu / Reference.Energy.water;
	}

}
