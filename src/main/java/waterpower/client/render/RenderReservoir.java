package waterpower.client.render;

import static net.minecraft.client.renderer.GlStateManager.*;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import net.minecraft.item.ItemStack;

import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fluids.FluidStack;

import net.minecraftforge.fml.relauncher.Side;

import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.common.block.reservoir.TileEntityReservoir;

@SideOnly(Side.CLIENT)
public class RenderReservoir extends TileEntitySpecialRenderer<TileEntityReservoir> {

	@Override

	public void renderTileEntityAt(TileEntityReservoir te, double tx, double ty, double tz, float partialTicks, int destroyStage) {
		if (!te.isMaster()) return;
		FluidStack stack = te.getFluidStackfromTank();

		if (stack != null) {

			float offset = (float) stack.amount / te.getFluidTankCapacity() * (te.size.height - 1);
			for (int i = 1; i <= te.size.getLength() - 2; ++i)
				for (int j = 1; j <= te.size.getWidth() - 2; ++j) {
					pushMatrix();
					enableBlend();
					tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
					BlockPos renderPos = te.getPos().add(i, 1, j);
					RenderUtils.translateToZero();
					translate(0, offset, 0);
					RenderUtils.scaleAndCorrectThePosition(1, offset, 1, renderPos.getX(), renderPos.getY(), renderPos.getZ());
					RenderHelper.disableStandardItemLighting();
					RenderUtils.renderBlock(te.getFluidfromTank().getBlock().getDefaultState(), renderPos);
					RenderHelper.enableStandardItemLighting();
					disableBlend();
					popMatrix();
				}

		}

	}



}
