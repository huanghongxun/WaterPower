package org.jackhuang.compactwatermills.block.turbines;

import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.block.BlockMultiID;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockTurbine extends BlockMultiID {

	public BlockTurbine(Configuration config, InternalName name) {
		super(config, name, Material.iron, ItemTurbine.class);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityTurbine();
	}

	@Override
	protected String getTextureFolder(int index) {
		return "turbine";
	}

	@Override
	protected int maxMetaData() {
		return 1;
	}

}
