package org.jackhuang.watercraft.common.block.turbines;

import java.util.ArrayList;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.block.BlockMeta;
import org.jackhuang.watercraft.common.block.BlockRotor;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockTurbine extends BlockRotor {
	
	public BlockTurbine() {
		super("cptBlockTurbine", Material.iron, ItemTurbine.class);
		
		GameRegistry.registerTileEntity(TileEntityTurbine.class,
				"cptwtrml.turbine");
	}
	
	@Override
	protected int getTextureIndex(IBlockAccess iBlockAccess, int x, int y, int z, int meta) {
		TileEntity tTileEntity = iBlockAccess.getTileEntity(x, y, z);
		if(tTileEntity instanceof TileEntityTurbine) {
			if(((TileEntityTurbine)tTileEntity).getType() == null) return meta;
			return ((TileEntityTurbine)tTileEntity).getType().ordinal();
		}
		return meta;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		textures = new IIcon[maxMetaData()][6];
		
		IIcon iconNorth = iconRegister.registerIcon(Reference.ModID + ":turbine/BACK");
		IIcon iconDown = iconRegister.registerIcon(Reference.ModID + ":turbine/DOWN");
		IIcon iconSide = iconRegister.registerIcon(Reference.ModID + ":turbine/SIDE");
		IIcon iconSouth = iconRegister.registerIcon(Reference.ModID + ":turbine/SOUTH");
		
		for (int i = 0; i < maxMetaData(); i++) {
			textures[i][0] = iconDown;
			textures[i][3] = 
                    iconRegister.registerIcon(Reference.ModID + ":turbine/SOUTH_" + getTextureName(i));;
			textures[i][5] = iconNorth;
			textures[i][1] = textures[i][4] = textures[i][2] = iconSide;
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileEntityTurbine();
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityTurbine();
	}

	@Override
	protected String getTextureFolder(int index) {
		return "turbine";
	}

	@Override
	protected int maxMetaData() {
		return TurbineType.values().length;
	}

	public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aX, int aY,
			int aZ, int aLogLevel) {
		ArrayList<String> al = new ArrayList<String>();
		TileEntity tileEntity = aPlayer.worldObj.getTileEntity(aX, aY, aZ);
		if(tileEntity instanceof TileEntityTurbine) {
			TileEntityTurbine te = (TileEntityTurbine) tileEntity;
			if(te.getType() == null)
				al.add("Type: null");
			else
				al.add("Type: " + te.getType().name());
			al.add("Output: " + te.getOfferedEnergy());
			al.add("Water: " + te.getWater());
		} else {
			al.add("Not a turbine tile entity.");
		}
		return al;
	}
}