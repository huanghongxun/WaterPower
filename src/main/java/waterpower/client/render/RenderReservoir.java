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
		if (te.masterBlock == null) return;
		TileEntityReservoir master = (TileEntityReservoir) te.masterBlock;
		FluidStack stack = master.getFluidStackfromTank();

		if (stack != null && master.lastRenderedTick != partialTicks) {
			master.lastRenderedTick = partialTicks;

			float offset = (float) stack.amount / master.getFluidTankCapacity() * (master.size.height - 1);
			for (int i = 1; i <= master.size.getLength() - 2; ++i)
				for (int j = 1; j <= master.size.getWidth() - 2; ++j) {
					pushMatrix();
					enableBlend();
					tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
					BlockPos renderPos = master.getPos().add(i, 1, j);
					RenderUtils.translateToZero();
					translate(0, offset, 0);
					RenderUtils.scaleAndCorrectThePosition(1, offset, 1, renderPos.getX(), renderPos.getY(), renderPos.getZ());
					RenderHelper.disableStandardItemLighting();
					RenderUtils.renderBlock(master.getFluidfromTank().getBlock().getDefaultState(), renderPos);
					RenderHelper.enableStandardItemLighting();
					disableBlend();
					popMatrix();
				}

		}
	}



}
