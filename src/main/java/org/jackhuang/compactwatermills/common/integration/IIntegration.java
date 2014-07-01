package org.jackhuang.compactwatermills.common.integration;

import java.util.ArrayList;
import java.util.List;

public abstract class IIntegration {
	public static List<IIntegration> integrations;
	
	static {
		integrations = new ArrayList<IIntegration>(10);
	}
	
	public abstract void integrate();
	public String name() {
		return this.getClass().getName();
	}
}
