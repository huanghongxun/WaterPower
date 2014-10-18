package org.jackhuang.watercraft.client.render;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

public final class FluidRenderer {
	public static final int DISPLAY_STAGES = 100;
	private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;
	private static Map<Fluid, int[]> flowingRenderCache = new HashMap();
	private static Map<Fluid, int[]> stillRenderCache = new HashMap();
	private static final RenderEntityBlock.RenderInfo liquidBlock = new RenderEntityBlock.RenderInfo();

	public static IIcon getFluidTexture(FluidStack fluidStack, boolean flowing) {
		if (fluidStack == null) {
			return null;
		}
		return getFluidTexture(fluidStack.getFluid(), flowing);
	}

	public static IIcon getFluidTexture(Fluid fluid, boolean flowing) {
		if (fluid == null) {
			return null;
		}
		IIcon icon = flowing ? fluid.getFlowingIcon() : fluid.getStillIcon();
		if (icon == null) {
			icon = ((TextureMap) Minecraft.getMinecraft().getTextureManager()
					.getTexture(TextureMap.locationBlocksTexture))
					.getAtlasSprite("missingno");
		}
		return icon;
	}

	public static ResourceLocation getFluidSheet(FluidStack liquid) {
		if (liquid == null) {
			return BLOCK_TEXTURE;
		}
		return getFluidSheet(liquid.getFluid());
	}

	public static ResourceLocation getFluidSheet(Fluid liquid) {
		return BLOCK_TEXTURE;
	}

	public static void setColorForFluidStack(FluidStack fluidstack) {
		if (fluidstack == null) {
			return;
		}

		int color = fluidstack.getFluid().getColor(fluidstack);
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, 1.0F);
	}

	public static int[] getFluidDisplayLists(FluidStack fluidStack,
			World world, boolean flowing) {
		if (fluidStack == null) {
			return null;
		}
		Fluid fluid = fluidStack.getFluid();
		if (fluid == null) {
			return null;
		}
		Map cache = flowing ? flowingRenderCache : stillRenderCache;
		int[] diplayLists = (int[]) cache.get(fluid);
		if (diplayLists != null) {
			return diplayLists;
		}

		diplayLists = new int[100];

		if (fluid.getBlock() != null) {
			liquidBlock.template = fluid.getBlock();
			liquidBlock.texture[0] = getFluidTexture(fluidStack, flowing);
		} else {
			liquidBlock.template = Blocks.water;
			liquidBlock.texture[0] = getFluidTexture(fluidStack, flowing);
		}

		cache.put(fluid, diplayLists);

		GL11.glDisable(2896);
		GL11.glDisable(3042);
		GL11.glDisable(2884);

		for (int s = 0; s < 100; s++) {
			diplayLists[s] = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(diplayLists[s], 4864);

			liquidBlock.minX = 0.01F;
			liquidBlock.minY = 0.0F;
			liquidBlock.minZ = 0.01F;

			liquidBlock.maxX = 0.99F;
			liquidBlock.maxY = (s / 100.0F);
			liquidBlock.maxZ = 0.99F;

			RenderEntityBlock.renderBlock(liquidBlock, world, 0, 0, 0, false,
					true);

			GL11.glEndList();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(2884);
		GL11.glEnable(3042);
		GL11.glEnable(2896);

		return diplayLists;
	}

	public static class RenderInfo {
		public double minX;
		public double minY;
		public double minZ;
		public double maxX;
		public double maxY;
		public double maxZ;
		public Block baseBlock = Blocks.sand;
		public IIcon texture = null;
		public IIcon[] textureArray = null;
		public boolean[] renderSide = new boolean[6];
		public float light = -1.0F;
		public int brightness = -1;

		public RenderInfo() {
			setRenderAllSides();
		}

		public RenderInfo(Block template, IIcon[] texture) {
			this();
			this.baseBlock = template;
			this.textureArray = texture;
		}

		public RenderInfo(float minX, float minY, float minZ, float maxX,
				float maxY, float maxZ) {
			this();
			setBounds(minX, minY, minZ, maxX, maxY, maxZ);
		}

		public float getBlockBrightness(IBlockAccess iblockaccess, int i,
				int j, int k) {
			return this.baseBlock.getMixedBrightnessForBlock(iblockaccess, i,
					j, k);
		}

		public final void setBounds(double minX, double minY, double minZ,
				double maxX, double maxY, double maxZ) {
			this.minX = minX;
			this.minY = minY;
			this.minZ = minZ;
			this.maxX = maxX;
			this.maxY = maxY;
			this.maxZ = maxZ;
		}

		public final void setRenderSingleSide(int side) {
			Arrays.fill(this.renderSide, false);
			this.renderSide[side] = true;
		}

		public final void setRenderAllSides() {
			Arrays.fill(this.renderSide, true);
		}

		public void rotate() {
			double temp = this.minX;
			this.minX = this.minZ;
			this.minZ = temp;

			temp = this.maxX;
			this.maxX = this.maxZ;
			this.maxZ = temp;
		}

		public void reverseX() {
			double temp = this.minX;
			this.minX = (1.0D - this.maxX);
			this.maxX = (1.0D - temp);
		}

		public void reverseZ() {
			double temp = this.minZ;
			this.minZ = (1.0D - this.maxZ);
			this.maxZ = (1.0D - temp);
		}

		public IIcon getBlockTextureFromSide(int i) {
			if (this.texture != null) {
				return this.texture;
			}

			int index = i;

			if ((this.textureArray == null) || (this.textureArray.length == 0)) {
				return this.baseBlock.getBlockTextureFromSide(index);
			}
			if (index >= this.textureArray.length) {
				index = 0;
			}

			return this.textureArray[index];
		}
	}
}