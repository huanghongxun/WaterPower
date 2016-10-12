/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.common.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.client.render.IIconContainer;
import waterpower.client.render.item.IItemIconContainerProvider;

public abstract class ItemRecolorable extends ItemBase implements IItemIconContainerProvider {

    public ItemRecolorable(String id) {
        super(id);
    }

	@SideOnly(Side.CLIENT)
    public abstract IIconContainer[] getIconContainers();
	
	@SideOnly(Side.CLIENT)
	public abstract int getColorFromItemstack(ItemStack stack, int tintIndex);

    @Override
	@SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(ItemStack stk, int pass) {
        return getIconContainer(stk).getIcon();
    }
}
