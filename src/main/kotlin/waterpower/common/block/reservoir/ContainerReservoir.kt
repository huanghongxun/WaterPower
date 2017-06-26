/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.reservoir

import net.minecraft.entity.player.EntityPlayer
import waterpower.common.block.container.ContainerFullInventory
import waterpower.common.block.inventory.SlotInventorySlot

class ContainerReservoir(player: EntityPlayer, var tileEntity: TileEntityReservoir) : ContainerFullInventory(player, tileEntity, 166) {
    init {
        // Input slot drawing
        addSlotToContainer(SlotInventorySlot(tileEntity.getFluidSlot(), 0, 128, 17))
        // Output slot drawing
        addSlotToContainer(SlotInventorySlot(tileEntity.getOutputSlot(), 0, 128, 53))
        // Upgrade slot drawing
        for (i in 0..3)
            addSlotToContainer(SlotInventorySlot(tileEntity.getUpgradeSlot(), i, 152, 8 + i * 18, 2))
    }
}
