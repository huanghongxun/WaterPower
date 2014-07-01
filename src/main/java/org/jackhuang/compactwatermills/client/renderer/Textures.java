package org.jackhuang.compactwatermills.client.renderer;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.Reference;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class Textures {
	public static final IIconContainer[] METAL = {
		ItemIcons.PLATE,
		ItemIcons.DUST,
		ItemIcons.DUST_TINY,
		ItemIcons.DUST_SMALL,
		ItemIcons.PLATE_DENSE,
		ItemIcons.INGOT,
		ItemIcons.BLOCK,
		ItemIcons.STICK,
		ItemIcons.SCREW,
		ItemIcons.GEAR,
		ItemIcons.NUGGET,
		ItemIcons.RING,
		ItemIcons.CRUSHED
	};
	
	public static void load() {
		for(ItemIcons i : ItemIcons.values()) {
			i.icon = CompactWatermills.instance.iconRegister.registerIcon(Reference.ModID + ":iconsets/" + i.name());
		}
	}
	
	public static enum ItemIcons implements IIconContainer {
		INGOT,
		PLATE,
		PLATE_DENSE,
		NUGGET,
		BLOCK,
		STICK,
		GEAR,
		DUST,
		DUST_SMALL,
		DUST_TINY,
		SCREW,
		RING,
		CRUSHED;
		
		protected IIcon icon = null;

		@Override
		public IIcon getIcon() {
			return icon;
		}
	}
}
