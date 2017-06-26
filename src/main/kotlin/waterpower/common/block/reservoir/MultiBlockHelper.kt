/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.reservoir

import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import waterpower.common.init.WPBlocks
import java.util.*

class MultiBlockHelper(val te: TileEntityReservoir) {
    val world = te.world
    val pos = te.pos
    val property = WPBlocks.reservoir.TYPES
    val type = te.type
    val blockState = world.getBlockState(pos)

    var list = mutableListOf<TileEntityReservoir>()

    fun conjoint(other: BlockPos): Boolean {
        val state = world.getBlockState(other)
        if (state.propertyKeys.contains(property))
            return world.getBlockState(other).getValue(property) == blockState.getValue(property)
        else
            return false
    }

    fun canBeMaster() = !conjoint(pos.down()) && !conjoint(pos.west()) && !conjoint(pos.north())

    /**
     * @param dir allowed facing: east, south
     * @return the size in this direction
     */
    fun searchDirection(dir: EnumFacing): Int {
        var res = 1
        while (res < 65)
            if (conjoint(pos.offset(dir, res)))
                ++res
            else
                break
        return res
    }

    /**
     * O --------> x(east,length)
     * |  \
     * |    \
     * |      \
     * |        \
     * V          >
     * z(south)     y(up)
     * (width)     (height)
     *
     * O -> master block
     */
    fun checkDirection(pos: BlockPos, dir: Vec3i, length: Int, width: Int, tmpList: MutableList<TileEntityReservoir>): Boolean {
        val dx = dir.x
        val dz = dir.z
        for (x in pos.x..pos.x + dx * (length - 1))
            for (z in pos.z..pos.z + dz * (width - 1)) {
                val te1 = world.getTileEntity(BlockPos(x, pos.y, z))
                val te2 = world.getTileEntity(BlockPos(x + (length - 1) * (1 - dx), pos.y, z + (width - 1) * (1 - dz)))
                if (te1 !is TileEntityReservoir || te2 !is TileEntityReservoir ||
                        te1.type != type || te2.type != type) {
                    return false
                } else {
                    tmpList += te1
                    if (te2 != te1)
                        tmpList += te2
                }
            }
        return true
    }

    /**
     * @return the valid height of the reservoir
     */
    fun checkReservoir(pos: BlockPos, length: Int, width: Int): Int {
        val list = mutableListOf<TileEntityReservoir>()
        val tmpList = mutableListOf<TileEntityReservoir>()
        val highest = minOf(pos.y + 64, 250)
        if (!checkDirection(pos, Vec3i(1, 0, 1), length, width, tmpList)) // check floor plane
            return 0
        list += tmpList
        tmpList.clear()
        for (y in pos.y + 1..highest) {
            if (!checkDirection(BlockPos(pos.x, y, pos.z + 1), EnumFacing.SOUTH.directionVec, length, width - 2, tmpList) ||
                    !checkDirection(BlockPos(pos.x, y, pos.z), EnumFacing.EAST.directionVec, length, width, tmpList)) {
                this.list = list
                return y - pos.y
            }
            list += tmpList
            tmpList.clear()
        }
        return highest - pos.y
    }

    fun coveredBlocks(r: Reservoir): Int {
        var res = 0
        for (i in pos.x + 1..pos.x + r.length - 1)
            for (j in pos.z + 1..pos.z + r.width - 1) {
                // if the height of (i, j) is larger than pos.y, there must be a block covering the reservoir
                val height = world.getChunkFromBlockCoords(BlockPos(i, 0, j)).getHeight(BlockPos(i, 0, j))
                if (height > pos.y) ++res
            }
        return res
    }

    @Synchronized
    fun reservoir(r: Reservoir) {
        list = mutableListOf()
        r.width = searchDirection(EnumFacing.SOUTH)
        r.length = searchDirection(EnumFacing.EAST)
        r.height = checkReservoir(pos, r.length, r.width)
        if (r.height <= 1 || r.width <= 2 || r.length <= 2) {
            r.setInvalid()
            list.clear()
        } else r.uuid = UUID.randomUUID()
    }
}