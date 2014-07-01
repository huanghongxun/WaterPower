package org.jackhuang.compactwatermills.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.client.renderer.IIconContainer;
import org.jackhuang.compactwatermills.common.item.crafting.MaterialTypes;

public abstract class ItemRecolorable extends ItemBase {

	
	public ItemRecolorable(InternalName internalName) {
		super(internalName);
	}

	public abstract short[] getRGBA(ItemStack stack);

	public abstract IIconContainer getIconContainer(int meta);
	
	@Override
	public IIcon getIconFromDamage(int meta) {
		return getIconContainer(meta).getIcon();
	}
}
