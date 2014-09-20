package org.jackhuang.watercraft.common;

public enum EnergyType {
	EU("EU"),
	KU("KU"),
	MJ("MJ"),
	RF("RF"),
	FZ("Charge");
	
	public String showedName;
	
	EnergyType(String showedName) {
		this.showedName = showedName;
	}
	
	public static double EU2MJ(double eu) {
		return eu/2.5;
	}
	
	public static double MJ2EU(double mj) {
		return mj*2.5;
	}
	
	public static double EU2RF(double eu) {
		return eu / 0.12;
	}
	
	public static double RF2EU(double rf) {
		return rf * 0.12;
	}
	
	public static double EU2FZ(double eu) {
		return eu * 10;
	}

}
