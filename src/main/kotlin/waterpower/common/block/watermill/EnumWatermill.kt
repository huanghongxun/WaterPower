/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.watermill

import net.minecraft.block.ITileEntityProvider
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import waterpower.client.i18n
import waterpower.common.INameable

enum class EnumWatermill(private val _tier: Int, val checkSize: Int, private val teClass: Class<out TileEntityWatermill>) : INameable, ITileEntityProvider {
    MK1(-1, 5, TileEntityWatermillMK1::class.java), // 2, 0
    MK2(0, 5, TileEntityWatermillMK2::class.java), // 8, 0
    MK3(1, 17, TileEntityWatermillMK3::class.java), // 32, 1
    MK4(2, 17, TileEntityWatermillMK4::class.java), // 128, 2
    MK5(3, 33, TileEntityWatermillMK5::class.java), // 512, 3
    MK6(4, 33, TileEntityWatermillMK6::class.java), // 2048, 4
    MK7(5, 65, TileEntityWatermillMK7::class.java), // 8192, 5
    MK8(6, 65, TileEntityWatermillMK8::class.java), // 32768, 6
    MK9(7, 129, TileEntityWatermillMK9::class.java), // 131072, 7
    MK10(8, 129, TileEntityWatermillMK10::class.java), // 524288, 8
    MK11(9, 255, TileEntityWatermillMK11::class.java), // 2097152, 9
    MK12(10, 255, TileEntityWatermillMK12::class.java), // 8388608, 10
    MK13(11, 513, TileEntityWatermillMK13::class.java), // 33554432, 11
    MK14(12, 513, TileEntityWatermillMK14::class.java), // 134217728, 12
    MK15(13, 1025, TileEntityWatermillMK15::class.java), // 536870912, 13
    MK16(14, 1025, TileEntityWatermillMK16::class.java); // 2147483648, 14

    fun getTier() = if (_tier < 0) 0 else _tier

    fun getOutput() = if (_tier == 14) 2147483647 else if (_tier < 0) 8 shr ((-_tier) * 2) else 8 shl (_tier * 2)

    override fun getName() = name.toLowerCase()

    override fun getUnlocalizedName() = "waterpower.watermill." + getName()

    override fun getLocalizedName() = i18n("waterpower.watermill.watermill") + " " + name

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity = teClass.newInstance()
}