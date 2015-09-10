package org.jackhuang.watercraft.common.block.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.client.gui.ContainerStandardMachine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLathe extends GuiMachineBase {
    public GuiLathe(EntityPlayer player, TileEntityStandardWaterMachine tileEntity) {
        super(new ContainerStandardMachine(player, tileEntity));

        this.name = StatCollector.translateToLocal("cptwtrml.machine.lathe.name");
        this.inv = StatCollector.translateToLocal("container.inventory");
        this.background = new ResourceLocation(Reference.ModID + ":textures/gui/GUILathe.png");
    }
}
