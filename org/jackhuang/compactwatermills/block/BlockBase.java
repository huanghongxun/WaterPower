package org.jackhuang.compactwatermills.block;

import java.util.Random;
import java.util.logging.Level;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.DefaultIds;
import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.Configuration;

public abstract class BlockBase extends BlockContainer {

	protected final InternalName internalName;
	
	//0-bottom 1-top 2-left 3-front 4-right 5-back
	
	private static final int[][] facingAndSideToSpriteOffset = {
			{ 3, 5, 1, 0, 4, 2 }, { 5, 3, 1, 0, 2, 4 }, { 0, 1, 3, 5, 4, 2 },
			{ 0, 1, 5, 3, 2, 4 }, { 0, 1, 2, 4, 3, 5 }, { 0, 1, 4, 2, 5, 3 } };

	@SideOnly(Side.CLIENT)
	protected Icon[][] textures;

	public BlockBase(Configuration config, InternalName internalName,
			Material material) {
		this(config, internalName, material, ItemBlock.class);
	}

	public BlockBase(Configuration config, InternalName internalName,
			Material material, Class<? extends ItemBlock> itemClass) {
		super(CompactWatermills.getBlockIdFor(config, internalName,
				DefaultIds.get(internalName)), material);

		setUnlocalizedName(internalName.name());
		setCreativeTab(CompactWatermills.creativeTabCompactWatermills);
		setHardness(3.0f);

		this.internalName = internalName;

		GameRegistry.registerBlock(this, itemClass, internalName.name());
	}

	@SideOnly(Side.CLIENT)
	public abstract void registerIcons(IconRegister paramIconRegister);

	@SideOnly(Side.CLIENT)
	public abstract Icon getBlockTexture(IBlockAccess paramIBlockAccess,
			int paramInt1, int paramInt2, int paramInt3, int paramInt4);

	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		int facing = getFacing(meta);
		int index = getTextureIndex(meta);
		int subIndex = getTextureSubIndex(facing, side);

		if (index >= this.textures.length)
			return null;
		try {
			return this.textures[index][subIndex];
		} catch (Exception e) {
			LogHelper.log(Level.SEVERE, "Side: " + side + "\n" + "Block: "
					+ this + "\n" + "Meta: " + meta + "\n" + "Facing: "
					+ facing + "\n" + "Index: " + index + "\n" + "SubIndex: "
					+ subIndex);
		}

		return null;
	}

	public String getUnlocalizedName() {
		return super.getUnlocalizedName().substring(5);
	}

	protected int getFacing(int meta) {
		return 3;
	}

	public int getFacing(IBlockAccess iBlockAccess, int x, int y, int z) {
		int meta = iBlockAccess.getBlockMetadata(x, y, z);

		return getFacing(meta);
	}

	protected String getTextureFolder(int index) {
		return null;
	}

	protected String getTextureName(int index) {
		Item item = Item.itemsList[this.blockID];

		if (!item.getHasSubtypes()) {
			if (index == 0) {
				return getUnlocalizedName();
			}
			return null;
		}

		ItemStack itemStack = new ItemStack(this, 1, index);
		String ret = item.getUnlocalizedName(itemStack);

		if (ret == null) {
			return null;
		}
		return ret.replace("item", "block");
	}
	
	@Override
	public int quantityDropped(Random random) {
		return 1;
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	protected int getTextureIndex(int meta) {
		return meta;
	}

	public final int getTextureSubIndex(int facing, int side) {
		return facingAndSideToSpriteOffset[facing][side];
	}

	protected int getMetaCount() {
		int metaCount = 0;

		while (getTextureName(metaCount) != null)
			metaCount++;

		return metaCount;
	}

	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.common;
	}

}
