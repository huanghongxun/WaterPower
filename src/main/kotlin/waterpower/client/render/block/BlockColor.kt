/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client.render.block

import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import waterpower.common.block.BlockBase

object BlockColor : IBlockColor {
    override fun colorMultiplier(state: IBlockState, worldIn: IBlockAccess?, pos: BlockPos?, tintIndex: Int): Int {
        if (state.block is BlockBase) {
            return (state.block as BlockBase).colorMultiplier(state, worldIn, pos, tintIndex)
        } else
            return 0
    }

}
