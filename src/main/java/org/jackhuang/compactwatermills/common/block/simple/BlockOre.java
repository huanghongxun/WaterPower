package org.jackhuang.compactwatermills.common.block.simple;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.common.block.BlockMultiID;
import org.jackhuang.compactwatermills.common.block.GlobalBlocks;

public class BlockOre extends BlockMultiID {

	public BlockOre() {
		super(InternalName.cptBlockOre, Material.rock, ItemOre.class);
		
		GlobalBlocks.ore = this;

		GlobalBlocks.monaziteOre = new ItemStack(this, 1, 0);
		GlobalBlocks.vanadiumOre = new ItemStack(this, 1, 1);
		GlobalBlocks.manganeseOre = new ItemStack(this, 1, 2);
		GlobalBlocks.magnetOre = new ItemStack(this, 1, 3);
		GlobalBlocks.zincOre = new ItemStack(this, 1, 4);
		
		OreType.registerRecipes();
	}
	
	@Override
	protected String getTextureFolder(int index) {
		return "ore";
	}
	
	@Override
	protected int getTextureIndex(IBlockAccess world, int x, int y, int z,
			int meta) {
		return meta;
	}

	@Override
	protected int maxMetaData() {
		return OreType.values().length;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return null;
	}
}
