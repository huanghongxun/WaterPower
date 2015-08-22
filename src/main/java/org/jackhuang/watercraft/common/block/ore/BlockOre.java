package org.jackhuang.watercraft.common.block.ore;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.block.BlockWaterPower;
import org.jackhuang.watercraft.common.block.GlobalBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOre extends BlockWaterPower {
	public Icon[] icon = new Icon[maxMetaData()];

	public BlockOre() {
		super("cptBlockOre", Material.rock, ItemOre.class);
		
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
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		icon[0] = iconRegister.registerIcon(Reference.ModID + ":ore/cptwtrml.ore.monazite");
		icon[1] = iconRegister.registerIcon(Reference.ModID + ":ore/cptwtrml.ore.vanadium");
		icon[2] = iconRegister.registerIcon(Reference.ModID + ":ore/cptwtrml.ore.manganese");
		icon[3] = iconRegister.registerIcon(Reference.ModID + ":ore/cptwtrml.ore.magnet");
		icon[4] = iconRegister.registerIcon(Reference.ModID + ":ore/cptwtrml.ore.zinc");
	}
	
	@Override
	public Icon getIcon(int side, int meta) {
		return icon[meta];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z,
			int side) {
		int meta = iBlockAccess.getBlockMetadata(x, y, z);
		return getIcon(side, meta);
	}
}
