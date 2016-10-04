/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.client.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import waterpower.Reference;

public class RecolorableTextures {
    public static final IIconContainer[] METAL = { ItemIcons.PLATE, ItemIcons.DUST, ItemIcons.DUST_TINY, ItemIcons.DUST_SMALL, ItemIcons.PLATE_DENSE,
            ItemIcons.INGOT, ItemIcons.STICK, ItemIcons.SCREW, ItemIcons.GEAR, ItemIcons.NUGGET, ItemIcons.RING };

    public static final IIconContainer[] CRUSHED = { ItemIcons.CRUSHED };

    public static final IIconContainer[] CRAFTING = { ItemIcons.PADDLE_BASE, ItemIcons.DRAINAGE_PLATE, ItemIcons.FIXED_FRAME, ItemIcons.FIXED_TOOL,
            ItemIcons.ROTATION_AXLE, ItemIcons.OUTPUT_INTERFACE, ItemIcons.ROTOR, ItemIcons.STATOR, ItemIcons.CASING, ItemIcons.CIRCUIT };

    private RecolorableTextures() {
    }
    
    public static void registerToService() {
    	for (ItemIcons i : ItemIcons.values())
    		IconRegisterService.INSTANCE.items.add(i);
    }

    public static enum ItemIcons implements IIconContainer, IIconRegister {
        INGOT, PLATE, PLATE_DENSE, NUGGET, BLOCK, STICK, GEAR, DUST, DUST_SMALL, DUST_TINY, SCREW, RING, PADDLE_BASE, DRAINAGE_PLATE, FIXED_FRAME, FIXED_TOOL, ROTATION_AXLE, OUTPUT_INTERFACE, ROTOR, STATOR, CASING, CIRCUIT, CRUSHED;

        protected TextureAtlasSprite icon = null;

        @Override
        public void registerIcons(TextureMap map) {
            icon = map.registerSprite(new ResourceLocation(Reference.ModID + ":items/iconsets/" + name()));
        }

        @Override
        public TextureAtlasSprite getIcon() {
            return icon;
        }
    }
}
