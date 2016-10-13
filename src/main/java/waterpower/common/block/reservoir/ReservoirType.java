package waterpower.common.block.reservoir;

import net.minecraft.util.IStringSerializable;
import waterpower.client.Local;

public enum ReservoirType implements IStringSerializable {
	WOOD     (100000, 100, 8  ), 
	STONE    (200000, 100, 16 ),
	LAPIS    (300000, 200, 64 ),
	TIN      (400000, 200, 64 ), 
	COPPER   (500000, 300, 64 ),
	QUARTZ   (700000, 400, 128),
	ZINC     (800000, 500, 128),
	BRONZE   (900000, 400, 128),
	NETHER   (1000000, 1000, 256),
	IRON     (1300000, 500 , 256),
	OBSIDIAN (2500000, 600 , 512),
	STEEL    (2500000, 600 , 1024),
	GOLD     (1800000, 1000, 512 ),
	MANGANESE(2000000, 2000, 2048),
	DIAMOND  (3000000, 5000, 2048),
	VANADIUM (200000000, 10000, 8192);

	public int capacity;
	public int maxUse;

	private ReservoirType(int capacity, int maxUse, int maxOutput) {
		this.capacity = capacity;
		this.maxUse = maxUse;
	}

	public String getShowedName() {
		return Local.get("cptwtrml.reservoir." + name()) + ' ' + Local.get("cptwtrml.reservoir.RESERVOIR");
	}

	public String getTitle() {
		return Local.get("cptwtrml.reservoir." + name());
	}

	public String tileEntityName() {
		return "ReservoirType." + name();
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}
}
