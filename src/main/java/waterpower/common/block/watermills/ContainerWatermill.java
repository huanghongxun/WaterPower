package waterpower.common.block.watermills;

import net.minecraft.entity.player.EntityPlayer;
import waterpower.client.gui.ContainerRotor;
import waterpower.common.block.inventory.SlotInventorySlot;

public class ContainerWatermill extends ContainerRotor {

    public ContainerWatermill(EntityPlayer player, TileEntityWatermill tileEntityCW) {
        super(player, tileEntityCW);
    }

    @Override
	protected void layoutContainer() {
        super.layoutContainer();
        for (int i = 0; i < 4; i++)
            addSlotToContainer(new SlotInventorySlot(((TileEntityWatermill) tileEntity).slotUpdater, i, 152, 8 + i * 18, 2));
    }
}
