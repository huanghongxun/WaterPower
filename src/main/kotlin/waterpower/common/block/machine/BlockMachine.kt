/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.machine

import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraftforge.fml.common.LoaderState
import waterpower.annotations.Init
import waterpower.annotations.NewInstance
import waterpower.common.block.BlockEnumTile
import waterpower.common.init.WPBlocks
import waterpower.common.init.WPItems
import waterpower.common.item.EnumComponent.*
import waterpower.common.item.EnumCrafting
import waterpower.common.item.EnumLevel.*
import waterpower.common.item.ItemComponent.Companion.get
import waterpower.common.item.ItemCrafting
import waterpower.common.recipe.Recipes.craft

@Init
@NewInstance(LoaderState.ModState.PREINITIALIZED)
class BlockMachine : BlockEnumTile<Machines>("machine", Material.IRON, Machines::class.java, Machines.values()) {
    init {
        WPBlocks.machine = this
        WPBlocks.blocks += this

        WPBlocks.compressor = getItemStack(Machines.compressor)
        WPBlocks.crusher = getItemStack(Machines.crusher)
        WPBlocks.advanced_compressor = getItemStack(Machines.advanced_compressor)
        WPBlocks.centrifuge = getItemStack(Machines.centrifuge)
        WPBlocks.lathe = getItemStack(Machines.lathe)
        WPBlocks.cutter = getItemStack(Machines.cutter)
        WPBlocks.sawmill = getItemStack(Machines.sawmill)
    }

    companion object {
        @JvmStatic
        fun postInit() {
            craft(WPBlocks.sawmill, " H ", "CPC", "KKK", 'H', WPItems.hammer, 'C',
                    get(casing, MK1), 'K', Blocks.STONEBRICK, 'P', ItemCrafting.get(EnumCrafting.diamond_blade));
            craft(WPBlocks.sawmill, " H ", "CPC", "KKK", 'H', WPItems.hammer, 'C',
                    get(casing, MK1), 'K', Blocks.STONEBRICK, 'P', Items.FLINT);
            craft(WPBlocks.crusher, " H ", "CPC", "KRK", 'H', WPItems.hammer, 'C',
                    get(casing, MK1), 'K', Blocks.STONEBRICK, 'P', ItemCrafting.get(EnumCrafting.diamond_glazing_wheel), 'R', Blocks.HOPPER);
            craft(WPBlocks.crusher, " H ", "CPC", "KRK", 'H', WPItems.hammer, 'C',
                    get(casing, MK1), 'K', Blocks.STONEBRICK, 'P', Items.FLINT, 'R', Blocks.HOPPER);
            craft(WPBlocks.compressor, "CDC", "DGD", "ICA", 'C', get(casing, MK1), 'D',
                    "plateDenseIndustrialSteel", 'G', ItemCrafting.get(EnumCrafting.industrial_steel_hydraulic_cylinder), 'I',
                    get(circuit, MK1), 'A', get(circuit, MK1));
            craft(WPBlocks.centrifuge, "CBC", "BRB", "XCX", 'C', get(casing, MK4), 'B',
                    ItemCrafting.get(EnumCrafting.brass_centrifuge_pot), 'R', get(rotationAxle, MK4), 'X', ItemCrafting.get(EnumCrafting.data_ball));
            craft(WPBlocks.cutter, "CPC", "SMS", "SSS", 'C', get(casing, MK1), 'P',
                    ItemCrafting.get(EnumCrafting.vanadium_steel_water_pipe), 'M', ItemCrafting.get(EnumCrafting.ruby_water_hole), 'S', "plateManganese");
            craft(WPBlocks.cutter, "A", 'A', WPBlocks.lathe);
            craft(WPBlocks.lathe, "A", 'A', WPBlocks.cutter);
            craft(WPBlocks.advanced_compressor, "CDC", "CPC", "CIC", 'C', get(casing, MK5), 'D',
                    "plateDenseVanadiumSteel", 'P', ItemCrafting.get(EnumCrafting.vanadium_steel_piston_cylinder), 'I', get(circuit, MK4));
        }
    }
}