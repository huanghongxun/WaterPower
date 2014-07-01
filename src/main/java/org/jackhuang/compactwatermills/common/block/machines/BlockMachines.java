package org.jackhuang.compactwatermills.common.block.machines;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.common.block.BlockMultiID;
import org.jackhuang.compactwatermills.common.block.GlobalBlocks;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityStandardWaterMachine;

import cpw.mods.fml.common.registry.GameRegistry;
import scala.tools.cmd.Meta;

public class BlockMachines extends BlockMultiID {

	public BlockMachines() {
		super( InternalName.cptBlockMachine, Material.iron,
				ItemMachines.class);

		setHardness(2f);
		//setStepSound(soundMetalFootstep);

		GlobalBlocks.macerator = new ItemStack(this, 1, 1);
		GlobalBlocks.compressor = new ItemStack(this, 1, 2);
		GlobalBlocks.sawmill = new ItemStack(this, 1, 3);
		GlobalBlocks.advancedCompressor = new ItemStack(this, 1, 4);
		GlobalBlocks.centrifuge = new ItemStack(this, 1, 5);
		GlobalBlocks.lathe = new ItemStack(this, 1, 6);
		GlobalBlocks.cutter = new ItemStack(this, 1, 7);

		GameRegistry.registerTileEntity(TileEntityMacerator.class,
				"cptwtrml.machine.macerator");
		GameRegistry.registerTileEntity(TileEntityCompressor.class,
				"cptwtrml.machine.compressor");
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
		TileEntity te = par1World.getTileEntity(par2, par3, par4);
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
	public void getSubBlocks(Item item, CreativeTabs p_149666_2_, List itemList) {
		for (int i = 1; i <= 7; i++) {
			ItemStack is = new ItemStack(this, 1, i);
			itemList.add(is);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return createTileEntity(var1, var2);
	}
}
