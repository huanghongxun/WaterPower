package waterpower.common.block.reservoir;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.Reference;
import waterpower.client.render.RenderUtils;

@SideOnly(Side.CLIENT)
public class GuiReservoir extends GuiContainer {
    private TileEntityReservoir gen;

    public GuiReservoir(EntityPlayer player, TileEntityReservoir gen) {
        super(new ContainerReservoir(player, gen));
        this.gen = gen;
        allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.renderEngine.bindTexture(new ResourceLocation(Reference.ModID + ":textures/gui/GUIReservoir.png"));
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(gen.getName(), 8, 6, 0x404040);
        fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 0x404040);
        fontRendererObj.drawString(I18n.format("cptwtrml.gui.reservoir.add") + ": " + gen.getLastAddedWater(), 12, 20, 0x404040);
        fontRendererObj.drawString(I18n.format("cptwtrml.gui.capacity") + ": " + gen.getFluidTankCapacity(), 12, 30, 0x404040);
        fontRendererObj.drawString(I18n.format("cptwtrml.gui.stored") + ": " + gen.getFluidAmount(), 12, 40, 0x404040);

        FluidStack f = gen.getFluidStackfromTank();

        TextureAtlasSprite fluid = RenderUtils.getFluidTexture(gen.getFluidStackfromTank(), false);
        if (fluid == null)
            return;
        float percent = (float) gen.getFluidAmount() / gen.getFluidTankCapacity();
        mc.renderEngine.bindTexture(RenderUtils.getFluidSheet(gen.getFluidfromTank()));

        GL11.glColor4f(1, 1, 1, 1);
        int h = (int) (13.0 * percent);
        drawTexturedModalRect(82, 49 - h, fluid, 12, h);

        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        int x = par1 - l, y = par2 - i1;
        if (x >= 82 && x <= 93 && y >= 36 && y <= 48) {
            drawHoveringText(
                    ImmutableList.of((f == null ? I18n.format("cptwtrml.gui.empty") : f.getLocalizedName()), gen.getFluidAmount() + "/"
                            + gen.getFluidTankCapacity() + "mb"), x, y, fontRendererObj);
        }
    }
}
