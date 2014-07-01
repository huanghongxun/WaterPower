package org.jackhuang.compactwatermills.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.Logger;
import org.jackhuang.compactwatermills.helpers.LogHelper;

@SideOnly(Side.CLIENT)
public class BlockTextureStitched extends TextureAtlasSprite {
	private final int subIndex;
	private AnimationMetadataSection animationMeta;
	private BufferedImage comparisonImage;
	private TextureAtlasSprite mappedTexture;
	private int mipmapLevels;
	private static Map<String, CacheEntry> cachedImages;
	private static Map<Integer, List<BlockTextureStitched>> existingTextures;

	public BlockTextureStitched(String name, int subIndex1) {
		super(name);

		this.subIndex = subIndex1;
	}

	public void copyFrom(TextureAtlasSprite textureStitched) {
		if ((textureStitched.getIconName().equals("missingno"))
				&& (this.mappedTexture != null))
			super.copyFrom(this.mappedTexture);
		else
			super.copyFrom(textureStitched);
	}

	public void updateAnimation() {
	}

	public boolean hasCustomLoader(IResourceManager manager,
			ResourceLocation location) {
		return true;
	}

	public boolean load(IResourceManager manager, ResourceLocation location) {
		String name = location.getResourcePath();
		int index = name.indexOf(':');

		if (index != -1) {
			location = new ResourceLocation(location.getResourceDomain(),
					name.substring(0, index));
		}
		location = new ResourceLocation(location.getResourceDomain(),
				"textures/blocks/" + location.getResourcePath() + ".png");
		Field mipmapLevel;
		Field anisotropicFiltering;
		try {
			mipmapLevel = TextureMap.class.getDeclaredField("field_147636_j");
			anisotropicFiltering = TextureMap.class
					.getDeclaredField("field_147637_k");
		} catch (NoSuchFieldException e) {
			try {
				mipmapLevel = TextureMap.class.getDeclaredField("mipmapLevels");
				anisotropicFiltering = TextureMap.class
						.getDeclaredField("anisotropicFiltering");
			} catch (NoSuchFieldException f) {
				throw new RuntimeException(f);
			}
		}

		mipmapLevel.setAccessible(true);
		anisotropicFiltering.setAccessible(true);
		try {
			this.mipmapLevels = mipmapLevel.getInt(Minecraft.getMinecraft()
					.getTextureMapBlocks());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			return loadSubImage(manager.getResource(location));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean loadSubImage(IResource res) throws IOException {
		String name = getIconName();

		CacheEntry cacheEntry = (CacheEntry) cachedImages.get(name);
		BufferedImage bufferedImage;
		if (cacheEntry != null) {
			bufferedImage = cacheEntry.image;
			this.animationMeta = cacheEntry.animationMeta;
		} else {
			bufferedImage = ImageIO.read(res.getInputStream());
			this.animationMeta = ((AnimationMetadataSection) res
					.getMetadata("animation"));

			cachedImages.put(name, new CacheEntry(bufferedImage,
					this.animationMeta));
		}

		int animationLength = 1;

		if ((this.animationMeta != null)
				&& (this.animationMeta.getFrameHeight() > 0)) {
			animationLength = this.animationMeta.getFrameHeight();
			try {
				Field parentAnimationMeta = TextureAtlasSprite.class
						.getDeclaredField("field_110982_k");
				parentAnimationMeta.setAccessible(true);
				parentAnimationMeta.set(this, this.animationMeta);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			this.animationMeta = null;
		}

		int size = bufferedImage.getHeight() / animationLength;
		int count = bufferedImage.getWidth() / size;
		int index = this.subIndex;

		if ((count == 1) || (count == 6) || (count == 12)) {
			index %= count;
		} else if (count == 2) {
			index /= 6;
		} else {
			LogHelper.warn("texture " + name + " is not properly sized");

			throw new IOException();
		}

		this.width = size;
		this.height = size;

		return loadFrames(bufferedImage, index, animationLength);
	}

	public IIcon getRealTexture() {
		return this.mappedTexture == null ? this : this.mappedTexture;
	}

	private boolean loadFrames(BufferedImage image, int index,
			int animationLength) {
		assert (animationLength > 0);

		int totalHeight = this.height * animationLength;

		this.comparisonImage = image.getSubimage(index * this.width, 0,
				this.width, totalHeight);

		int[] rgbaData = new int[this.width * totalHeight];
		this.comparisonImage.getRGB(0, 0, this.width, totalHeight, rgbaData, 0,
				this.width);

		int hash = Arrays.hashCode(rgbaData);

		List<BlockTextureStitched> matchingTextures = (List) existingTextures
				.get(Integer.valueOf(hash));

		if (matchingTextures != null) {
			int[] rgbaData2 = new int[this.width * totalHeight];

			for (BlockTextureStitched matchingTexture : matchingTextures) {
				if ((matchingTexture.width != this.width)
						|| (matchingTexture.comparisonImage.getHeight() != totalHeight)) {
					continue;
				}
				matchingTexture.comparisonImage.getRGB(0, 0, this.width,
						totalHeight, rgbaData2, 0, this.width);

				if (Arrays.equals(rgbaData, rgbaData2)) {
					this.mappedTexture = matchingTexture;

					return true;
				}
			}

			matchingTextures.add(this);
		} else {
			matchingTextures = new ArrayList();
			matchingTextures.add(this);

			existingTextures.put(Integer.valueOf(hash), matchingTextures);
		}
		int pixelsPerFrame = this.width * this.height;
		Method fixTransparentPixels;
		Method prepareAnisotropicFiltering;
		try {
			fixTransparentPixels = TextureAtlasSprite.class.getDeclaredMethod(
					"func_147961_a", new Class[] { int[][].class });
			prepareAnisotropicFiltering = TextureAtlasSprite.class
					.getDeclaredMethod("func_147960_a", new Class[] {
							int[][].class, Integer.TYPE, Integer.TYPE });
		} catch (NoSuchMethodException e) {
			try {
				fixTransparentPixels = TextureAtlasSprite.class
						.getDeclaredMethod("fixTransparentPixels",
								new Class[] { int[][].class });
				prepareAnisotropicFiltering = TextureAtlasSprite.class
						.getDeclaredMethod("prepareAnisotropicFiltering",
								new Class[] { int[][].class, Integer.TYPE,
										Integer.TYPE });
			} catch (NoSuchMethodException f) {
				throw new RuntimeException(f);
			}
		}

		fixTransparentPixels.setAccessible(true);
		prepareAnisotropicFiltering.setAccessible(true);
		Iterator it;
		if ((this.animationMeta != null)
				&& (this.animationMeta.getFrameCount() > 0))
			for (it = this.animationMeta.getFrameIndexSet().iterator(); it
					.hasNext();) {
				Integer frameIndex = (Integer) it.next();
				if (frameIndex.intValue() >= animationLength)
					throw new RuntimeException("invalid frame index: "
							+ frameIndex + " (" + getIconName() + ")");

				while (this.framesTextureData.size() <= frameIndex.intValue())
					this.framesTextureData.add(null);

				int[][] data = new int[1 + this.mipmapLevels][];
				data[0] = Arrays.copyOfRange(rgbaData, frameIndex.intValue()
						* pixelsPerFrame, (frameIndex.intValue() + 1)
						* pixelsPerFrame);
				try {
					fixTransparentPixels.invoke(this, new Object[] { data });
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				this.framesTextureData.set(frameIndex.intValue(), data);
			}
		else {
			for (int i = 0; i < animationLength; i++) {
				int[][] data = new int[1 + this.mipmapLevels][];
				data[0] = Arrays.copyOfRange(rgbaData, i * pixelsPerFrame,
						(i + 1) * pixelsPerFrame);
				try {
					fixTransparentPixels.invoke(this, new Object[] { data });
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				this.framesTextureData.add(data);
			}
		}

		return false;
	}

	public static void onPostStitch() {
		for (List<BlockTextureStitched> textures : existingTextures.values()) {
			for (BlockTextureStitched texture : textures) {
				texture.comparisonImage = null;
			}

		}

		cachedImages.clear();
		existingTextures.clear();
	}

	static {
		cachedImages = new HashMap();
		existingTextures = new HashMap();
	}

	static class CacheEntry {
		final BufferedImage image;
		final AnimationMetadataSection animationMeta;

		CacheEntry(BufferedImage image1, AnimationMetadataSection animationMeta1) {
			this.image = image1;
			this.animationMeta = animationMeta1;
		}
	}
}