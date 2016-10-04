package waterpower.common.block.machines;

import net.minecraft.util.IStringSerializable;

public enum MachineType implements IStringSerializable {
	MACERATOR,
	COMPRESSOR,
	SAWMILL,
	ADVCOMPRESSOR,
	CENTRIFUGE,
	LATHE,
	CUTTER;
	
	@Override
	public String getName() {
		return name().toLowerCase();
	}

}
