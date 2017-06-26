/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.init

import net.minecraft.item.ItemStack
import waterpower.common.block.BlockBase
import waterpower.common.block.machine.BlockMachine
import waterpower.common.block.ore.BlockMaterial
import waterpower.common.block.ore.BlockOre
import waterpower.common.block.reservoir.BlockReservoir
import waterpower.common.block.turbine.BlockTurbine
import waterpower.common.block.watermill.BlockWatermill
import java.util.*

object WPBlocks {
    val blocks = LinkedList<BlockBase>()

    lateinit var ore: BlockOre
    lateinit var material: BlockMaterial
    lateinit var watermill: BlockWatermill
    lateinit var reservoir: BlockReservoir
    lateinit var turbine: BlockTurbine
    lateinit var machine: BlockMachine

    lateinit var compressor: ItemStack
    lateinit var crusher: ItemStack
    lateinit var advanced_compressor: ItemStack
    lateinit var centrifuge: ItemStack
    lateinit var sawmill: ItemStack
    lateinit var lathe: ItemStack
    lateinit var cutter: ItemStack

}