/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.common;

import org.jackhuang.watercraft.Reference;

public enum EnergyType {
    EU("IC2/EU") {
        @Override
        public double getFromEU(double eu) {
            return eu;
        }
    },
    KU("IC2/KU") {
        @Override
        public double getFromEU(double eu) {
            return EU2KU(eu);
        }
    },
    HU("IC2/HU") {
        @Override
        public double getFromEU(double eu) {
            return EU2HU(eu);
        }
    },
    MJ("BC6/MJ") {
        @Override
        public double getFromEU(double eu) {
            return EU2MJ(eu);
        }
    },
    RF("Cofh/RF") {
        @Override
        public double getFromEU(double eu) {
            return EU2RF(eu);
        }
    },
    Charge("FZ/Charge") {
        @Override
        public double getFromEU(double eu) {
            return EU2Charge(eu);
        }
    },
    Steam("Steam/mb") {
        @Override
        public double getFromEU(double eu) {
            return EU2Steam(eu);
        }
    },
    Water("Water/mb") {
        @Override
        public double getFromEU(double eu) {
            return EU2Water(eu);
        }
    };

    public String showedName;

    EnergyType(String showedName) {
        this.showedName = showedName;
    }

    public abstract double getFromEU(double eu);

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

    public static double EU2Charge(double eu) {
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
