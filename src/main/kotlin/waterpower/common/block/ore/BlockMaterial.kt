/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.ore

import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fml.common.LoaderState
import net.minecraftforge.oredict.OreDictionary
import waterpower.annotations.Init
import waterpower.annotations.NewInstance
import waterpower.client.render.block.BlockColor
import waterpower.common.block.BlockEnum
import waterpower.common.init.WPBlocks
import waterpower.common.item.MaterialTypes
import java.awt.Color

@NewInstance(LoaderState.ModState.PREINITIALIZED)
class BlockMaterial : BlockEnum<MaterialTypes>("material_block", Material.ROCK, MaterialTypes::class.java, MaterialTypes.values()) {
    init {
        WPBlocks.material = this
        WPBlocks.blocks += this

        for (material in MaterialTypes.values()) {
            OreDictionary.registerOre("block" + material.name, getItemStack(material))
        }
    }

    override fun colorMultiplier(state: IBlockState, world: IBlockAccess?, pos: BlockPos?, index: Int): Int {
        val type = getTypeFromState(state)
        return Color(type.R, type.G, type.B, type.A).rgb
    }

    companion object {
        @JvmStatic
        @Init(LoaderState.ModState.INITIALIZED, side = 0)
        fun init() {
            Minecraft.getMinecraft().blockColors.registerBlockColorHandler(BlockColor, WPBlocks.material)
        }
    }

}