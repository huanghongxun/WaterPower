/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.watermill

import net.minecraft.entity.player.EntityPlayer
import waterpower.common.block.container.ContainerRotor
import waterpower.common.block.inventory.SlotInventorySlot

class ContainerWatermill(player: EntityPlayer, watermill: TileEntityWatermill)
    : ContainerRotor(player, watermill) {
    init {
        for (i in 0..3)
            addSlotToContainer(SlotInventorySlot((te as TileEntityWatermill).slotUpdater, i, 152, 8 + i * 18, 2))
    }
}