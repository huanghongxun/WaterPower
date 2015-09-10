package org.jackhuang.watercraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class RenderUtils {
    private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;

    public static IIcon getSafeIcon(IIcon icon) {
        if (icon == null)
            return getMissingIcon();
        return icon;
    }

    public static IIcon getMissingIcon() {
        return ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
    }

    public static IIcon getFluidTexture(Fluid fluid, boolean flowing) {
        if (fluid == null)
            return RenderUtils.getMissingIcon();
        IIcon icon = flowing ? fluid.getFlowingIcon() : fluid.getStillIcon();
        icon = RenderUtils.getSafeIcon(icon);
        return icon;
    }

    public static ResourceLocation getFluidSheet(Fluid fluid) {
        return BLOCK_TEXTURE;
    }

    public static void setColor(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, 1.0F);
    }
}
