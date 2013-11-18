package org.jackhuang.compactwatermills;

public class UUIDManager {
	
	static int uuids;
	
	public static int registerUUID() {
		return uuids++;
	}
	
	static {
		uuids = 0;
	}

}
