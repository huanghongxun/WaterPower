package org.jackhuang.compactwatermills.block.turbines;

import java.util.logging.Level;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.block.BlockMultiID;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import com.google.common.base.Throwables;

public class BlockReservoir extends BlockMultiID {

	public BlockReservoir(Configuration config, InternalName name) {
		super(config, name, Material.iron, ItemReservoir.class);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		try {
			return new TileEntityReservoir(ReservoirType.values()[metadata]);
		} catch (Exception e) {
			LogHelper.log(Level.WARNING, "Failed to Register Reservior: "
					+ ReservoirType.values()[metadata].showedName);
			throw Throwables.propagate(e);
		}
	}

	@Override
	protected String getTextureFolder(int index) {
		return "turbine";
	}

}
