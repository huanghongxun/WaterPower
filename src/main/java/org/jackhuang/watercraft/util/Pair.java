/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.util;

public class Pair {

    public int k;
    public int t;

    @Override
    public String toString() {
        return "Pair [k=" + k + ", t=" + t + "]";
    }

    public Pair(int k, int t) {
        super();
        this.k = k;
        this.t = t;
    }
    
    public void clear() {
        k = t = 0;
    }
    
    public void add(Pair p2) {
        this.k += p2.k;
        this.t += p2.t;
    }
    

}
