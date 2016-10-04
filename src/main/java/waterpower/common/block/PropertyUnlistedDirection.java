package waterpower.common.block;

import java.util.Collection;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyUnlistedDirection extends PropertyDirection implements IUnlistedProperty<EnumFacing> {

	public PropertyUnlistedDirection(String name, Collection<EnumFacing> values) {
		super(name, values);
	}

	@Override
	public boolean isValid(EnumFacing value) {
		return true;
	}

	@Override
	public Class<EnumFacing> getType() {
		return getValueClass();
	}

	@Override
	public String valueToString(EnumFacing value) {
		return getName(value);
	}

}
