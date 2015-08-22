package org.jackhuang.watercraft.common.block.machines;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.block.BlockWaterPower;
import org.jackhuang.watercraft.common.block.GlobalBlocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMachines extends BlockWaterPower {

	public BlockMachines() {
		super("cptBlockMachine", Material.iron, ItemMachines.class);

		setHardness(2f);

		GlobalBlocks.macerator = new ItemStack(this, 1, 1);
		GlobalBlocks.compressor = new ItemStack(this, 1, 2);
		GlobalBlocks.sawmill = new ItemStack(this, 1, 3);
		GlobalBlocks.advancedCompressor = new ItemStack(this, 1, 4);
		GlobalBlocks.centrifuge = new ItemStack(this, 1, 5);
		GlobalBlocks.lathe = new ItemStack(this, 1, 6);
		GlobalBlocks.cutter = new ItemStack(this, 1, 7);

		GameRegistry.registerTileEntity(TileEntityMacerator.class,
				"cptwtrml.machine.macerator");
		TileEntityMacerator.init();
		GameRegistry.registerTileEntity(TileEntityCompressor.class,
				"cptwtrml.machine.compressor");
		TileEntityCompressor.init();
		GameRegistry.registerTileEntity(TileEntitySawmill.class,
				"cptwtrml.machine.sawmill");
		TileEntitySawmill.init();
		GameRegistry.registerTileEntity(TileEntityAdvancedCompressor.class,
				"cptwtrml.machine.advancedCompressor");
		TileEntityAdvancedCompressor.init();
		GameRegistry.registerTileEntity(TileEntityCentrifuge.class,
				"cptwtrml.machine.centrifuge");
		TileEntityCentrifuge.init();
		GameRegistry.registerTileEntity(TileEntityLathe.class,
				"cptwtrml.machine.lathe");
		TileEntityLathe.init();
		GameRegistry.registerTileEntity(TileEntityCutter.class,
				"cptwtrml.machine.cutter");
		TileEntityCutter.init();
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.textures = new Icon[maxMetaData()][6];
		
		Icon iconSide = iconRegister.registerIcon(Reference.ModID
				+ ":machine/SIDE");
		Icon iconDown = iconRegister.registerIcon(Reference.ModID
				+ ":machine/DOWN");
		Icon iconUp = iconRegister.registerIcon(Reference.ModID
				+ ":machine/UP");

		for (int i = 1; i <= 7; i++) {
			textures[i][0] = iconDown;
			textures[i][1] = iconUp;
			textures[i][3] = iconRegister.registerIcon(Reference.ModID
					+ ":machine/" + getTextureName(i));
			textures[i][2] = textures[i][4] = textures[i][5] = iconSide;
		}
	}

	@Override
	protected int maxMetaData() {
		return 8;
	}

	@Override
	protected int getTextureIndex(IBlockAccess world, int x, int y, int z,
			int meta) {
		return meta;
	}

	@Override
	protected String getTextureFolder(int index) {
		return "machine";
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		switch (metadata) {
		case 1:
			return new TileEntityMacerator();
		case 2:
			return new TileEntityCompressor();
		case 3:
			return new TileEntitySawmill();
		case 4:
			return new TileEntityAdvancedCompressor();
		case 5:
			return new TileEntityCentrifuge();
		case 6:
			return new TileEntityLathe();
		case 7:
			return new TileEntityCutter();
		default:
			return null;
		}
	}

	public int getComparatorInputOverride(World par1World, int par2, int par3,
			int par4, int par5) {
		TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);
		if (te != null) {
			Class cls = te.getClass();

			if ((te instanceof TileEntityStandardWaterMachine)) {
				TileEntityStandardWaterMachine tem = (TileEntityStandardWaterMachine) te;
				return (int) Math.floor(tem.getProgress() * 15.0F);
			}
		}

		return 0;
	}

	@Override
	public void getSubBlocks(int item, CreativeTabs p_149666_2_, List itemList) {
		for (int i = 1; i <= 7; i++) {
			ItemStack is = new ItemStack(this, 1, i);
			itemList.add(is);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return createTileEntity(var1, 0);
	}
}
