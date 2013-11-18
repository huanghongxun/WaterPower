package org.jackhuang.compactwatermills.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jackhuang.compactwatermills.helpers.LogHelper;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.Resource;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class BlockTextureStitched extends TextureAtlasSprite {
	private final int subIndex;
	private AnimationMetadataSection animationMeta;
	private BufferedImage comparisonImage;
	private TextureAtlasSprite mappedTexture;
	private static Map<String, BlockTextureStitched.CacheEntry> cachedImages = new HashMap<String, BlockTextureStitched.CacheEntry>();
	private static Map<Integer, List<BlockTextureStitched>> existingTextures = new HashMap<Integer, List<BlockTextureStitched>>();

	public BlockTextureStitched(String name, int subIndex) {
		super(name);

		this.subIndex = subIndex;
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

	public boolean load(ResourceManager manager, ResourceLocation location)
			throws IOException {
		String name = location.getResourcePath();
		int index = name.indexOf(':');

		if (index != -1) {
			int extStart = name.lastIndexOf('.');
			location = new ResourceLocation(location.getResourceDomain(),
					name.substring(0, index) + name.substring(extStart));
		}

		return loadSubImage(manager.getResource(location));
	}

	public boolean loadSubImage(Resource res) throws IOException {
		String name = getIconName();

		BlockTextureStitched.CacheEntry cacheEntry = (BlockTextureStitched.CacheEntry) cachedImages
				.get(name);
		AnimationMetadataSection animationMeta;
		BufferedImage bufferedImage;
		if (cacheEntry != null) {
			bufferedImage = cacheEntry.image;
			animationMeta = cacheEntry.animationMeta;
		} else {
			bufferedImage = ImageIO.read(res.getInputStream());
			animationMeta = (AnimationMetadataSection) res
					.getMetadata("animation");

			cachedImages.put(name, new BlockTextureStitched.CacheEntry(
					bufferedImage, animationMeta));
		}

		int animationLength = 1;

		if ((animationMeta != null) && (animationMeta.getFrameHeight() > 0)) {
			animationLength = animationMeta.getFrameHeight();
			this.animationMeta = animationMeta;
			try {
				Field parentAnimationMeta = TextureAtlasSprite.class
						.getDeclaredField("field_110982_k");
				parentAnimationMeta.setAccessible(true);
				parentAnimationMeta.set(this, animationMeta);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		int size = bufferedImage.getHeight() / animationLength;
		int count = bufferedImage.getWidth() / size;
		int index = this.subIndex;

		if ((count == 1) || (count == 6) || (count == 12)) {
			index %= count;
		} else if (count == 2) {
			index /= 6;
		} else {
			LogHelper.log("texture " + name + " is not properly sized");

			throw new IOException();
		}

		this.width = size;
		this.height = size;

		return loadFrames(bufferedImage, index, animationLength);
	}

	public Icon getRealTexture() {
		return this.mappedTexture == null ? this : this.mappedTexture;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean loadFrames(BufferedImage image, int index,
			int animationLength) {
		int totalHeight = this.height * animationLength;

		this.comparisonImage = image.getSubimage(index * this.width, 0,
				this.width, totalHeight);

		int[] rgbaData = new int[this.width * totalHeight];
		this.comparisonImage.getRGB(0, 0, this.width, totalHeight, rgbaData, 0,
				this.width);

		int hash = Arrays.hashCode(rgbaData);

		List<BlockTextureStitched> matchingTextures = existingTextures
				.get(Integer.valueOf(hash));

		if (matchingTextures != null) {
			int[] rgbaData2 = new int[this.width * totalHeight];

			for (BlockTextureStitched matchingTexture : matchingTextures) {
				if ((matchingTexture.comparisonImage.getWidth() != this.width)
						|| (matchingTexture.comparisonImage.getHeight() != totalHeight)) {
					continue;
				}
				matchingTexture.comparisonImage.getRGB(0, 0, this.width,
						totalHeight, rgbaData2, 0, this.width);

				if (Arrays.equals(rgbaData, rgbaData2)) {
					this.mappedTexture = matchingTexture;

					return false;
				}
			}

			matchingTextures.add(this);
		} else {
			matchingTextures = new ArrayList();
			matchingTextures.add(this);

			existingTextures.put(Integer.valueOf(hash), matchingTextures);
		}

		int pixelsPerFrame = this.width * this.height;
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

				this.framesTextureData.set(
						frameIndex.intValue(),
						Arrays.copyOfRange(rgbaData, frameIndex.intValue()
								* pixelsPerFrame, (frameIndex.intValue() + 1)
								* pixelsPerFrame));
			}
		else {
			for (int i = 0; i < animationLength; i++) {
				this.framesTextureData.add(Arrays.copyOfRange(rgbaData, i
						* pixelsPerFrame, (i + 1) * pixelsPerFrame));
			}
		}

		return true;
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

	class CacheEntry {
		final BufferedImage image;
		final AnimationMetadataSection animationMeta;

		CacheEntry(BufferedImage image, AnimationMetadataSection animationMeta) {
			this.image = image;
			this.animationMeta = animationMeta;
		}
	}
}