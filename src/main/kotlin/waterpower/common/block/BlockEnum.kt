/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block

import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import waterpower.common.INameable

abstract class BlockEnum<T>(id: String, material: Material, val typeClass: Class<T>)
    : BlockBase({ type = typeClass; typeValues = typeClass.enumConstants; id }(), material, { b -> ItemBlockEnum(b as BlockEnum<T>, typeClass) })
where T : Enum<T>, T : INameable {

    companion object {
        lateinit var type: Class<*>
        lateinit var typeValues: Array<*>
    }

    lateinit var TYPES: PropertyEnum<T>
    val types: Array<T> = typeClass.enumConstants

    init {
        defaultState = blockState.baseState.withProperty(TYPES, types.first())
    }

    fun getStateFromType(t: T) =
            defaultState.withProperty(TYPES, t)

    fun getMetaDataFromType(t: T) =
            t.ordinal

    fun getTypeFromState(state: IBlockState) =
            types[getMetaFromState(state)]

    override fun getStateFromMeta(meta: Int) =
            getStateFromType(if (types == null) typeValues[meta] as T else types[meta])

    override fun getMetaFromState(state: IBlockState) =
            getMetaDataFromType(state.getValue(TYPES))

    final override fun createBlockState(): BlockStateContainer {
        TYPES = PropertyEnum.create("type", type as Class<T>)
        return createBlockStateImpl()
    }

    open fun createBlockStateImpl() = BlockStateContainer(this, TYPES)

    override fun maxMetaData() = types.size

    override fun getSubBlocks(tab: CreativeTabs?, list: NonNullList<ItemStack>) {
        for (i in 0 until maxMetaData())
            list.add(ItemStack(this, 1, i))
    }

    fun getItemStack(type: T, amount: Int = 1) =
            getItemStack(getMetaDataFromType(type), amount)

    fun getItemStack(meta: Int, amount: Int = 1) =
            ItemStack(this, amount, meta)
}