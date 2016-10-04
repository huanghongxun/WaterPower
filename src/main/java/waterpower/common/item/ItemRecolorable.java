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
import waterpower.client.render.IIconContainer;
import waterpower.client.render.item.IItemIconContainerProvider;

public abstract class ItemRecolorable extends ItemBase implements IItemIconContainerProvider, IItemColor {

    public ItemRecolorable(String id) {
        super(id);
    }

    public abstract IIconContainer[] getIconContainers();

    @Override
    public TextureAtlasSprite getIcon(ItemStack stk, int pass) {
        return getIconContainer(stk).getIcon();
    }
}
