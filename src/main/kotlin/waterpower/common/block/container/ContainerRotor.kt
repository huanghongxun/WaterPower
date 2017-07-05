/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.container

import net.minecraft.entity.player.EntityPlayer
import waterpower.common.block.inventory.SlotInventorySlot
import waterpower.common.block.tile.TileEntityRotorGenerator

open class ContainerRotor(player: EntityPlayer, val te: TileEntityRotorGenerator)
    : ContainerFullInventory(player, te, 166) {
    init {
        addSlotToContainer(SlotInventorySlot(te.slotRotor, 0, 80, 26))
    }
}