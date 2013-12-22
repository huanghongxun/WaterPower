package org.jackhuang.compactwatermills.block.watermills;

import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.block.BlockMultiID;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockCompactWatermill extends BlockMultiID {
	
	public BlockCompactWatermill(Configuration config, InternalName name) {
		super(config, name, Material.iron, ItemCompactWaterMill.class);		
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return WaterType.makeTileEntity(metadata);
	}
	
	@Override
	protected String getTextureFolder(int index) {
		return "watermill";
	}

	@Override
	protected int maxMetaData() {
		return WaterType.values().length;
	}
	
	
}
