/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemBlockBase(block: BlockBase) : ItemBlock(block) {

    override fun placeBlockAt(stack: ItemStack?, player: EntityPlayer?, world: World?, pos: BlockPos?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float, newState: IBlockState?): Boolean {
        return super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)
    }
}