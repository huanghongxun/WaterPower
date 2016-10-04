package waterpower.client.render;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;

public interface ITexture {
	public List<BakedQuad> getQuads(Block block, BlockPos pos, EnumFacing facing, float offset);

	public boolean isValidTexture();
}