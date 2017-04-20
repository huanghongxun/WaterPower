package waterpower.common.block.machines;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import waterpower.Reference;
import waterpower.client.Local;

public class GuiCentrifuge extends GuiMachineBase {
	
	public static final String TITLE_I18N = "cptwtrml.machine.centrifuge.name";
	public static final ResourceLocation GUI = new ResourceLocation(Reference.ModID + ":textures/gui/GUICentrifuge.png");
	
    public GuiCentrifuge(EntityPlayer player, TileEntityCentrifuge tileEntity) {
        super(new ContainerCentrifuge(player, tileEntity));

        this.name = Local.get(TITLE_I18N);
        this.background = GUI;
    }
}