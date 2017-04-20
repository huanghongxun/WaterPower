package waterpower.common.block.machines;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.Reference;
import waterpower.client.Local;
import waterpower.client.gui.ContainerStandardMachine;

@SideOnly(Side.CLIENT)
public class GuiLathe extends GuiMachineBase {
	
	public static final String TITLE_I18N = "cptwtrml.machine.lathe.name";
	public static final ResourceLocation GUI = new ResourceLocation(Reference.ModID + ":textures/gui/GUILathe.png");
	
    public GuiLathe(EntityPlayer player, TileEntityStandardWaterMachine tileEntity) {
        super(new ContainerStandardMachine(player, tileEntity));

        this.name = Local.get(TITLE_I18N);
        this.background = GUI;
    }
}
