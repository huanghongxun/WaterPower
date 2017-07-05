/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.reservoir

import net.minecraft.block.ITileEntityProvider
import net.minecraft.world.World
import waterpower.client.i18n
import waterpower.common.INameable

enum class Reservoirs(val capacity: Int, val maxExtract: Int, private val teClass: Class<out TileEntityReservoir>) : INameable, ITileEntityProvider {
    wood(100000, 8, TileEntityReservoirWood::class.java),
    stone(200000, 16, TileEntityReservoirStone::class.java),
    lapis(300000, 32, TileEntityReservoirLapis::class.java),
    tin(400000, 64, TileEntityReservoirTin::class.java),
    copper(500000, 64, TileEntityReservoirCopper::class.java),
    quartz(700000, 128, TileEntityReservoirQuartz::class.java),
    zinc_alloy(800000, 128, TileEntityReservoirZincAlloy::class.java),
    bronze(900000, 128, TileEntityReservoirBronze::class.java),
    iron(1300000, 256, TileEntityReservoirIron::class.java),
    obsidian(2500000, 512, TileEntityReservoirObsidian::class.java),
    steel(25000000, 1024, TileEntityReservoirSteel::class.java),
    gold(13000000, 4096, TileEntityReservoirGold::class.java),
    manganese_steel(20000000, 2048, TileEntityReservoirManganeseSteel::class.java),
    diamond(30000000, 4096, TileEntityReservoirDiamond::class.java),
    vanadium_steel(2147483647, 8192, TileEntityReservoirVanadiumSteel::class.java);

    override fun getName() = name.toLowerCase()
    override fun getUnlocalizedName() = "waterpower.rotor." + name
    override fun getLocalizedName() = i18n("waterpower.reservoir.$name") + ' ' + i18n("waterpower.reservoir.reservoir")
    override fun createNewTileEntity(worldIn: World?, meta: Int) = teClass.newInstance()
}