/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory

abstract class ContainerFullInventory : ContainerBase {
    constructor(player: EntityPlayer, inv: IInventory, height: Int) : super(inv) {
        addPlayerInventorySlots(player, height)
    }

    constructor(player: EntityPlayer, inv: IInventory, width: Int, height: Int) : super(inv) {
        addPlayerInventorySlots(player, width, height)
    }
}