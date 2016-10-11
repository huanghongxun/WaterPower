package waterpower.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import waterpower.common.block.BlockWaterPower;

public class BlockColor implements IBlockColor {

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
		if (state.getBlock() instanceof BlockWaterPower) {
			return ((BlockWaterPower) state.getBlock()).colorMultiplier(state, worldIn, pos, tintIndex);
		} else return 0;
	}

}
