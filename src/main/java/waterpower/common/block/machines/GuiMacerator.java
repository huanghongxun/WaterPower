package waterpower.common.block.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.Reference;
import waterpower.client.gui.ContainerStandardMachine;

@SideOnly(Side.CLIENT)
public class GuiMacerator extends GuiMachineBase {
    public GuiMacerator(EntityPlayer player, TileEntityStandardWaterMachine tileEntity) {
        super(new ContainerStandardMachine(player, tileEntity));

        this.name = I18n.format("cptwtrml.machine.macerator.name");
        this.inv = I18n.format("container.inventory");
        this.background = new ResourceLocation(Reference.ModID + ":textures/gui/GUIMacerator.png");
    }
}
