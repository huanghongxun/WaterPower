package waterpower.client;

import net.minecraft.util.text.translation.I18n;

public class Local {
	
	public static String get(String id) {
		return I18n.translateToLocal(id);
	}

}
