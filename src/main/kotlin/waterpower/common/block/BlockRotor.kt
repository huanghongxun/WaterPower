/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block

import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import waterpower.common.block.tile.TileEntityRotorGenerator
import waterpower.common.item.ItemRotor
import waterpower.util.emptyStack
import waterpower.util.isStackEmpty

abstract class BlockRotor<T>(id: String, material: Material, typeClass: Class<T>, types: Array<T>)
    : BlockEnumTile<T>(id, material, typeClass, types)
where T : Enum<T>, T : waterpower.common.INameable, T : ITileEntityProvider {

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, stack: ItemStack?, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val te = worldIn.getTileEntity(pos)
        if (te is TileEntityRotorGenerator) {
            val now = te.slotRotor.get()
            val heldItem = playerIn.getHeldItem(hand)
            if (heldItem != null && heldItem.getItem() is ItemRotor) {
                te.slotRotor.put(heldItem)
                val id = playerIn.inventory.currentItem
                if (id in 0 until 9)
                    playerIn.inventory.mainInventory[id] = emptyStack
                if (!isStackEmpty(now))
                    playerIn.inventory.addItemStackToInventory(now)
                return true
            } else if (heldItem == null && playerIn.isSneaking()) {
                te.slotRotor.put(emptyStack)
                if (!isStackEmpty(now))
                    playerIn.inventory.addItemStackToInventory(now)
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, stack, facing, hitX, hitY, hitZ)
    }
}