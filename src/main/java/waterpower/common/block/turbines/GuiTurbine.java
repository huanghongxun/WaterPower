package waterpower.common.block.turbines;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.Reference;
import waterpower.client.gui.ContainerRotor;
import waterpower.common.EnergyType;
import waterpower.util.Utils;

@SideOnly(Side.CLIENT)
public class GuiTurbine extends GuiContainer {
    private TileEntityTurbine gen;

    private GuiButton btnEnergyType;

    public GuiTurbine(EntityPlayer player, TileEntityTurbine gen) {
        super(new ContainerRotor(player, gen));
        this.gen = gen;
        allowUserInput = false;
    }

    @Override
    public void initGui() {
        super.initGui();

        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        btnEnergyType = new GuiButton(1, l + 100, i1 + 24, 30, 20, gen.energyType.name());
        this.buttonList.add(btnEnergyType);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.renderEngine.bindTexture(new ResourceLocation(Reference.ModID + ":textures/gui/GUITurbine.png"));
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(gen.getName(), 8, 6, 0x404040);
        fontRendererObj.drawString(I18n.format("cptwtrml.watermill.ROTOR") + ":", 44, 30, 0x404040);
        fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 0x404040);
        fontRendererObj.drawString(
        		I18n.format("cptwtrml.watermill.OUTPUT") + ": " + Utils.DEFAULT_DECIMAL_FORMAT.format(gen.getFromEU(gen.latestOutput))
                        + gen.energyType.name() + "/t", 8, 50, 0x404040);
    }

    @Override
    protected void actionPerformed(GuiButton p_146284_1_) throws IOException {
        super.actionPerformed(p_146284_1_);

        switch (p_146284_1_.id) {
        case 1:
            EnergyType newType = EnergyType.values()[(gen.energyType.ordinal() + 1) % EnergyType.values().length];
            btnEnergyType.displayString = newType.name();
            gen.onUnitChanged(newType);
            break;
        }
    }
}
