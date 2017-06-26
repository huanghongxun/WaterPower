/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.machine

import net.minecraft.entity.player.EntityPlayer
import waterpower.common.block.inventory.SlotInventorySlot


class ContainerCentrifuge(entityPlayer: EntityPlayer, tileEntity: TileEntityCentrifuge)
    : ContainerBaseMachine(entityPlayer, tileEntity) {

    override fun paintLayout() {
        addSlotToContainer(SlotInventorySlot(tileEntity.inputSlot, 0, 26, 26))
        addSlotToContainer(SlotInventorySlot(tileEntity.inputSlot, 1, 26, 44))

        for (i in 0..1)
            addSlotToContainer(SlotInventorySlot(tileEntity.outputSlot, i, 86 + i * 18, 26))
        for (i in 2..3)
            addSlotToContainer(SlotInventorySlot(tileEntity.outputSlot, i, 86 + (i - 2) * 18, 26 + 18))

        for (i in 0..3)
            addSlotToContainer(SlotInventorySlot(tileEntity.upgradeSlot, i, upgradeX, upgradeY + i * 18, 4))
    }

}