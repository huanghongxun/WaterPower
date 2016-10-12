package waterpower.client.render.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import waterpower.common.item.ItemRecolorable;

public class ItemColor implements IItemColor {

	public static final ItemColor INSTANCE = new ItemColor();
	private ItemColor() {
	}
	
	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		if (stack.getItem() instanceof ItemRecolorable)
			return ((ItemRecolorable) stack.getItem()).getColorFromItemstack(stack, tintIndex);
		return 0;
	}

}
