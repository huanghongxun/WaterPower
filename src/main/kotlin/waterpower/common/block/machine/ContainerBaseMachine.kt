/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.machine

import net.minecraft.entity.player.EntityPlayer
import waterpower.common.block.container.ContainerFullInventory
import waterpower.common.block.inventory.SlotInventorySlot

open class ContainerBaseMachine @JvmOverloads constructor(entityPlayer: EntityPlayer,
                                                          val tileEntity: TileEntityBaseMachine,
                                                          protected var outputNum: Int = 2,
                                                          protected var height: Int = 166,
                                                          protected var inputX: Int = 26,
                                                          protected var inputY: Int = 34,
                                                          protected var outputX: Int = 86,
                                                          protected var outputY: Int = 35,
                                                          protected var upgradeX: Int = 152,
                                                          protected var upgradeY: Int = 8)
    : ContainerFullInventory(entityPlayer, tileEntity, height) {

    init {
        paintLayout()
    }

    protected open fun paintLayout() {
        addSlotToContainer(SlotInventorySlot(tileEntity.inputSlot, 0, inputX, inputY))

        for (i in 0 until outputNum)
            addSlotToContainer(SlotInventorySlot(tileEntity.outputSlot, i, outputX + i * 18, outputY))

        for (i in 0..3)
            addSlotToContainer(SlotInventorySlot(tileEntity.upgradeSlot, i, upgradeX, upgradeY + i * 18, 2))
    }
}