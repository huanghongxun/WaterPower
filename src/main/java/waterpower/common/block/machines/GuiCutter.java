package waterpower.common.block.machines;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import waterpower.Reference;
import waterpower.client.Local;
import waterpower.client.gui.ContainerStandardMachine;

public class GuiCutter extends GuiMachineBase {
	
	public static final String TITLE_I18N = "cptwtrml.machine.cutter.name";
	public static final ResourceLocation GUI = new ResourceLocation(Reference.ModID + ":textures/gui/GUICutter.png");
	
    public GuiCutter(EntityPlayer player, TileEntityStandardWaterMachine tileEntity) {
        super(new ContainerStandardMachine(player, tileEntity));

        this.name = Local.get(TITLE_I18N);
        this.background = GUI;
    }
}