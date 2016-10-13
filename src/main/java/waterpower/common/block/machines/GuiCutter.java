package waterpower.common.block.machines;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import waterpower.Reference;
import waterpower.client.Local;
import waterpower.client.gui.ContainerStandardMachine;

public class GuiCutter extends GuiMachineBase {
    public GuiCutter(EntityPlayer player, TileEntityStandardWaterMachine tileEntity) {
        super(new ContainerStandardMachine(player, tileEntity));

        this.name = Local.get("cptwtrml.machine.cutter.name");
        this.inv = Local.get("container.inventory");
        this.background = new ResourceLocation(Reference.ModID + ":textures/gui/GUICutter.png");
    }
}