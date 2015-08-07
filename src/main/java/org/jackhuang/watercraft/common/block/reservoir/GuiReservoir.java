package org.jackhuang.watercraft.common.block.reservoir;

import org.jackhuang.watercraft.Reference;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

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

	mc.renderEngine.bindTexture(new ResourceLocation(Reference.ModID
		+ ":textures/gui/GUIReservoir.png"));
	int l = (width - xSize) / 2;
	int i1 = (height - ySize) / 2;
	drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	fontRendererObj.drawString(gen.getInventoryName(), 8, 6, 0x404040);
	fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 0x404040);
	fontRendererObj.drawString(StatCollector.translateToLocal("cptwtrml.gui.capacity") + ": " + gen.getMaxFluidAmount(), 12, 20, 0x404040);
	fontRendererObj.drawString(StatCollector.translateToLocal("cptwtrml.gui.reservoir.add") + ": " + gen.getLastAddedWater(), 12, 30, 0x404040);
	fontRendererObj.drawString(StatCollector.translateToLocal("cptwtrml.gui.fluid_amount") + ": " + gen.getFluidAmount(), 12, 40, 0x404040);
	fontRendererObj.drawString(StatCollector.translateToLocal("cptwtrml.gui.reservoir.hpWater") + ": " + gen.getHPWater(), 12, 50, 0x404040);

	FluidStack f = gen.getFluidStackfromTank();
	fontRendererObj.drawString(StatCollector.translateToLocal("cptwtrml.gui.stored_fluid") + ": " + (f == null ? StatCollector.translateToLocal("cptwtrml.gui.empty") : f.getLocalizedName()), 12, 60, 0x404040);
    }
}
