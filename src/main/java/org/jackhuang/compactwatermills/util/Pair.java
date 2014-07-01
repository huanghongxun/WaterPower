package org.jackhuang.compactwatermills.util;

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
