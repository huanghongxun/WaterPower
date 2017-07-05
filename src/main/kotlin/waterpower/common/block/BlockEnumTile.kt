/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block

import ic2.api.tile.IWrenchable
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.common.Optional
import waterpower.common.INameable
import waterpower.common.block.tile.TileEntityBase
import waterpower.integration.IDs

@Optional.InterfaceList(Optional.Interface(iface = "ic2.api.tile.IWrenchable", modid = IDs.IndustrialCraft2, striprefs = true))
abstract class BlockEnumTile<T>(id: String, material: Material, typeClass: Class<T>, val typesTile: Array<T>)
    : BlockEnum<T>(id, material, typeClass, typesTile), ITileEntityProvider, IWrenchable
where T : Enum<T>, T : INameable, T : ITileEntityProvider {

    init {
        defaultState = defaultState.withProperty(FACINGS, EnumFacing.NORTH)
    }

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        if (meta >= typesTile.size) return null
        else return typesTile[meta].createNewTileEntity(worldIn, meta)
    }

    override fun createBlockStateImpl()
            = BlockStateContainer(this, TYPES, FACINGS)

    protected open fun getDirection(state: IBlockState): EnumFacing {
        return getDirection(getMetaFromState(state))
    }

    protected open fun getDirection(meta: Int): EnumFacing {
        return EnumFacing.NORTH
    }

    protected open fun setDirection(sta: IBlockState, side: EnumFacing): Boolean {
        return false
    }

    fun getDirection(iBlockAccess: IBlockAccess, pos: BlockPos): EnumFacing {
        val te = iBlockAccess.getTileEntity(pos)

        if (te is TileEntityBase)
            return te.getFacing()
        else
            return getDirection(iBlockAccess.getBlockState(pos))
    }

    fun setDirection(iBlockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean {
        val te = iBlockAccess.getTileEntity(pos)

        if (te is TileEntityBase)
            return te.setFacing(side)
        else
            return setDirection(iBlockAccess.getBlockState(pos), side)
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    override fun getFacing(world: World, pos: BlockPos): EnumFacing {
        return getDirection(world, pos)
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    override fun setFacing(world: World, pos: BlockPos, facing: EnumFacing, player: EntityPlayer): Boolean {
        return setDirection(world, pos, facing)
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    override fun wrenchCanRemove(world: World, pos: BlockPos, player: EntityPlayer): Boolean {
        return true
    }

    @Optional.Method(modid = IDs.IndustrialCraft2)
    override fun getWrenchDrops(world: World, pos: BlockPos, state: IBlockState, te: TileEntity, player: EntityPlayer, fortune: Int): List<ItemStack> {
        return getDrops(world, pos, state, te, fortune)
    }
}