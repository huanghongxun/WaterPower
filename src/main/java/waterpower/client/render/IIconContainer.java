/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.client.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface IIconContainer {
	TextureAtlasSprite getIcon();

	default TextureAtlasSprite getOverlayIcon() { return null; }
}
