package org.jackhuang.compactwatermills.common.block.turbines;

import gregtech.api.interfaces.IDebugableBlock;

import java.util.ArrayList;

import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.common.block.BlockMeta;
import org.jackhuang.compactwatermills.common.block.watermills.TileEntityWatermill;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTurbine extends BlockMeta
	implements IDebugableBlock {

	public BlockTurbine() {
		super(InternalName.cptBlockTurbine, Material.iron, ItemTurbine.class);
		
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

	@Override
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
