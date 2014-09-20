/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.client.render;

import org.jackhuang.watercraft.common.item.ItemRecolorable;
import org.jackhuang.watercraft.common.item.crafting.ItemMaterial;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public class RecolorableItemRenderer implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.EQUIPPED
				|| type == ItemRenderType.EQUIPPED_FIRST_PERSON
				|| type == ItemRenderType.ENTITY
				|| type == ItemRenderType.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return type == ItemRenderType.ENTITY;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		int meta = stack.getItemDamage();
		ItemRecolorable item = (ItemRecolorable) stack.getItem();

		GL11.glEnable(GL11.GL_BLEND);

		if (type == ItemRenderType.ENTITY) {
			if (RenderItem.renderInFrame) {
				GL11.glScalef(0.85F, 0.85F, 0.85F);
				GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslated(-0.5D, -0.42D, 0.0D);
			} else {
				GL11.glTranslated(-0.5D, -0.42D, 0.0D);
			}
		}

		GL11.glColor3f(1.0F, 1.0F, 1.0F);

		IIcon icon = stack.getIconIndex();
		if (icon == null)
			return;
		Minecraft.getMinecraft().renderEngine
				.bindTexture(TextureMap.locationItemsTexture);
		GL11.glBlendFunc(770, 771);
		short[] rgb = item.getRGBA(stack);
        GL11.glColor3f(rgb[0] / 255.0F, rgb[1] / 255.0F, rgb[2] / 255.0F);
		if (type.equals(IItemRenderer.ItemRenderType.INVENTORY))
			renderIcon(icon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
		else
			ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(),
					icon.getMinV(), icon.getMinU(), icon.getMaxV(),
					icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);

		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void renderIcon(IIcon icon, double size, double z, float nx,
			float ny, float nz) {
		renderIcon(icon, 0.0D, 0.0D, size, size, z, nx, ny, nz);
	}

	public static void renderIcon(IIcon icon, double xStart, double yStart,
			double xEnd, double yEnd, double z, float nx, float ny, float nz) {
		if (icon == null)
			return;
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(nx, ny, nz);
		if (nz > 0.0F) {
			Tessellator.instance.addVertexWithUV(xStart, yStart, z,
					icon.getMinU(), icon.getMinV());
			Tessellator.instance.addVertexWithUV(xEnd, yStart, z,
					icon.getMaxU(), icon.getMinV());
			Tessellator.instance.addVertexWithUV(xEnd, yEnd, z, icon.getMaxU(),
					icon.getMaxV());
			Tessellator.instance.addVertexWithUV(xStart, yEnd, z,
					icon.getMinU(), icon.getMaxV());
		} else {
			Tessellator.instance.addVertexWithUV(xStart, yEnd, z,
					icon.getMinU(), icon.getMaxV());
			Tessellator.instance.addVertexWithUV(xEnd, yEnd, z, icon.getMaxU(),
					icon.getMaxV());
			Tessellator.instance.addVertexWithUV(xEnd, yStart, z,
					icon.getMaxU(), icon.getMinV());
			Tessellator.instance.addVertexWithUV(xStart, yStart, z,
					icon.getMinU(), icon.getMinV());
		}
		Tessellator.instance.draw();
	}

}
