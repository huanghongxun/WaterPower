package waterpower.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import waterpower.common.block.tileentity.TileEntityInventory;

public class ContainerFullInventory extends ContainerBase {

    public ContainerFullInventory(EntityPlayer entityPlayer, TileEntityInventory base, int height) {
        super(base);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(entityPlayer.inventory, col + row * 9 + 9, 8 + col * 18, height + -82 + row * 18));
            }

        }

        for (int col = 0; col < 9; col++)
            addSlotToContainer(new Slot(entityPlayer.inventory, col, 8 + col * 18, height + -24));

    }

}