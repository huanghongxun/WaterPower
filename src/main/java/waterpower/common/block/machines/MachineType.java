package waterpower.common.block.machines;

import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum MachineType implements IStringSerializable {
	MACERATOR("macerator", TileEntityMacerator.class),
	COMPRESSOR("compressor", TileEntityCompressor.class),
	SAWMILL("sawmill", TileEntitySawmill.class),
	ADVCOMPRESSOR("advancedCompressor", TileEntityAdvancedCompressor.class),
	CENTRIFUGE("centrifuge", TileEntityCentrifuge.class),
	LATHE("lathe", TileEntityLathe.class),
	CUTTER("cutter", TileEntityCutter.class);
	
	String id;
	Class<? extends TileEntityWaterMachine> tileEntityClazz;
	
	private MachineType(String id, Class<? extends TileEntityWaterMachine> clazz) {
		this.id = id;
		tileEntityClazz = clazz;
	}
	
	@Override
	public String getName() {
		return name().toLowerCase();
	}
	
	public TileEntityWaterMachine newInstance() {
		try {
			return tileEntityClazz.newInstance();
		} catch(Exception e) {
			throw new Error(e);
		}
	}
	
	public String getUnlocalizedName() {
		return "cptwtrml.machine." + id;
	}
	
	public void registerTileEntity() {
		GameRegistry.registerTileEntity(tileEntityClazz, getUnlocalizedName());
	}

}
