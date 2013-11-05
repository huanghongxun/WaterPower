package org.jackhuang.compactwatermills.block.turbines;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.block.BlockMultiID;

public class BlockReservoir extends BlockMultiID {

	public BlockReservoir(Configuration config, InternalName name) {
		super(config, name, Material.iron, ItemReservoir.class);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

	@Override
	protected String getTextureFolder(int index) {
		return "turbine";
	}
	
}
