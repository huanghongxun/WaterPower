package waterpower.common.block.machines;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import waterpower.Reference;
import waterpower.client.Local;

public class GuiCentrifuge extends GuiMachineBase {
    public GuiCentrifuge(EntityPlayer player, TileEntityCentrifuge tileEntity) {
        super(new ContainerCentrifuge(player, tileEntity));

        this.name = Local.get("cptwtrml.machine.centrifuge.name");
        this.inv = Local.get("container.inventory");
        this.background = new ResourceLocation(Reference.ModID + ":textures/gui/GUICentrifuge.png");
    }
}